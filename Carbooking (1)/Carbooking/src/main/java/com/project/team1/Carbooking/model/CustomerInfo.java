package com.project.team1.Carbooking.model;

import jakarta.persistence.*;

@Entity
@Table(name="customerinfo")
public class CustomerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "customer_name")
    private String name;

    private String bookingDate;
    @Column(name = "customer_contacts")
    private String contactNumber;
    private String bookingReference;
    @ManyToOne
    @JoinColumn(name = "tripid", insertable = false, updatable = false)
    private Trip trip;

    private int tripid;



    public CustomerInfo(int id, String name, String bookingDate, String contactNumber, String bookingReference, int tripid) {
        this.id = id;
        this.name = name;
        this.bookingDate=bookingDate;
        this.contactNumber = contactNumber;
        this.bookingReference = bookingReference;
        this.tripid = tripid;
    }

    public CustomerInfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getTripid() {
        return tripid;
    }

    public void setTripid(int tripid) {
        this.tripid = tripid;
    }
    public void bookTrip(CustomerInfo customerInfos) {
    }
    private void generateBookingReference() {
        // Implement your logic to generate a unique booking reference (e.g., using timestamps or random strings)
        this.bookingReference = "TR" + System.currentTimeMillis();
    }
}
