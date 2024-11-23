package az.evoacademy.backend.authentication.authorization;


import az.evoacademy.backend.authentication.dto.request.RegisterDTO;
import az.evoacademy.backend.authentication.dto.response.TokenResponse;
import az.evoacademy.backend.authentication.jwt.JwtUtil;
import az.evoacademy.backend.authentication.dto.request.LoginDTO;
import az.evoacademy.backend.dto.request.UserDTO;
import az.evoacademy.backend.model.user.User;
import az.evoacademy.backend.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AuthorizationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                                   UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

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
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }
}
