package org.senla_project.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.config.WebSecurityConfig;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class RoleControllerTest {

    final JsonParser jsonParser;
    final RoleController roleController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAll_thenReturnAllElements() throws Exception {
        roleController.create(TestData.getRoleCreateDto());
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.getAll(1, 5).getTotalElements(), 1);
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAll_thenReturnFirstSix() throws Exception {
        roleController.create(RoleCreateDto.builder().roleName("User").build());
        roleController.create(RoleCreateDto.builder().roleName("Admin").build());
        roleController.create(RoleCreateDto.builder().roleName("Man in black").build());
        roleController.create(RoleCreateDto.builder().roleName("President").build());
        roleController.create(RoleCreateDto.builder().roleName("Jesus Christ").build());
        roleController.create(RoleCreateDto.builder().roleName("God").build());
        roleController.create(RoleCreateDto.builder().roleName("Reptiloid").build());
        roleController.create(RoleCreateDto.builder().roleName("Anunnak").build());
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.getAll(1, 6).getNumberOfElements(), 6);
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getById_thenReturnElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto createdRole = roleController.create(roleCreateDto);
        mockMvc.perform(get("/roles/{id}", createdRole.getRoleId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdRole.getRoleName(),
                roleCreateDto.getRoleName());
    }

    @Test
    void create_thenThrowUnauthorizedException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenThrowForbiddenException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void create_thenReturnCreatedElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(roleController.getByRoleName(roleCreateDto.getRoleName()).getRoleName(),
                roleCreateDto.getRoleName());
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowForbiddenException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto roleResponseDto = roleController.create(roleCreateDto);
        RoleCreateDto updatedRoleCreateDto = TestData.getUpdatedRoleCreateDto();

        mockMvc.perform(put("/roles/update/{id}", roleResponseDto.getRoleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedRoleCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.getByRoleName(updatedRoleCreateDto.getRoleName()).getRoleName(),
                updatedRoleCreateDto.getRoleName());
        Assertions.assertEquals(roleController.getByRoleName(updatedRoleCreateDto.getRoleName()).getRoleId(),
                roleResponseDto.getRoleId());
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void delete_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto roleResponseDto = roleController.create(roleCreateDto);
        mockMvc.perform(delete("/roles/delete/{id}", roleResponseDto.getRoleId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleController.getById(UUID.fromString(roleResponseDto.getRoleId())));
    }

    @Test
    void getByRoleName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByRoleName_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getByRoleName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getByRoleName_thenReturnElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.create(roleCreateDto);
        mockMvc.perform(get("/roles?role_name={name}", roleCreateDto.getRoleName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        RoleResponseDto roleResponseDto = roleController.getByRoleName(roleCreateDto.getRoleName());
        Assertions.assertEquals(roleResponseDto.getRoleName(),
                roleCreateDto.getRoleName());
    }

}