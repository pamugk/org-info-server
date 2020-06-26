package ru.psu.org_info_server.model.dto;

import lombok.*;
import ru.psu.org_info_server.model.transfer.Exists;
import ru.psu.org_info_server.model.transfer.New;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    @Null(groups = {New.class})
    @NotNull(groups = {Exists.class})
    private UUID id;
    @NotBlank
    private String name;
    private UUID organization;
    private UUID chief;
}
