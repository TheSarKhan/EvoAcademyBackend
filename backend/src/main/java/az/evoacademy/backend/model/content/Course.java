package az.evoacademy.backend.model.content;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
     Long id;
     String name;
     String description;
}
