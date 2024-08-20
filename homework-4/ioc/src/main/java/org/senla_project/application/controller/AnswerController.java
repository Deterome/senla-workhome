package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.service.AnswerService;
import org.senla_project.application.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnswerController implements ControllerInterface<AnswerDto> {

    @Autowired
    private AnswerService service;
    @Autowired
    private JsonParser jsonParser;

    @Override
    public void execute() {
        service.execute();
    }

    @SneakyThrows
    @Override
    public String getAllElements() {
        return jsonParser.parseObjectToJson(service.getAllElements());
    }

    @SneakyThrows
    @Override
    public String getElementById(@NonNull UUID id) {
        return jsonParser.parseObjectToJson(service.getElementById(id));
    }

    @Override
    public void addElement(@NonNull AnswerDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        service.deleteElement(id);
    }

    public UUID findAnswerId(String authorName, UUID questionId, String body) {
        return service.findAnswerId(authorName, questionId, body);
    }
}
