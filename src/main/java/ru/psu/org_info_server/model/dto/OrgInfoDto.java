package ru.psu.org_info_server.model.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgInfoDto {
    private UUID id;
    private String name;
    private UUID parentId;
    private String parentName;
    private int employeeCount;
}
