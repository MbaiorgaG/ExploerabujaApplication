package com.shimadove.exploerabujaapplication.web;

import com.shimadove.exploerabujaapplication.domain.Tour;
import com.shimadove.exploerabujaapplication.domain.TourRating;
import com.shimadove.exploerabujaapplication.domain.TourRatingPk;
import com.shimadove.exploerabujaapplication.repo.TourRatingRepository;
import com.shimadove.exploerabujaapplication.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
    TourRatingRepository ratingRepository;
    TourRepository repository;

    @Autowired
    public TourRatingController(TourRatingRepository ratingRepository, TourRepository repository) {
        this.ratingRepository = ratingRepository;
        this.repository = repository;
    }

    /**
     * @param tourId
     * @param ratingDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId,
                                 @RequestBody @Validated RatingDTO ratingDto) {
        Tour tour = verifyTour(tourId);
        ratingRepository.save(new TourRating(new TourRatingPk(tour, ratingDto.getCustomerId()),
                ratingDto.getScore(), ratingDto.getComment()));
    }

    /**
     * @param tourId
     * @return
     * @throws NoSuchElementException
     */
    private Tour verifyTour(int tourId) throws NoSuchElementException {
        return repository.findById(tourId).orElseThrow(() -> new NoSuchElementException(
                "Tour " + tourId + " dose not exist"
        ));
    }

    /**
     * @param tourId
     * @return
     */
    @GetMapping
    public List<RatingDTO> getAllRatingForTours(@PathVariable(value = "tourId") int tourId) {
        verifyTour(tourId);
        return ratingRepository.findByPkTourId(tourId).stream()
                .map(RatingDTO::new).collect(Collectors.toList());
    }

    @GetMapping(path = "/ratings")
    public Page<RatingDTO> getAllRatingForTours(@PathVariable(value = "tourId") int tourId,
                                                Pageable pageable) {
        verifyTour(tourId);
        Page<TourRating> tourRatings = ratingRepository.findByPkTourId(tourId,pageable);
        return new PageImpl<>(
                tourRatings.get().map(RatingDTO::new).collect(Collectors.toList()),
                pageable,
                tourRatings.getTotalElements()
        );
    }

    /**
     * @param tourId
     * @return
     */
    @GetMapping(path = "/average")
    public Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {
        verifyTour(tourId);
        return Map.of("Average", ratingRepository.findByPkTourId(tourId).stream()
                .mapToInt(TourRating::getScore).average()
                .orElseThrow(() -> new NoSuchElementException("This Tour has not rating")));
    }

    /**
     *
     * @param tourId
     * @param ratingDTO
     * @return
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDTO updateWithPut(@PathVariable(value = "tourId")int tourId,
                                   @RequestBody @Validated RatingDTO ratingDTO){
        TourRating rating = verifyTourRating(tourId,ratingDTO.getCustomerId());
        rating.setScore(ratingDTO.getScore());
        rating.setComment(ratingDTO.getComment());
        return new RatingDTO(ratingRepository.save(rating));
    }

    /**
     *
     * @param tourId
     * @param ratingDTO
     * @return
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDTO updateWithPatch(@PathVariable(value = "tourId")int tourId,
                                     @RequestBody @Validated RatingDTO ratingDTO){
        TourRating rating = verifyTourRating(tourId,ratingDTO.getCustomerId());
        if(ratingDTO.getScore() != null){
            rating.setScore(ratingDTO.getScore());
        }

        if(ratingDTO.getComment() != null){
            rating.setComment(ratingDTO.getComment());
        }

        return new RatingDTO(ratingRepository.save(rating));
    }

    /**
     *
     * @param tourId
     * @param customerId
     */
    @DeleteMapping(path = "/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value = "tourId") int tourId,
                       @PathVariable(value = "customerId")int customerId){
        TourRating tourRating = verifyTourRating(tourId, customerId);
        ratingRepository.delete(tourRating);
    }


    /**
     * Exception handler if no such exception is thrown in this class
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException exception) {
        return exception.getMessage();
    }

    public TourRating verifyTourRating(int tourId,
                                       int customerId) {
        return ratingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId)
                .orElseThrow(() -> new NoSuchElementException("Tour-rating pair for request{" +
                        tourId + "for customer " + customerId + "}"));
    }


}