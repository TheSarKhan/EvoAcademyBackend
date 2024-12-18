package az.evoacademy.backend.service.impl;

import az.evoacademy.backend.dto.request.FindWhatYouLearnRequest;
import az.evoacademy.backend.dto.request.WhatYouLearnRequest;
import az.evoacademy.backend.model.content.WhatYouLearn;
import az.evoacademy.backend.model.content.WhatYouLearnItem;
import az.evoacademy.backend.repository.content.WhatYouLearnItemRepository;
import az.evoacademy.backend.repository.content.WhatYouLearnRepository;
import az.evoacademy.backend.service.WhatYouLearnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhatYouLearnServiceImpl implements WhatYouLearnService {
    private final WhatYouLearnRepository whatYouLearnRepository;
    private final WhatYouLearnItemRepository whatYouLearnItemRepository;

    @Override
    public WhatYouLearn findWhatYouLearn(FindWhatYouLearnRequest request) {
      WhatYouLearn wantedEducation=whatYouLearnRepository.findByEducationName(request.getEducationName());
      return wantedEducation;

    }
          @Override
        public WhatYouLearn saveWYL(WhatYouLearnRequest request) {
            // Ana model oluştur
            WhatYouLearn whatYouLearn = new WhatYouLearn();
            whatYouLearn.setEducationName(request.getEducationName());

            // JSON item'ları oluştur ve ekle
            var items = request.getItems().stream().map(itemRequest -> {
                WhatYouLearnItem item = new WhatYouLearnItem();
                item.setHeader(itemRequest.getHeader());
                item.setDescription(itemRequest.getDescription());
                return item;
            }).collect(Collectors.toList());

            whatYouLearn.setItems(items);
whatYouLearnItemRepository.saveAll(items);
            // Kaydet ve geri döndür
            return whatYouLearnRepository.save(whatYouLearn);
        }
}
