package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class CollaborationControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    CollaborationController collabController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        collabController.addElement(TestData.getCollaborationCreateDto());
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/collabs/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollaborationResponseDto createdCollaboration = collabController.addElement(collabCreateDto);
        mockMvc.perform(get("/collabs/{id}", createdCollaboration.getCollabId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdCollaboration.getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void addElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(post("/collabs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void updateElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        CollaborationResponseDto collabResponseDto = collabController.addElement(collabCreateDto);
        CollaborationCreateDto updatedCollaborationCreateDto = TestData.getUpdatedCollaborationCreateDto();

        mockMvc.perform(put("/collabs/update/{id}", collabResponseDto.getCollabId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedCollaborationCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.findCollabByName(updatedCollaborationCreateDto.getCollabName()).getCollabName(),
                updatedCollaborationCreateDto.getCollabName());
        Assertions.assertEquals(collabController.findCollabByName(updatedCollaborationCreateDto.getCollabName()).getCollabId(),
                collabResponseDto.getCollabId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/collabs/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollaborationResponseDto collabResponseDto = collabController.addElement(collabCreateDto);
        mockMvc.perform(delete("/collabs/delete/{id}", collabResponseDto.getCollabId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> collabController.getElementById(UUID.fromString(collabResponseDto.getCollabId())));
    }

    @Test
    void findCollabByName() throws Exception {
        mockMvc.perform(get("/collabs?collab_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.addElement(collabCreateDto);
        mockMvc.perform(get("/collabs?collab_name={name}", collabCreateDto.getCollabName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        CollaborationResponseDto collabResponseDto = collabController.findCollabByName(collabCreateDto.getCollabName());
        Assertions.assertEquals(collabResponseDto.getCollabName(),
                collabCreateDto.getCollabName());
    }

}