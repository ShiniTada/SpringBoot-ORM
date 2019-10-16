package com.epam.esm.exception;

public class ControllerException extends RuntimeException {

  public ControllerException() {}

  public ControllerException(String message) {
    super(message);
  }
}
