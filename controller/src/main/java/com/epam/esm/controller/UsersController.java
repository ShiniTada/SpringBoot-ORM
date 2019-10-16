package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.dto.jsonview.Views;
import com.epam.esm.dto.list.GiftCertificateListDto;
import com.epam.esm.dto.list.OrdersListDto;
import com.epam.esm.dto.list.TagListDto;
import com.epam.esm.dto.list.UsersListDto;
import com.epam.esm.entity.RoleEnum;
import com.epam.esm.exception.AlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UsersService;
import com.epam.esm.validator.ControllerValidator;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

  @Autowired private UsersService usersService;
  @Autowired private GiftCertificateService giftCertificateService;
  @Autowired private TagService tagService;

  @GetMapping
  @JsonView({Views.AllUsersView.class})
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public UsersListDto getAllUsers(
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "page_number", required = false) Integer pageNumber,
      @RequestParam(value = "max_results", required = false) Integer maxResults) {
    ControllerValidator.validatePaginationParams(pageNumber, maxResults);
    return new UsersListDto(usersService.getAll(sort, pageNumber, maxResults));
  }

  @GetMapping(value = "/{id}")
  @JsonView({Views.CurrentUsersView.class})
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public UsersDto getUsers(@PathVariable("id") Long id) {
    checkIsCurrentUsersOrAdmin(id);
    UsersDto usersDto = usersService.getById(id);
    if (usersDto == null) {
      throw new ResourceNotFoundException("User with id " + id + " does not exist.");
    }
    return usersDto;
  }

  @GetMapping(value = "/{id}/gift_certificates")
  @JsonView(Views.WithoutTags.class)
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public GiftCertificateListDto getUsersGiftCertificates(
      @PathVariable("id") Long id,
      @RequestParam(value = "page_number", required = false) Integer pageNumber,
      @RequestParam(value = "max_results", required = false) Integer maxResults) {
    ControllerValidator.validatePaginationParams(pageNumber, maxResults);
    checkId(id);
    checkIsCurrentUsersOrAdmin(id);
    return new GiftCertificateListDto(
        giftCertificateService.getGiftCertificatesByUsersId(id, null, pageNumber, maxResults));
  }

  @GetMapping(value = "/{id}/orders")
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public OrdersListDto getUsersListOrders(
      @PathVariable("id") Long id,
      @RequestParam(value = "page_number", required = false) Integer pageNumber,
      @RequestParam(value = "max_results", required = false) Integer maxResults) {
    ControllerValidator.validatePaginationParams(pageNumber, maxResults);
    checkId(id);
    checkIsCurrentUsersOrAdmin(id);
    return new OrdersListDto(usersService.getAllOrdersByUsersId(id, pageNumber, maxResults));
  }

  @GetMapping(value = "/{id}/orders/tags")
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public TagListDto getUsersListTags(
      @PathVariable("id") Long id,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "limit", required = false) Integer limit) {
    ControllerValidator.validateUsersPopularTagsParams(sort, limit);
    checkId(id);
    return new TagListDto(tagService.getMostPopularTags(id, limit));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @JsonView(Views.CurrentUsersView.class)
  public UsersDto signUp(@RequestBody UsersDto usersDto) {
    UsersDto existedUsers = usersService.getByName(usersDto.getName());
    if (existedUsers != null) {
      throw new AlreadyExistException("User with the same name already exist");
    }
    return usersService.create(usersDto);
  }

  @PatchMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @JsonView(Views.CurrentUsersView.class)
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public UsersDto updateUsers(@PathVariable("id") Long id, @RequestBody UsersDto usersDto) {
    checkId(id);
    checkIsCurrentUsers(id);
    return usersService.update(id, usersDto);
  }

  @PostMapping(value = "/{id}/gift_certificates/")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
  public OrdersDto addOrders(
      @PathVariable("id") Long toUsersId, @RequestBody GiftCertificateDto giftCertificateDto) {
    checkId(toUsersId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UsersDto currentUsers = usersService.getByName(authentication.getName());
    return usersService.addOrders(giftCertificateDto.getId(), toUsersId, currentUsers.getId());
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void delete(@PathVariable("id") Long id) {
    checkId(id);
    usersService.deleteById(id);
  }

  private void checkIsCurrentUsers(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UsersDto currentUsers = usersService.getByName(authentication.getName());
    if (!currentUsers.getId().equals(id)) {
      throw new AccessDeniedException("Access is denied");
    }
  }private void checkIsCurrentUsersOrAdmin(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UsersDto currentUsers = usersService.getByName(authentication.getName());
    if (!currentUsers.getId().equals(id) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority(RoleEnum.ROLE_ADMIN.name()))) {
      throw new AccessDeniedException("Access is denied");
    }
  }

  private void checkId(Long id) {
    UsersDto usersDto = usersService.getById(id);
    if (usersDto == null) {
      throw new ResourceNotFoundException("User with id " + id + " does not exist.");
    }
  }
}
