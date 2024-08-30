package groupbee.employee.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "rocketChat", url = "http://chat.groupbee.co.kr" )
public interface RocketChatFeignClient {

    @PostMapping(value = "/api/v1/users.create", headers = {"X-Auth-Token=jedFwXTaOb61G4W2xwiDV5UVTl8OSIwnRY0ZcGmpgvA","X-User-Id=Gyxy6EKKnwDz83K5D"})
    public Map<String, Object> register(Map<String, Object> data); // <String, Object>

    @PostMapping("/api/v1/login")
    public Map<String, Object> login(Map<String, Object> data);

    @PostMapping(value = "/api/v1/users.update", headers = {"X-Auth-Token=jedFwXTaOb61G4W2xwiDV5UVTl8OSIwnRY0ZcGmpgvA","X-User-Id=Gyxy6EKKnwDz83K5D"})
    public Map<String, Object> update(Map<String, Object> data);
}
