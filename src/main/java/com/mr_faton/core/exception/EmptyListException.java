package com.mr_faton.core.exception;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 06.10.2015
 */
public class EmptyListException extends Exception {
    public EmptyListException(String message) {
        super(message);
    }

    public EmptyListException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyListException() {
    }

    public EmptyListException(Throwable cause) {
        super(cause);
    }
}
