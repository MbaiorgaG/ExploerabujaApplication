package com.shimadove.exploerabujaapplication.service;

import com.shimadove.exploerabujaapplication.domain.Difficulty;
import com.shimadove.exploerabujaapplication.domain.Region;
import com.shimadove.exploerabujaapplication.domain.Tour;
import com.shimadove.exploerabujaapplication.domain.TourPackage;
import com.shimadove.exploerabujaapplication.repo.TourPackageRepository;
import com.shimadove.exploerabujaapplication.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService {
    TourRepository tourRepository;
    TourPackageRepository tourPackageRepository;

    @Autowired
    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
        this.tourRepository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     * Create a new tour object and persist it to the database
     * @param title
     * @param description
     * @param blurb
     * @param price
     * @param duration
     * @param bullets
     * @param keywords
     * @param tourPackageName
     * @param difficulty
     * @param region
     * @return Tour Entity
     */
    public Tour createTour(String title, String description, String blurb, Integer price, String duration,
                           String bullets, String keywords, String tourPackageName, Difficulty difficulty,
                           Region region){

        //Create a tour package from the passed tour package name
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                .orElseThrow(()-> new RuntimeException("Tour package dose not exist "+ tourPackageName));

        return tourRepository.save(new Tour(title,description,blurb,price,duration,bullets,
                keywords,tourPackage,difficulty,region));

    }

    /**
     * Calculate the total number of tours
     * @return
     */
    public long total(){
        return tourRepository.count();
    }
}
