package az.evoacademy.backend.authentication.authorization;


import az.evoacademy.backend.authentication.dto.request.RegisterDTO;
import az.evoacademy.backend.authentication.dto.response.TokenResponse;
import az.evoacademy.backend.authentication.jwt.JwtUtil;
import az.evoacademy.backend.authentication.dto.request.LoginDTO;
import az.evoacademy.backend.authentication.redis.RedisService;
import az.evoacademy.backend.dto.request.UserDTO;
import az.evoacademy.backend.model.user.User;
import az.evoacademy.backend.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
private  final RedisService redisService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginDTO request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with email: " + request.getEmail());
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), null);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterDTO request) {
        return authService.register(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken, jwtUtil.extractUsername(refreshToken))) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
        }

        String email = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email, null);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("Authorization") String token) {
        try {
            // "Bearer " kısmını çıkar
            token = token.substring(7);

            // Token'ı Redis'ten al
            String email = jwtUtil.extractUsername(token);
            String savedToken = redisService.getTokenFromRedis(email);

            // Token geçerliliğini kontrol et
            if (savedToken == null || !jwtUtil.isTokenValid(token, email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz veya süresi dolmuş token!");
            }

            // Token geçerli ise yanıt ver
            return ResponseEntity.ok("Auth endpoint is working!");

        } catch (Exception e) {
            // Hata durumunda yanıt ver
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token doğrulama hatası: " + e.getMessage());
        }
    }


}
