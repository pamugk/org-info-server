package ru.psu.org_info_server.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.psu.org_info_server.model.transfer.Exists;
import ru.psu.org_info_server.model.transfer.New;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Getter
@Setter
public class EmployeeDto {
    @Null(groups = {New.class})
    @NotNull(groups = {Exists.class})
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private UUID organization;
    private UUID chief;
}
