package com.thembu.guessza.clientconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "guessza.maps") //allows us to map external properties from application.properies into type safe java objects
public record MapsProperties(
        String apiKey
) {

    public MapsProperties {

        // add null check
        if(apiKey == null || apiKey.isBlank() ) {
            throw  new IllegalArgumentException("Api key configuration is missing");
        }


    }

    //override toString method so we dont accidentally print out api key in code
    @Override
    public String toString() {
        return "ApiKey[PROTECTED]";
    }



}
