package com.thembu.guessza.location;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    private final  LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<Location> getRandomLocations(int count , UUID user_id) {
       return locationRepository.findRandomActiveLocations(count , user_id);

    }

    @Transactional(readOnly = true)
    public List<Location> getLocationByProvince( Province province, int count , UUID user_id) {
        return  locationRepository.findRandomLocationsByProvince( province.name() , count , user_id);
    }







}
