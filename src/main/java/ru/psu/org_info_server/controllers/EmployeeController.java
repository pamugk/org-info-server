package ru.psu.org_info_server.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.*;
import ru.psu.org_info_server.model.transfer.Exists;
import ru.psu.org_info_server.model.transfer.New;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public Response<UUID> createEmployee(@Validated(New.class) @RequestBody EmployeeDto newEmployee) {
        return Response.<UUID>builder().data(service.createEmployee(newEmployee)).build();
    }

    @DeleteMapping
    public void deleteEmployee(@RequestParam UUID id) {
        service.deleteEmployee(id);
    }

    @GetMapping
    public Response<EmployeeDto> getEmployeeInfo(@RequestParam UUID id) {
        return Response.<EmployeeDto>builder().data(service.getEmployeeInfo(id)).build();
    }

    @GetMapping("/list")
    public Response<ListChunk<EmployeeInfoDto>> getEmployeeList(@RequestParam(defaultValue = "0") @Min(0) int offset,
                                                                @RequestParam Optional<@Min(0) Integer> limit,
                                                                @RequestParam(defaultValue = "") String search,
                                                                @RequestParam(required = false) UUID[] exclude) {
        return Response.<ListChunk<EmployeeInfoDto>>builder()
                .data(service.getEmployeeList(limit.orElse(null), offset, search, exclude)).build();
    }

    
    @GetMapping("/tree")
    public Response<List<TreeNode<EmployeeDto>>> getEmployeeTree(@RequestParam(required = false) UUID id) {
         return Response.<List<TreeNode<EmployeeDto>>>builder().data(service.getEmployeeTree(id)).build();
    }

    @PutMapping
    public void updateEmployee(@Validated(Exists.class) @RequestBody EmployeeDto updatedEmployee) {
        service.updateEmployee(updatedEmployee);
    }
}
