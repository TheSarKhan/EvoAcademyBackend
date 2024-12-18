package az.evoacademy.backend.authentication.authorization;


import az.evoacademy.backend.authentication.dto.request.RegisterDTO;
import az.evoacademy.backend.authentication.dto.response.TokenResponse;
import az.evoacademy.backend.authentication.jwt.JwtUtil;
import az.evoacademy.backend.authentication.redis.RedisService;
import az.evoacademy.backend.dto.request.UserDTO;
import az.evoacademy.backend.model.user.User;
import az.evoacademy.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private  final RedisService redisService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public TokenResponse register(RegisterDTO request) {
        // E-posta kontrolü
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-posta zaten kayıtlı!");
        }

        LocalDateTime now = LocalDateTime.now();
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        // Kullanıcı oluşturma ve veritabanına kaydetme
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(request.getEmail(), null); // Claims kısmı null olabilir

        // Token'ı Redis'e kaydet
        redisService.saveTokenToRedis(accessToken, request.getEmail());
        String savedToken = redisService.getTokenFromRedis(request.getEmail()); // Al
        if (savedToken != null) {
            System.out.println("Redis'ten alınan token: " + savedToken);
        } else {
            System.out.println("Token Redis'ten alınamadı");
        }
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
