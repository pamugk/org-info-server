package ru.psu.org_info_server.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrgInfoDto {
    private UUID id;
    private String name;
    private UUID parentId;
    private String parentName;
    private int employeeCount;
}
