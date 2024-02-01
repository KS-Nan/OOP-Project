package com.project.team1.Carbooking.model;

import jakarta.persistence.*;

@Entity
@Table(name="trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tripNumber;
    private String departureCity;
    private String destinationCity;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;

    // Default constructor (required by JPA)
    public Trip() {
    }

    public Trip(Long id, String tripNumber, String departureCity, String destinationCity, String departureTime, String arrivalTime, int availableSeats) {
        this.id = id;
        this.tripNumber = tripNumber;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String flightNumber) {
        this.tripNumber = flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
