package az.evoacademy.backend.model.content;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table
@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WhatYouLearnItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String header;
    String description;
}
