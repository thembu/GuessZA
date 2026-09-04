package com.thembu.guessza.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    @Query(value = "SELECT * FROM locations WHERE active = true AND (:#{#visitedLocations == null || #visitedLocations.isEmpty()} = true OR name NOT IN (:visitedLocations)) ORDER BY random() LIMIT :count", nativeQuery = true)
    List<Location> findRandomActiveLocations(@Param("visitedLocations") List<String> excludedLocations,  @Param("count") int count);

    @Query(value = "SELECT * FROM locations WHERE active = true AND (:#{#visitedLocations == null || #visitedLocations.isEmpty()} = true OR name NOT IN (:visitedLocations)) AND province ILIKE :province ORDER BY random() LIMIT :count", nativeQuery = true)
List<Location> findRandomLocationsByProvince(@Param("visitedLocations") List<String> excludedLocations,  @Param("province") String province,@Param("count") int count);


}
