package com.study.myboard.shared;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자가 없으면 jackson databind 가 작동하지 않는다
public class GenericResponse {
    private String message;

    public GenericResponse(String message) {
        this.message = message;
    }

}
