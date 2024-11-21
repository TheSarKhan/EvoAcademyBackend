package az.evoacademy.backend.controller;

import az.evoacademy.backend.model.content.Course;
import az.evoacademy.backend.repository.content.CourseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController (CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
