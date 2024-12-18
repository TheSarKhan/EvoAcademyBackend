package az.evoacademy.backend.model.content;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table
public class WhatYouLearn{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String educationName;
    @JdbcTypeCode(SqlTypes.JSON)
    List<WhatYouLearnItem> items;

}
