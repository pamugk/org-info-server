package ru.psu.org_info_server.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmployeeDto {
    private UUID id;
    private String name;
    private UUID organization;
    private UUID chief;
}
