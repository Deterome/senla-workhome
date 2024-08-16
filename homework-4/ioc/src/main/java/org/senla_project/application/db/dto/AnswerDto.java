package org.senla_project.application.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class AnswerDto {

    private String body;
    private UserDto author;
    private int usefulness;
    private Date createTime;

}


