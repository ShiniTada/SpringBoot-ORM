package com.epam.esm.exception;

import java.util.ArrayList;
import java.util.List;

public class WrongUrlParams extends ControllerException {

	private final List<String> errorMessages = new ArrayList<>();

	public WrongUrlParams(String message) {
		errorMessages.add(message);
	}

	public WrongUrlParams(List<String> errorMessages) {
		this.errorMessages.addAll(errorMessages);
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
}
