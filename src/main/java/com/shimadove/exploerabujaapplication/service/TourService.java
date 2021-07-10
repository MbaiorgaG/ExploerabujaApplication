package com.shimadove.exploerabujaapplication.service;

import com.shimadove.exploerabujaapplication.domain.Tour;
import com.shimadove.exploerabujaapplication.domain.TourPackage;
import com.shimadove.exploerabujaapplication.repo.TourPackageRepository;
import com.shimadove.exploerabujaapplication.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
     *
     * @param title
     * @param tourPackageName
     * @param details
     * @return
     */
    public Tour createTour(String title, String tourPackageName, Map<String, String> details){

        //Create a tour package from the passed tour package name
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                .orElseThrow(()-> new RuntimeException("Tour package dose not exist "+ tourPackageName));

        return tourRepository.save(new Tour(title, tourPackage,details));
    }

    /**
     * Calculate the total number of tours
     * @return
     */
    public long total(){
        return tourRepository.count();
    }
}
