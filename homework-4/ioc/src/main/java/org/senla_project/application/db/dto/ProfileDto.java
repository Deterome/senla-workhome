package org.senla_project.application.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class ProfileDto {

    private String bio;
    private String firstname;
    private String surname;
    private Date birthday;
    private String avatarUrl;
    private int rating;

}
