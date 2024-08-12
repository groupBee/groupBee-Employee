package groupbee.employee.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
//            HttpSession session = request.getSession();
            String springSession = request.getHeader("Cookie");
            System.out.println(springSession);
//            HttpSession session = attributes.getRequest().getSession(false);
            template.header("Cookie", springSession);
        }
    }
}
