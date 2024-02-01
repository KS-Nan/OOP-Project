package com.project.team1.Carbooking.Data;

import com.project.team1.Carbooking.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip>findById(int id);
    List<Trip> findByTripNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(String search, String search1);


}
