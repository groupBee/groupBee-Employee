package groupbee.employee.service.feign;

import groupbee.employee.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MailService", url = "${FEIGN_BASE_URL}", configuration = FeignConfig.class)
public interface MailFeignClient {
    @PostMapping(value = "/add/mailbox" , headers = {"X-API-Key=E63DEF-77C26B-9A104F-C6AEBF-B2ED8D"})
    public List<Map<String, Object>> addMailbox(Map<String,Object> map);

    @PostMapping(value = "/edit/mailbox" , headers = {"X-API-Key=E63DEF-77C26B-9A104F-C6AEBF-B2ED8D"})
    public List<Map<String, Object>> editMailbox(Map<String,Object> map);
}
