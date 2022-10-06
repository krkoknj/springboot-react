package com.study.myboard;

import com.study.myboard.error.ApiError;
import com.study.myboard.shared.GenericResponse;
import com.study.myboard.user.User;
import com.study.myboard.user.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    public static final String API_1_0_USERS = "/api/1.0/users";
    @Autowired
    TestRestTemplate testRestTemplate;


    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanUp() {
        userRepository.deleteAll();
    }


    @Test
    public void postUser_whenUserIsValid_receiveOk() {
        User user = createValidUser();

        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_whenUserIsValid_receive_success() {
        User user = createValidUser();

        ResponseEntity<GenericResponse> objectResponseEntity = postSignup(user, GenericResponse.class);
        assertThat(objectResponseEntity.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();
        postSignup(user, Object.class);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        User user = createValidUser();
        postSignup(user, Object.class);
        List<User> users = userRepository.findAll();
        User inDB = users.get(0);
        assertThat(user.getPassword()).isNotEqualTo(inDB.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullUsername_receiveBadRequest() {
        User user = createValidUser();
        user.setUsername(null);
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullDisplayName_receiveBadRequest() {
        User user = createValidUser();
        user.setDisplayName(null);
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullPassword_receiveBadRequest() {
        User user = createValidUser();
        user.setPassword(null);
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasUsernameWithLessThanRequired_receiveBadRequest() {
        User user = createValidUser();
        user.setUsername("abc");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasDisplayNameWithLessThanRequired_receiveBadRequest() {
        User user = createValidUser();
        user.setDisplayName("abc");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setUsername(valueOf256Chars);
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_닉네임이_길이가255를_넘을때_BadRequest를_받습니다() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setDisplayName(valueOf256Chars);
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_비밀번호의_길이가255를_넘을때_BadRequest를_받습니다() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setPassword(valueOf256Chars + "A1");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_비밀번호가_소문자일때_BadRequest를_받습니다() {
        User user = createValidUser();
        user.setPassword("alllowercase");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_비밀번호가_대문자일때_BadRequest를_받습니다() {
        User user = createValidUser();
        user.setPassword("ALLUPPERCASE");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_비밀번호가_모두_숫자일때_BadRequest를_받습니다() {
        User user = createValidUser();
        user.setPassword("123456789");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 사용자_비밀번호가_정규식과_일치할때_OK를_받습니다() {
        User user = createValidUser();
        user.setPassword("P4Word11");
        ResponseEntity<Object> objectResponseEntity = postSignup(user, Object.class);
        assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 사용자_유효성체크_실패시_에러경로받기() {
        User user = new User();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
    }

    @Test
    public void 사용자_유효성체크_실패시_에러와_유효성검사에러받기() {
        User user = new User();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
    }

    

    public <T> ResponseEntity<T> postSignup(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(API_1_0_USERS, request, response);
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-display-name");
        user.setPassword("test-password");
        return user;
    }

}