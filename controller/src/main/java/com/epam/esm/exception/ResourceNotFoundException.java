package com.epam.esm.exception;

public class ResourceNotFoundException extends ControllerException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
