package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    QuestionRepository questionRepositoryMock;
    @Spy
    QuestionMapper questionMapperSpy;
    @InjectMocks
    QuestionService questionServiceMock;

    @Test
    void addElement() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionServiceMock.addElement(questionCreateDto);
        Mockito.verify(questionRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionServiceMock.updateElement(UUID.randomUUID(), questionCreateDto);
        Mockito.verify(questionRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(questionRepositoryMock).deleteById(Mockito.any());
        questionServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(questionRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        try {
            questionServiceMock.getAllElements();
            Mockito.verify(questionRepositoryMock).findAll();
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findElementById() {
        try {
            questionServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(questionRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findQuestionByParams() {
        try {
            Question question = TestData.getQuestion();
            questionServiceMock.findQuestionByParams(question.getHeader(), question.getBody(), question.getAuthor().getNickname());
            Mockito.verify(questionRepositoryMock).findQuestion(Mockito.any(), Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}