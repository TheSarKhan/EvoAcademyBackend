package az.evoacademy.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE )
public class UserDTO {
      String username;
      String email;
      String password;
      String confirmPassword;
      Boolean acceptTermsOfUse;
}

