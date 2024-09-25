package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.CollaborationsJoiningCreateDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.util.TestData;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CollaborationsJoiningServiceTest {

    @Mock
    CollaborationsJoiningRepository collabJoinRepositoryMock;
    @Spy
    CollaborationsJoiningMapper collabJoinMapperSpy;
    @InjectMocks
    CollaborationsJoiningService collabJoinServiceMock;

    @Test
    void addElement() {
        CollaborationsJoiningCreateDto collabJoinCreateDto = TestData.getCollabJoiningCreateDto();
        Mockito.doNothing().when(collabJoinRepositoryMock).create(Mockito.any());
        collabJoinServiceMock.addElement(collabJoinCreateDto);
        Mockito.verify(collabJoinRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        CollaborationsJoiningCreateDto collabJoinCreateDto = TestData.getCollabJoiningCreateDto();
        Mockito.doNothing().when(collabJoinRepositoryMock).update(Mockito.any());
        collabJoinServiceMock.updateElement(UUID.randomUUID(), collabJoinCreateDto);
        Mockito.verify(collabJoinRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(collabJoinRepositoryMock).deleteById(Mockito.any());
        collabJoinServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(collabJoinRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        collabJoinServiceMock.getAllElements();
        Mockito.verify(collabJoinRepositoryMock).findAll();
    }

    @Test
    void findElementById() {
        collabJoinServiceMock.findElementById(UUID.randomUUID());
        Mockito.verify(collabJoinRepositoryMock).findById(Mockito.any());
    }

    @Test
    void findCollaborationsJoining() {
        CollaborationsJoining collabJoin = TestData.getCollabJoining();
        collabJoinServiceMock.findCollabJoin(collabJoin.getUser().getNickname(), collabJoin.getCollab().getCollabName());
        Mockito.verify(collabJoinRepositoryMock).findCollabJoin(Mockito.any(), Mockito.any());
    }
}