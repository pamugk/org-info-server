package ru.psu.org_info_server.exceptions;
public class HasChildrenException extends ServiceException {
    public HasChildrenException() { super("Something still has children"); }
    public HasChildrenException(String message) {
        super(message);
    }
}