package com.fola.EtherStockPro.exceptions;

public class ValueRequiredException extends  RuntimeException{
    public ValueRequiredException(String message) {
        super(message);
    }
}
