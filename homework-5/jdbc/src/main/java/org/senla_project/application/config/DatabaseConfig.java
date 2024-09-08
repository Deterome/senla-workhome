package org.senla_project.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor @AllArgsConstructor @Getter
public class DatabaseConfig {

    @Value("${DATABASE_URL}")
    private String url;
    @Value("${DATABASE_USERNAME}")
    private String username;
    @Value("${DATABASE_PASSWORD}")
    private String password;

}