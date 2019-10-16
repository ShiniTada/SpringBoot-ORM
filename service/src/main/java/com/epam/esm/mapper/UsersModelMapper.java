package com.epam.esm.mapper;

import com.epam.esm.dto.UsersDto;
import com.epam.esm.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersModelMapper {

  @Autowired private ModelMapper mapper;

  public Users toEntity(UsersDto dto) {
    return (dto == null)
        ? null
        : new Users(dto.getId(), dto.getName(), dto.getPassword(), dto.getUsersBalance());
  }

  public UsersDto toDto(Users entity) {
    return (entity == null) ? null : mapper.map(entity, UsersDto.class);
  }

  public List<Users> listToEntity(List<UsersDto> usersDtos) {
    return usersDtos.stream().map(this::toEntity).collect(Collectors.toList());
  }

  public List<UsersDto> listToDto(List<Users> usersList) {
    return usersList.stream().map(this::toDto).collect(Collectors.toList());
  }
}
