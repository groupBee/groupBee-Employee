//package groupbee.employee.filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class SessionIdFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String sessionId = null;
//
//        // Cookie 헤더에서 SESSION 값을 추출
//        String cookieHeader = request.getHeader("Cookie");
//        if (cookieHeader != null) {
//            for (String cookie : cookieHeader.split(";")) {
//                cookie = cookie.trim();
//                if (cookie.startsWith("SESSION=")) {
//                    sessionId = cookie.substring("SESSION=".length());
//                    break;
//                }
//            }
//        }
//
//        // 세션 ID가 존재하면 이를 통해 HttpServletRequest의 세션을 설정
//        if (sessionId != null) {
//            request.getSession().setAttribute("SPRING_SESSION_ID", sessionId);
//            // 여기에서 세션 ID를 사용하여 추가적인 로직을 구현할 수 있습니다.
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
