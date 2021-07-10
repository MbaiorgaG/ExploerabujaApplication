package com.shimadove.exploerabujaapplication.repo;

import com.shimadove.exploerabujaapplication.domain.TourRating;
import com.shimadove.exploerabujaapplication.domain.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends CrudRepository<TourRating, String> {
    /**
     *
     * @param tourId
     * @return
     */
    List<TourRating> findByTourId(String tourId);

    /**
     *
     * @param tourId
     * @param customerId
     * @return
     */
    Optional<TourRating> findByTourIdAndCustomerId(String tourId, Integer customerId);

    /**
     *
     * @param tourId
     * @param pageable
     * @return
     */
    Page<TourRating> findByTourId(String tourId, Pageable pageable);
}
