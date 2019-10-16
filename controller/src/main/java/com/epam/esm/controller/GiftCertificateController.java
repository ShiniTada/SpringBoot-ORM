package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.jsonview.Views;
import com.epam.esm.dto.list.GiftCertificateListDto;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.validator.ControllerValidator;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/gift_certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

  @Autowired private GiftCertificateService giftCertificateService;

  @GetMapping
  @JsonView(Views.WithoutTags.class)
  public GiftCertificateListDto getAllGiftCertificates(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "tags", required = false) List<String> tagNames,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "page_number", required = false) Integer pageNumber,
      @RequestParam(value = "max_results", required = false) Integer maxResults) {

    ControllerValidator.validatePaginationParams(pageNumber, maxResults);
    if (name == null && description == null && tagNames == null) {
      return new GiftCertificateListDto(
          giftCertificateService.getAll(sort, pageNumber, maxResults));
    } else {
      GiftCertificateDto giftCertificateDto = new GiftCertificateDto(name, description);
      if (tagNames != null) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (String tagName : tagNames) {
          tagDtos.add(new TagDto(tagName));
        }
        giftCertificateDto.setListTagDto(tagDtos);
      }
      return new GiftCertificateListDto(
          giftCertificateService.getGiftCertificates(
              giftCertificateDto, sort, pageNumber, maxResults));
    }
  }

  @GetMapping(value = "/{id}")
  public GiftCertificateDto getGiftCertificate(@PathVariable("id") Long id) {
    GiftCertificateDto giftCertificateDto = giftCertificateService.getById(id);
    if (giftCertificateDto == null) {
      throw new ResourceNotFoundException("Gift certificate with id " + id + " does not exist.");
    }
    return giftCertificateDto;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public GiftCertificateDto addGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
    giftCertificateDto = giftCertificateService.create(giftCertificateDto);
    return giftCertificateDto;
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
  public GiftCertificateDto updateGiftCertificate(
      @PathVariable("id") Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
    checkId(id);
    giftCertificateDto = giftCertificateService.update(id, giftCertificateDto);
    return giftCertificateDto;
  }

  @PatchMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public GiftCertificateDto updateGiftCertificatePrice(
      @PathVariable("id") Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
    checkId(id);
    giftCertificateDto = giftCertificateService.updatePrice(id, giftCertificateDto.getPrice());
    return giftCertificateDto;
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void delete(@PathVariable("id") Long id) {
    checkId(id);
    giftCertificateService.deleteById(id);
  }

  private void checkId(Long id) {
    if (giftCertificateService.isDeleted(id)) {
      throw new ResourceNotFoundException("Gift certificate with id " + id + " does not exist.");
    }
  }
}
