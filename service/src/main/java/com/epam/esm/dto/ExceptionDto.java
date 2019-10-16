package com.epam.esm.dto;

import java.util.ArrayList;
import java.util.List;

public class ExceptionDto {

  private List<String> messages = new ArrayList<>();

  public ExceptionDto(List<String> messages) {
    this.messages.addAll(messages);
  }

  public ExceptionDto() {}

  public void addMessage(String message) {
    messages.add(message);
  }

  public List<String> getMessages() {
    return messages;
  }

  @Override
  public String toString() {
    return "ExceptionDto{" + "messages=" + messages + '}';
  }
}
