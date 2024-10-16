package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.service.CollaborationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collabs")
@RequiredArgsConstructor
public class CollaborationController implements DefaultControllerInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    final private CollaborationService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CollaborationResponseDto> getAllElements(@RequestParam(name="page") int pageNumber) {
        return service.findAllElements(pageNumber);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CollaborationResponseDto addElement(@NonNull @RequestBody CollaborationCreateDto element) {
        return service.addElement(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody CollaborationCreateDto updatedElement) {
        return service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteElement(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteElement(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto findCollabByName(@NonNull @RequestParam(name = "collab_name") String collabName) {
        return service.findCollabByName(collabName);
    }

}