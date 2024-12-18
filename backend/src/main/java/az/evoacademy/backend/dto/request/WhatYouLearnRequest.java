package az.evoacademy.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WhatYouLearnRequest {
    String educationName;
    @JdbcTypeCode(SqlTypes.JSON)
    List<WhatYouLearnItemRequest> items;
}
