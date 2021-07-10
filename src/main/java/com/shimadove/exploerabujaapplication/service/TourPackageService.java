package com.shimadove.exploerabujaapplication.service;

import com.shimadove.exploerabujaapplication.domain.TourPackage;
import com.shimadove.exploerabujaapplication.repo.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourPackageService {
    private TourPackageRepository tourPackageRepository;

    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
    *Create a tour package
    * @param code code of the package
    * @param name name of the package
    *
    * @return new or existing tour package
     */
    public TourPackage createTourPackage(String code, String name){
        return tourPackageRepository.findById(code)
                .orElse(tourPackageRepository.save(new TourPackage(code,name)));
    }


    /**
     * Look up all the tours in abuja
     * @return all the tours in abuja
     */
    public Iterable<TourPackage> lookUp(){return tourPackageRepository.findAll();}


    /**
     *
     * @return
     */
    public long total(){return tourPackageRepository.count();}
}
