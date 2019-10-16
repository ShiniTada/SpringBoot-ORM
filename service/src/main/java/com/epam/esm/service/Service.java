package com.epam.esm.service;

import com.epam.esm.exception.WrongIdException;

import java.util.List;

public interface Service<T> {

  /**
   * Get all objects
   *
   * @param sort - field by which the sorting will be. If sort is null sorting will be by id
   * @param pageNumber - number or page
   * @param maxResults - how much elements should be shown on page
   * @return all objects
   */
  List<T> getAll(String sort, int pageNumber, int maxResults);

  /**
   * Get object by id
   *
   * @param id - object id
   * @return object
   */
  T getById(long id);

  /**
   * Create object
   *
   * @param t - object
   * @return created object. If it's {@code null} the object was not created
   */
  T create(T t);

  /**
   * Update object
   *
   * @param id - id of modified object
   * @param t - object-dto with fields which should be changed
   * @return modified object-dto
   * @throws WrongIdException throws if object with {@code id} does not exist.
   */
  T update(long id, T t);

  /**
   * Delete object by id
   *
   * @param id - object id
   */
  void deleteById(long id);
}
