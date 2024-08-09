package groupbee.employee.controller;

import groupbee.employee.dto.DepartmentDto;
import groupbee.employee.service.employee.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/department/all")
    public List<DepartmentDto> getDepartments() {
        return departmentService.findAll();
    }
}
