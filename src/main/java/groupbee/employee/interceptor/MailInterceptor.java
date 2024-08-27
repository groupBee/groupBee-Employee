package groupbee.employee.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;


public class MailInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("X-API-Key","E63DEF-77C26B-9A104F-C6AEBF-B2ED8D");
    }
}

