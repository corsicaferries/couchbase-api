package com.neptune2.couchbase_api.repository;

import com.neptune2.couchbase_api.model.Travel;

import java.util.List;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends CouchbaseRepository<Travel, String> {
   

    // 2️⃣ Trouver tous les voyages d’un navire spécifique
    @Query("#{#n1ql.selectEntity} WHERE ship.name = $1")
    List<Travel> findByShipName(String name);

    // 3️⃣ Trouver tous les voyages d’un port de départ donné
    @Query("#{#n1ql.selectEntity} WHERE line.departurePortId = $1")
    List<Travel> findByDeparturePort(String departurePortId);
}
