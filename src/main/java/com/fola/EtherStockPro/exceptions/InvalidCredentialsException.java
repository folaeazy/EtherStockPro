package com.fola.EtherStockPro.exceptions;

public class InvalidCredentialsException extends  RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
