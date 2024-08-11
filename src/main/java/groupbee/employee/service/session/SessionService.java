package groupbee.employee.service.session;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private final HashOperations<String, String, Object> hashOperations;

    public String getSessionId(String key) {
        logger.debug("Fetching session ID for key: {}", key);

        String filedName = "sessionAttr:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME";
        Object sessionValue = hashOperations.get(key, filedName);

        if (sessionValue != null) {
            logger.debug("Session value found: {}", sessionValue);
            return sessionValue.toString();
        } else {
            logger.warn("No session value found for key: {}", key);
            return null;
        }
    }
}
