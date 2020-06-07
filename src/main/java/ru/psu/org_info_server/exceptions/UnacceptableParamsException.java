package ru.psu.org_info_server.exceptions;

public class UnacceptableParamsException extends ServiceException {
    public UnacceptableParamsException() { super("Some params have unacceptable params"); }
    public UnacceptableParamsException(String message) {
        super(message);
    }
}
