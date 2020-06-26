package ru.psu.org_info_server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDto {
    private UUID id;
    private String name;
    private UUID chief;
    private String chiefName;
    private UUID organization;
    private String organizationName;
}
