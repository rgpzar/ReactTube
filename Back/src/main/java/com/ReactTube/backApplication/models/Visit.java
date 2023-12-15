package com.ReactTube.backApplication.models;

import com.ReactTube.backApplication.models.CompositeKeys.UserVideoPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    @EmbeddedId
    private UserVideoPK id;

}
