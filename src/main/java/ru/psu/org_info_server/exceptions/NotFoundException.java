package ru.psu.org_info_server.exceptions;
public class NotFoundException  extends ServiceException {
    public NotFoundException() {
        super("Something was not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}