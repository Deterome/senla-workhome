package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.service.QuestionService;
import org.senla_project.application.util.Exception.EntityNotFoundException;
import org.senla_project.application.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionController implements ControllerInterface<QuestionDto> {

    @Autowired
    private QuestionService service;
    @Autowired
    private JsonParser jsonParser;

    @Override
    public void execute() {
        service.execute();
    }

    @Override
    public String getAllElements() {
        return jsonParser.parseObjectToJson(service.getAllElements());
    }

    @Override
    public String getElementById(@NonNull UUID id) {
        return jsonParser.parseObjectToJson(service.getElementById(id).orElseThrow(() -> new EntityNotFoundException("Question not found")));
    }

    @Override
    public void addElement(@NonNull QuestionDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull QuestionDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        service.deleteElement(id);
    }

    public Optional<UUID> findQuestionId(String header, String body, String authorName) {
        return service.findQuestionId(header, body, authorName);
    }
}