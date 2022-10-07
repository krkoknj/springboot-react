package com.study.myboard.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @PersistenceContext
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 사용자이름_중복검사() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-display-name");
        user.setPassword("test-password");
        testEntityManager.persist(user);

        User inDB = userRepository.findByUsername("test-user");
        assertThat(inDB).isNotNull();
    }
}