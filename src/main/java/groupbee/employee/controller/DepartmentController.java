package groupbee.employee.controller;

import groupbee.employee.dto.department.DepartmentDto;
import groupbee.employee.service.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/department/all")
    public List<DepartmentDto> getDepartments() {
        return departmentService.findAll();
    }
}
