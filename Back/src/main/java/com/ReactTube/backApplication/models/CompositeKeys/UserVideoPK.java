package com.ReactTube.backApplication.models.CompositeKeys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVideoPK {
    private long userId;
    private Long videoId;
    private Date time;
}
