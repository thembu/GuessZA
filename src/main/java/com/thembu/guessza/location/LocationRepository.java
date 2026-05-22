package com.thembu.guessza.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

@Query(value = "select  * from locations where active=true order by random() limit :count" , nativeQuery = true)
List<Location> findRandomActiveLocations(@Param("count") int count);



}
