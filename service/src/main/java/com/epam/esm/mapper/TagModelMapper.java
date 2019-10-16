package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagModelMapper {

  @Autowired private ModelMapper mapper;

  public Tag toEntity(TagDto dto) {
    return (dto == null) ? null : mapper.map(dto, Tag.class);
  }

  public TagDto toDto(Tag entity) {
    return (entity == null) ? null : mapper.map(entity, TagDto.class);
  }

  public List<Tag> listToEntity(List<TagDto> tagsDto) {
    return tagsDto.stream().map(this::toEntity).collect(Collectors.toList());
  }

  public List<TagDto> listToDto(List<Tag> tags) {
    return tags.stream().map(this::toDto).collect(Collectors.toList());
  }
}
