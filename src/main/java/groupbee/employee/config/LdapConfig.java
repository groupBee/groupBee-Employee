package groupbee.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.SearchScope;

@Configuration
public class LdapConfig {

    private String ldapUrl = System.getenv("LDAP_URL");
    private String ldapBase = System.getenv("LDAP_BASE");
    private String ldapUsername = System.getenv("LDAP_USERNAME");
    private String ldapPassword = System.getenv("LDAP_PASSWORD");


    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUrl(ldapUrl);
        source.setBase(ldapBase);
        source.setUserDn(ldapUsername);
        source.setPassword(ldapPassword);
        return source;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }
}
