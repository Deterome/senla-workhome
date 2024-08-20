package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.service.ProfileService;
import org.senla_project.application.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileController implements ControllerInterface<ProfileDto> {

    @Autowired
    private ProfileService service;
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
    public void addElement(@NonNull ProfileDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull ProfileDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        service.deleteElement(id);
    }

    public UUID findProfileId(String nickname) {
        return service.getProfileId(nickname);
    }
}
