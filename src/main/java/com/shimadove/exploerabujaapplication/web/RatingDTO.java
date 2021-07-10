package com.shimadove.exploerabujaapplication.web;

import com.shimadove.exploerabujaapplication.domain.TourRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatingDTO {
    @Min(0)
    @Max(5)
    private Integer score;
    @Size(max=255)
    private String comment;
    @NotNull
    private Integer customerId;

    /**
     * Construct a RatingDto from a fully instantiated TourRating.
     *
     * @param tourRating Tour Rating Object
     */
    public RatingDTO(TourRating tourRating) {
        this(tourRating.getScore(), tourRating.getComment(),tourRating.getPk().getCustomerId());
    }
}
