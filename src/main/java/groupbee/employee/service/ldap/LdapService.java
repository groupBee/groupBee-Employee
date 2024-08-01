package groupbee.employee.service.ldap;

import groupbee.employee.dto.LdapDto;
import groupbee.employee.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LdapService {
    private final LdapTemplate ldapTemplate;
    private final RedisService redisService;

    public List<LdapDto> getAllLdapEntries() {
        LdapQuery query = LdapQueryBuilder.query()
                .base("") // base DN 설정
                .where("objectClass").is("person");

        List<Attributes> results = ldapTemplate.search(query, (AttributesMapper<Attributes>) (attributes) -> attributes);

        // Attributes 객체를 LdapEntryDto로 변환
        return results.stream()
                .map(LdapDto::new)
                .collect(Collectors.toList());
    }


    public LdapDto getLdapDto(String uid){
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "person"));

        LdapQuery query = LdapQueryBuilder.query()
               .base("")
                .where("objectClass").is("person")
                .and("uid").is(uid);

        List<Attributes> results = ldapTemplate.search(query, (AttributesMapper<Attributes>) (attributes) -> attributes);

        LdapDto ldapDto = new LdapDto(results.get(0));

        if(results.isEmpty()){
            return null;
        } else {
            // Redis cache에 LdapDto를 저장
            redisService.setValue(uid,ldapDto.getAttributes().get("cn"));
            // Attributes 객체를 LdapEntryDto로 변환
            return new LdapDto(results.get(0));
        }
    }
}
