package groupbee.employee.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    // 데이터 추가
    public void setValue(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    // 키 가져오기
    public Object getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }



}
