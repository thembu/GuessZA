package com.thembu.guessza.location;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final  LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getRandomLocations(List<String> visitedLocations, int count) {
       return locationRepository.findRandomActiveLocations(visitedLocations,count);

    }

    public List<Location> getLocationByProvince(List<String> visitedLocations , String province, int count) {
        return  locationRepository.findRandomLocationsByProvince(visitedLocations , province , count);
    }



}
