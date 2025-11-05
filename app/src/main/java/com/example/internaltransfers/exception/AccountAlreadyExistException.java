package com.example.internaltransfers.exception;

public class AccountAlreadyExistException extends RuntimeException {

  public AccountAlreadyExistException(String message) {
    super(message);
  }
}
