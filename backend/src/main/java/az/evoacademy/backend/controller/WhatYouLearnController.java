package az.evoacademy.backend.controller;

import az.evoacademy.backend.dto.request.FindWhatYouLearnRequest;
import az.evoacademy.backend.dto.request.WhatYouLearnRequest;
import az.evoacademy.backend.model.content.WhatYouLearn;
import az.evoacademy.backend.repository.content.WhatYouLearnRepository;
import az.evoacademy.backend.service.WhatYouLearnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/whatYouLearn")
@RequiredArgsConstructor
public class WhatYouLearnController {

    private final WhatYouLearnService whatYouLearnService;
    private final WhatYouLearnRepository whatYouLearnRepository;

    @GetMapping
    public ResponseEntity<?> whatYouLearn(@RequestBody FindWhatYouLearnRequest findWhatYouLearnRequest) {
        WhatYouLearn whatYouLearn = whatYouLearnRepository.findByEducationName(findWhatYouLearnRequest.getEducationName());
    return ResponseEntity.ok(whatYouLearn);
    }
    @PostMapping
    public  ResponseEntity<?>addWhatYouLearn(@RequestBody WhatYouLearnRequest request) {
       List<WhatYouLearn> exists= whatYouLearnRepository.findAll();
        for (int i = 0; i <exists.size() ; i++) {
            if (exists.get(i).getEducationName().equals(request.getEducationName())) {
                return ResponseEntity.status(400).body("Elde bele bir kurs var");
            }
        }
        whatYouLearnService.saveWYL(request);

        return ResponseEntity.ok(request);
    }
}
