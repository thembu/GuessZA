package com.thembu.guessza.location;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    @Query(value = "SELECT * FROM locations WHERE id NOT IN ( SELECT location_id FROM visited_locations WHERE user_id = :user_id) AND  active = true   ORDER BY random() LIMIT :count", nativeQuery = true)
    List<Location> findRandomActiveLocations(@Param("count") int count , @Param("user_id") UUID user_id);

    @Query(value = "SELECT * FROM locations WHERE id NOT IN ( SELECT location_id FROM visited_locations WHERE user_id = :user_id) AND  active = true AND province ILIKE :province ORDER BY random() LIMIT :count", nativeQuery = true)
    List<Location> findRandomLocationsByProvince(@Param("province") String province,@Param("count") int count , @Param("user_id") UUID user_id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO visited_locations (location_id , user_id) VALUES (:location_id, :user_id ) ON CONFLICT (location_id, user_id) DO NOTHING" , nativeQuery = true)
    void saveVisited(@Param("location_id") UUID location_id , @Param("user_id") UUID user_id);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM visited_locations WHERE user_id = :user_id", nativeQuery = true)
    void deleteVisited(@Param("user_id") UUID user_id);

}
