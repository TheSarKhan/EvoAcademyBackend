package az.evoacademy.backend.repository.content;

import az.evoacademy.backend.model.content.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
