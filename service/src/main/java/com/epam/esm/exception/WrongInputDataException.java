package com.epam.esm.exception;

import java.util.ArrayList;
import java.util.List;

public class WrongInputDataException extends ServiceException {

  private final List<String> errorMessages = new ArrayList<>();

  public WrongInputDataException(String message) {
    errorMessages.add(message);
  }

  public WrongInputDataException(List<String> errorMessages) {
    this.errorMessages.addAll(errorMessages);
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }
}
