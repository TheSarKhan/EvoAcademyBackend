package az.evoacademy.backend.repository.content;

import az.evoacademy.backend.model.content.WhatYouLearn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhatYouLearnRepository extends JpaRepository<WhatYouLearn, Integer> {
WhatYouLearn findByEducationName(String educationName);
}
