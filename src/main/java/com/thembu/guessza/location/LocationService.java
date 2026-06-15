package com.thembu.guessza.location;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final  LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getRandomLocations(List<String> vistedLocations, int count) {
       return locationRepository.findRandomActiveLocations(vistedLocations,count);

    }



}
