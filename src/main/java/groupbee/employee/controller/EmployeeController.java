package groupbee.employee.controller;

import groupbee.employee.dto.EmailDto;
import groupbee.employee.service.employee.EmployeeService;
import groupbee.employee.service.ldap.LdapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final HttpSession httpSession;
    private final EmployeeService employService;
    private final LdapService ldapService;

    @PutMapping("/employee/sync")
    public ResponseEntity<Map<String,Object>> syncEmployee() {
        return employService.sync(ldapService.getAllLdapEntries());
    }

    @GetMapping("/employee/auth/email")
    public ResponseEntity<EmailDto> getEmail() {
        return employService.getEmail();
    }


    @PostMapping("/employee/auth/login")
    public ResponseEntity<Map<String,Object>> login(@RequestPart("data") Map<String,String> userLoginData) {
        return employService.checkLongin(userLoginData.get("id"), userLoginData.get("passwd"));
    }

    @PostMapping("/employee/auth/logout")
    public ResponseEntity<Map<String,Object>> logout() {
        return employService.logout();
    }

    @GetMapping("/employee/auth/islogin")
    public ResponseEntity<Map<String,Object>> isLogin() {
        return employService.getIsLogin();
    }
    @GetMapping("/employee/auth/changepasswd")
    public ResponseEntity<Map<String,Object>> changePasswd(@RequestPart("data") Map<String,Object> passwdData) {
        return employService.updatePassword(passwdData);
    }

    @GetMapping("/employee/info")
    public ResponseEntity<Map<String,Object>> getEmployeeInfo(HttpServletRequest request) {
        return employService.getEmployeeInfo();
    }

    @GetMapping("/employee/list")
    public ResponseEntity<Map<String,Object>> getEmployeeList() {
        return employService.getEmployeeList();
    }



}
