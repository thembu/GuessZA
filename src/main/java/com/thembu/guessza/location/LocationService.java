package com.thembu.guessza.location;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final  LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<Location> getRandomLocations(int count) {
       return locationRepository.findRandomActiveLocations(count);

    }

    @Transactional(readOnly = true)
    public List<Location> getLocationByProvince( String province, int count) {
        return  locationRepository.findRandomLocationsByProvince( province , count);
    }







}
