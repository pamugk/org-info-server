package ru.psu.org_info_server.exceptions;
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}