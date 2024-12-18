package az.evoacademy.backend.service;

import az.evoacademy.backend.dto.request.FindWhatYouLearnRequest;
import az.evoacademy.backend.dto.request.WhatYouLearnRequest;
import az.evoacademy.backend.model.content.WhatYouLearn;

public interface WhatYouLearnService {
    WhatYouLearn findWhatYouLearn(FindWhatYouLearnRequest request);
    WhatYouLearn saveWYL(WhatYouLearnRequest whatYouLearnRequest);
}
