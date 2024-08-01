package groupbee.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LdapDto {
    private Map<String, Object> attributes = new HashMap<>();

    public LdapDto(Attributes attrs) {
        NamingEnumeration<? extends Attribute> ne = attrs.getAll();
        try {
            while (ne.hasMore()) {
                Attribute attr = ne.next();
                attributes.put(attr.getID(), attr.get());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
