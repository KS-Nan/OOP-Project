import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Trip {
    private int id;
    private String tripNumber;
    private String departureCity;
    private String destinationCity;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;
    private Connection connection;

    public Trip(String tripNumber, String departureCity, String destinationCity, String departureTime, String arrivalTime, int availableSeats, Connection connection) {
        this.tripNumber = tripNumber;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.connection = connection;
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void bookSeat(CustomerInfo customerInfo) {
        try {
            if (availableSeats > 0) {
                customerInfo.setBookingReference(tripNumber + customerInfo.getCustomerName().hashCode());
                availableSeats--;

                // Update the database to reflect the booked seat
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE trips SET available_seats = ? WHERE id = ?"
                );
                preparedStatement.setInt(1, availableSeats);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();

                System.out.println("Reservation successful. Your booking reference: " + customerInfo.getBookingReference());
            } else {
                System.out.println("Sorry, no available seats on this trip.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayDetails() {
        System.out.println("\n===================================");
        System.out.println("           Trip Details");
        System.out.println("===================================");
        System.out.printf("Trip Number:       %s\n", tripNumber);
        System.out.printf("Departure City:    %s\n", departureCity);
        System.out.printf("Destination City:  %s\n", destinationCity);
        System.out.printf("Departure Time:    %s\n", departureTime);
        System.out.printf("Arrival Time:      %s\n", arrivalTime);
        System.out.printf("Available Seats:   %d\n", availableSeats);
        System.out.println("-----------------------------------");
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace(); // Add appropriate error handling
    }
}
