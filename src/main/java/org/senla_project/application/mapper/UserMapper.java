package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("UserMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class, RoleMapper.class})
public abstract class UserMapper {

    @Named("toUserFromName")
    public User toUserFromName(String username) {
        return User.builder()
                .username(username)
                .build();
    }

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "dto.roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UUID id, UserCreateDto dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "dto.roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UserCreateDto dto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UserResponseDto userResponseDto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toStringListFromRoleSet"})
    public abstract UserCreateDto toUserCreateDto(User entity);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toStringListFromRoleSet"})
    public abstract UserResponseDto toUserResponseDto(User entity);

    public abstract List<User> toUserList(List<UserResponseDto> dtoList);

    public abstract List<UserResponseDto> toUserResponseDtoList(List<User> entityList);

    public String toStringFromUser(User user) {
        return user.getUsername();
    }

    @Named("toStringListFromUserSet")
    public abstract List<String> toStringListFromUserSet(Set<User> usersSet);

    @Named("toUserSetFromStringList")
    public Set<User> toUserSetFromStringList(List<String> usersList) {
        return usersList.stream().map(this::toUserFromName).collect(Collectors.toSet());
    }

}