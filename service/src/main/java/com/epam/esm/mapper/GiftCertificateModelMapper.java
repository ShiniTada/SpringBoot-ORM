package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateModelMapper {

  @Autowired private ModelMapper mapper;

  public GiftCertificate toEntity(GiftCertificateDto dto) {
    return (dto == null)
        ? null
        : new GiftCertificate(
            dto.getId(),
            dto.getName(),
            dto.getDescription(),
            dto.getPrice(),
            dto.getCreationDate(),
            dto.getLastModified(),
            dto.getDurationInDays(),
            false);
  }

  public GiftCertificateDto toDto(GiftCertificate entity) {
    return (entity == null) ? null : mapper.map(entity, GiftCertificateDto.class);
  }

  public List<GiftCertificate> listToEntity(List<GiftCertificateDto> certificatesDto) {
    return certificatesDto.stream().map(this::toEntity).collect(Collectors.toList());
  }

  public List<GiftCertificateDto> listToDto(List<GiftCertificate> certificates) {
    return certificates.stream().map(this::toDto).collect(Collectors.toList());
  }
}
