package com.epam.esm.exception.handler;

import com.epam.esm.dto.ExceptionDto;
import com.epam.esm.exception.AlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.WrongIdException;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.exception.WrongUrlParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER_HANDLER =
      LogManager.getLogger(ControllersExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AlreadyExistException.class)
  public ResponseEntity<Object> alreadyExistException(AlreadyExistException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(WrongIdException.class)
  public ResponseEntity<Object> wrongIdException(WrongIdException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<Object> authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
    LOGGER_HANDLER.warn("You are not authorized");
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage("You are not authorized");
    return new ResponseEntity<>(exceptionDto, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(WrongInputDataException.class)
  public ResponseEntity<Object> wrongInputData(WrongInputDataException ex) {
    ExceptionDto exceptionDto = new ExceptionDto();
    if (!ex.getErrorMessages().isEmpty()) {
      LOGGER_HANDLER.warn(ex.getErrorMessages());
      ex.getErrorMessages().forEach(exceptionDto::addMessage);
    }
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(WrongUrlParams.class)
  public ResponseEntity<Object> wrongUrlParams(WrongUrlParams ex) {
    ExceptionDto exceptionDto = new ExceptionDto();
    if (!ex.getErrorMessages().isEmpty()) {
      LOGGER_HANDLER.warn(ex.getErrorMessages());
      ex.getErrorMessages().forEach(exceptionDto::addMessage);
    }
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> dataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    Pattern p = Pattern.compile("(ERROR:)(.*?)(\\n)");
    Matcher m = p.matcher(ex.getMessage());
    List<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group());
    }
    LOGGER_HANDLER.warn(matches.get(0));
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(matches.get(0));
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> globalExceptionHandler(Exception ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage() + ". Only 'application/json' is supported.");
    return new ResponseEntity<>(exceptionDto, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOGGER_HANDLER.warn("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(
        "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }
}
