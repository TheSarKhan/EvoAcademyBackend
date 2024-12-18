package az.evoacademy.backend.authentication.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOKEN_PREFIX = "token:";

    // Token'ı Redis'e kaydetme
    public void saveTokenToRedis(String token, String email) {
        redisTemplate.opsForValue().set(TOKEN_PREFIX + email, token);
    }

    // Token'ı Redis'ten alma
    public String getTokenFromRedis(String email) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + email);
    }

    // Token'ı Redis'ten silme
    public void deleteTokenFromRedis(String email) {
        redisTemplate.delete(TOKEN_PREFIX + email);
    }
}
