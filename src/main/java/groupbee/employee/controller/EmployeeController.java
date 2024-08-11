package groupbee.employee.controller;

import groupbee.employee.service.employee.EmployeeService;
import groupbee.employee.service.feign.EmployeeFeignClient;
import groupbee.employee.service.ldap.LdapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final HttpSession httpSession;
    private final EmployeeService employService;
    private final LdapService ldapService;
    private final EmployeeFeignClient employeeFeignClient;

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
    public Map<String,Object> getEmployeeInfo(HttpServletRequest request) {
        return employService.getEmployeeInfo();
    }

    @GetMapping("employee/auth/info")
    public Map<String,Object> getAuthInfo(HttpServletRequest request) {
        return employService.getAuthEmployeeInfo(request.getHeader("Cookie"));
    }

    @GetMapping("/employee/test")
    public Map<String,Object> test() {
        return employeeFeignClient.getUserInfo();
    }
}
