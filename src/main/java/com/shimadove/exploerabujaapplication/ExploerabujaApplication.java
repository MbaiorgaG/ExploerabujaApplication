package com.shimadove.exploerabujaapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimadove.exploerabujaapplication.service.TourPackageService;
import com.shimadove.exploerabujaapplication.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//        TourFromFile.read(fileToImport).forEach(importedTour ->
//                tourService.createTour(importedTour.getTitle(),
//                        importedTour.getDescription(),
//                        importedTour.getBlurb(),
//                        importedTour.getPrice(),
//                        importedTour.getLength(),
//                        importedTour.getBullets(),
//                        importedTour.getKeywords(),
//                        importedTour.getPackageType(),
//                        importedTour.getDifficulty(),
//                        importedTour.getRegion()));
        TourFromFile.read(fileToImport).forEach(tourFromFile ->
                tourService.createTour(tourFromFile.getTitle(),
                        tourFromFile.getPackageName(),
                        tourFromFile.getDetails()));
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
        private String  title, packageName;
        private Map<String, String> details;

        TourFromFile(Map<String, String> record){
            this.title = record.get("title");
            this.packageName = record.get("packageType");
            this.details = record;
            this.details.remove("packageType");
            this.details.remove("title");
        }

        static List<TourFromFile> read (String fileToImport)throws IOException{
//            return  new ObjectMapper().setVisibility(FIELD, ANY).
//                    readValue(new FileInputStream(fileToImport),new TypeReference<List<TourFromFile>>() {});
            List<Map<String, String>> records = new ObjectMapper().setVisibility(FIELD, ANY).
                    readValue(new FileInputStream(fileToImport),
                            new TypeReference<List<Map<String, String>>>() {
                            });
            return records.stream().map(TourFromFile::new)
                    .collect(Collectors.toList());
        }
        protected TourFromFile (){}

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public void setDetails(Map<String, String> details) {
            this.details = details;
        }
    }

}
