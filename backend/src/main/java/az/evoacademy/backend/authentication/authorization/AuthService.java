package az.evoacademy.backend.authentication.authorization;


import az.evoacademy.backend.authentication.dto.request.RegisterDTO;
import az.evoacademy.backend.authentication.dto.response.TokenResponse;
import az.evoacademy.backend.authentication.jwt.JwtUtil;
import az.evoacademy.backend.dto.request.UserDTO;
import az.evoacademy.backend.model.user.User;
import az.evoacademy.backend.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

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

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
