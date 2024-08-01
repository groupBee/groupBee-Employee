package groupbee.employee.controller;

import groupbee.employee.dto.LdapDto;
import groupbee.employee.service.ldap.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ldapController {
   private final LdapService ldapService;
   private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/ldap")
    public List<LdapDto> ldap() {
        for (LdapDto ldapDto : ldapService.getAllLdapEntries()) {
            System.out.println(ldapDto.getAttributes().get("sn"));
            System.out.println(ldapDto.getAttributes().get("uid"));
            System.out.println(ldapDto.getAttributes().get("mail"));
            System.out.println(ldapDto.getAttributes().get("title"));
            System.out.println(ldapDto.getAttributes().get("userPassword"));
            System.out.println(ldapDto.getAttributes().get("departmentNumber"));
            System.out.println(ldapDto.getAttributes().get("homePhone"));
            System.out.println(ldapDto.getAttributes().get("mobile"));
            System.out.println(ldapDto.getAttributes().get("sambaSID"));
        }
        return  ldapService.getAllLdapEntries();
    }

    @GetMapping("/get/id")
    public LdapDto getLdapDto(String uid){
        return ldapService.getLdapDto(uid);
    }
}
