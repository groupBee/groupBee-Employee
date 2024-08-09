package groupbee.employee.controller;

import groupbee.employee.dto.EmployeeDto;
import groupbee.employee.service.employee.EmployeeService;
import groupbee.employee.service.ldap.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employService;
    private final LdapService ldapService;

    @PutMapping("/employee/sync")
    public Map<String,Object> syncEmployee() {
        return employService.sync(ldapService.getAllLdapEntries());
    }

    @PostMapping("/employee/auth/login")
    public Map<String,Object> login(@RequestPart("data") Map<String,String> userLoginData) {
        return employService.checkLongin(userLoginData.get("id"), userLoginData.get("passwd"));
    }

    @PostMapping("/employee/auth/logout")
    public Map<String,Object> logout() {
        return employService.logout();
    }

    @GetMapping("/employee/info")
    public Map<String,Object> getEmployeeInfo() {
        return employService.getEmployeeInfo();
    }
}
