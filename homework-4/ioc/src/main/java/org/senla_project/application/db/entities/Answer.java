package org.senla_project.application.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Answer {

    private int answerId;
    private String body;
    private User author;
    private Question question;
    private int usefulness;
    private Date createTime;

}


