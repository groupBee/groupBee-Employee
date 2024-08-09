package groupbee.employee.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "employee", url = "${FEIGN_BASE_URL}")
public interface EmployeeFeignClient {
    @GetMapping("/api/employee/info")
    Map<String,Object> getUserInfo();
}
