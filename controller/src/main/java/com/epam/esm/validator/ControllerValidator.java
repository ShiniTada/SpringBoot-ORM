package com.epam.esm.validator;

import com.epam.esm.exception.WrongUrlParams;

import java.util.ArrayList;
import java.util.List;

public final class ControllerValidator {

  private static final int MIN_PAGE_NUMBER = 1;
  private static final int MIN_MAX_RESULTS = 1;
  private static final int MAX_MAX_RESULTS = 30;

  private ControllerValidator() {}

  public static void validatePaginationParams(Integer pageNumber, Integer maxResults) {
    List<String> exceptionMessages = new ArrayList<>();
    if (pageNumber == null) {
      exceptionMessages.add("Param 'page_number' is required.");
    } else if (pageNumber < MIN_PAGE_NUMBER) {
      exceptionMessages.add("Param 'page_number' should be greater then 0.");
    }
    if (maxResults == null) {
      exceptionMessages.add("Param 'max_results' is required.");
    } else if (maxResults < MIN_MAX_RESULTS || maxResults > MAX_MAX_RESULTS) {
      exceptionMessages.add("Param 'max_results' should be greater then 0, but less than 30.");
    }
    if (!exceptionMessages.isEmpty()) {
      throw new WrongUrlParams(exceptionMessages);
    }
  }

  public static void validateUsersPopularTagsParams(String sort, Integer limit) {
    List<String> exceptionMessages = new ArrayList<>();
    if (sort == null || !sort.equals("popularity")) {
      exceptionMessages.add("Available 'sort' params: popularity.");
    }
    if (limit == null) {
      exceptionMessages.add("Param 'limit' is required.");
    } else if (limit < MIN_MAX_RESULTS || limit > MAX_MAX_RESULTS) {
      exceptionMessages.add("Param 'limit' should be greater then 0, but less than 30.");
    }
    if (!exceptionMessages.isEmpty()) {
      throw new WrongUrlParams(exceptionMessages);
    }
  }
}
