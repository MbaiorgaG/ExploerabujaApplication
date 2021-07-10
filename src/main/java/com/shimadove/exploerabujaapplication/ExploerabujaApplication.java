package com.shimadove.exploerabujaapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimadove.exploerabujaapplication.domain.Difficulty;
import com.shimadove.exploerabujaapplication.domain.Region;
import com.shimadove.exploerabujaapplication.domain.Tour;
import com.shimadove.exploerabujaapplication.service.TourPackageService;
import com.shimadove.exploerabujaapplication.service.TourService;
import org.hibernate.type.Type;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

@SpringBootApplication
public class ExploerabujaApplication  implements CommandLineRunner {

    @Value("${explore.importJson}")
    private String importFile;

    @Autowired
    private TourService tourService;
    @Autowired
    private TourPackageService tourPackageService;

    public static void main(String[] args) {
        SpringApplication.run(ExploerabujaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createTourPackages();
        long numOfTourPackages = tourPackageService.total();
        createTours(importFile);
        long numOfTours = tourService.total();
    }

    private void createTours(String fileToImport) throws IOException{
        TourFromFile.read(fileToImport).forEach(importedTour ->
                tourService.createTour(importedTour.getTitle(),
                        importedTour.getDescription(),
                        importedTour.getBlurb(),
                        importedTour.getPrice(),
                        importedTour.getLength(),
                        importedTour.getBullets(),
                        importedTour.getKeywords(),
                        importedTour.getPackageType(),
                        importedTour.getDifficulty(),
                        importedTour.getRegion()));
    }

    private void createTourPackages() {
        tourPackageService.createTourPackage("BC", "Backpack Cal");
        tourPackageService.createTourPackage("CC", "California Calm");
        tourPackageService.createTourPackage("CH", "California Hot springs");
        tourPackageService.createTourPackage("CY", "Cycle California");
        tourPackageService.createTourPackage("DS", "From Desert to Sea");
        tourPackageService.createTourPackage("KC", "Kids California");
        tourPackageService.createTourPackage("NW", "Nature Watch");
        tourPackageService.createTourPackage("SC", "Snowboard Cali");
        tourPackageService.createTourPackage("TC", "Taste of California");
    }



    private static class  TourFromFile{
        //Fields
        private String  packageType, title, description, blurb, price, length,
                bullets, keywords, difficulty, region;

        static List<TourFromFile> read (String fileToImport)throws IOException{
            return  new ObjectMapper().setVisibility(FIELD, ANY).
                    readValue(new FileInputStream(fileToImport),new TypeReference<List<TourFromFile>>() {});
        }
        protected TourFromFile (){}

        String getPackageType(){return packageType;}
        String getTitle(){return title;}
        String getDescription() { return description; }

        String getBlurb() { return blurb; }

        Integer getPrice() { return Integer.parseInt(price); }

        String getLength() { return length; }

        String getBullets() { return bullets; }

        String getKeywords() { return keywords; }

        Difficulty getDifficulty() { return Difficulty.valueOf(difficulty); }

        Region getRegion() { return Region.findByLable(region); }
    }

}
