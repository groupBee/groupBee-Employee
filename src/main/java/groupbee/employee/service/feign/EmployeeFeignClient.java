package groupbee.employee.service.feign;

import groupbee.employee.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "employee", url = "${FEIGN_BASE_URL}",configuration = FeignConfig.class)
public interface EmployeeFeignClient {
    @GetMapping("/api/employee/auth/info")
    Map<String,Object> getUserInfo();
}
