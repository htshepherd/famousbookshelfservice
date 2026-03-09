package com.famousbookshelf.dto;

import com.famousbookshelf.entity.Recommendation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminRecommendationVO extends Recommendation {
    private String bookName;
    private String authorName;
    private String celebrityName;
}
