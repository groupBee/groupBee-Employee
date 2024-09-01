package groupbee.employee.controller;

import groupbee.employee.service.employee.HRService;
import groupbee.employee.service.xml.OdooClient;
import lombok.RequiredArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.Map;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class HRController {
    private final HRService hrService;

    @PostMapping("/info")
    public Map<String,Object> getInfo() throws MalformedURLException, XmlRpcException {
        return hrService.getInfo();
    }

    @PostMapping("/info/id")
    public Object[] getInfoById(String idNumber) throws MalformedURLException, XmlRpcException {
        return hrService.getInfoByIdNumber(idNumber);
    }
}
