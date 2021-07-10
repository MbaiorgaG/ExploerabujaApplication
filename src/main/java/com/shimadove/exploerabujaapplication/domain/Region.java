package com.shimadove.exploerabujaapplication.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Region {
    Central_Coast("Central Coast"),Southern_Callifornia("Southern Callifornia");
    private String lable;

    public static Region findByLable(String lable){
        for (Region region:Region.values()){
            if (region.lable.equalsIgnoreCase(lable))
                return region;
        }
        return null;
    }
}
