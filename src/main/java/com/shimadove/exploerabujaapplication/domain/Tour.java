package com.shimadove.exploerabujaapplication.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Tour {
    @Id
    private String id;

    @Indexed
    private String title;

    @Indexed
    private String tourPackagedCode;

    private String tourPackageName;

    private Map<String, String> details;

    public Tour(String title, TourPackage tourPackage, Map<String, String> details) {
        this.title = title;
        this.tourPackageName = tourPackage.getName();
        this.tourPackagedCode = tourPackage.getCode();
        this.details = details;
    }
}
