import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookingSystem {

    private List<Trip> trips;
    private List<CustomerInfo> customerInfos;
    private Scanner scanner;
    private static Connection connection;

    public BookingSystem() {
        this.trips = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.customerInfos = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/carbooking";
            String username = "root";
            String password = "...........";  // Replace with your actual password
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        initializeTrips();
    }

    private void initializeTrips() {
        // Implement loading trips from the database if needed
    }

    public void addTrip(Trip trip) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO trips (trip_number, departure_city, destination_city, departure_time, arrival_time, available_seats) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, trip.getTripNumber());
            preparedStatement.setString(2, trip.getDepartureCity());
            preparedStatement.setString(3, trip.getDestinationCity());
            preparedStatement.setString(4, trip.getDepartureTime());
            preparedStatement.setString(5, trip.getArrivalTime());
            preparedStatement.setInt(6, trip.getAvailableSeats());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                trip.setId(generatedKeys.getInt(1));
            }

            trips.add(trip);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTrip(Trip trip) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM trips WHERE id = ?"
            );
            preparedStatement.setInt(1, trip.getId());
            preparedStatement.executeUpdate();

            trips.remove(trip);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchTrip(String tripNumber) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM trips WHERE trip_number = ?"
            );
            preparedStatement.setString(1, tripNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Trip trip = createTripFromResultSet(resultSet);
                trip.displayDetails();
            } else {
                System.out.println("Trip is not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayTripDetails() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM trips"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Trip trip = createTripFromResultSet(resultSet);
                trip.displayDetails();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeBooking() {
        System.out.println("Enter passenger details:");
        System.out.print("Name: ");
        String customerName = scanner.nextLine();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Booking date: ");
        LocalDate bookingDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Contact Number: ");
        String contactNumber = scanner.nextLine();


        System.out.print("Enter the trip number to make a booking: ");
        String tripNumber = scanner.nextLine();

        Trip selectedTrip = null;

        try {
            // Retrieve flight details from the database
            PreparedStatement selectTripStatement = connection.prepareStatement(
                    "SELECT * FROM trips WHERE trip_number = ? FOR UPDATE"
            );
            selectTripStatement.setString(1, tripNumber);
            ResultSet resultSet = selectTripStatement.executeQuery();

            if (resultSet.next()) {
                selectedTrip = createTripFromResultSet(resultSet);

                // Check if there are available seats
                if (selectedTrip.getAvailableSeats() > 0) {
                    // Update availableSeats in the database
                    PreparedStatement updateSeatsStatement = connection.prepareStatement(
                            "UPDATE trips SET available_seats = available_seats - 1 WHERE trip_number = ?"
                    );
                    updateSeatsStatement.setString(1, tripNumber);
                    updateSeatsStatement.executeUpdate();
                } else {
                    System.out.println("No available seats for the selected trip. Reservation failed.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (selectedTrip != null) {

            CustomerInfo newCustomerInfo = new CustomerInfo(customerName, bookingDate, contactNumber, tripNumber,  connection);
            selectedTrip.bookSeat(newCustomerInfo);
            customerInfos.add(newCustomerInfo);
            System.out.println("Reservation successful. Your booking reference: " + newCustomerInfo.getBookingReference());
        } else {
            System.out.println("Invalid trip number. Reservation failed.");
        }
    }


    public void cancelReservation() {
        System.out.println("Enter customer details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        CustomerInfo customerInfoToRemove = null;

        for (CustomerInfo customerInfo : customerInfos) {
            if (customerInfo.getCustomerName().equals(name)) {
                customerInfoToRemove = customerInfo;
                break;
            }
        }

        if (customerInfoToRemove != null) {
            try {
                // Remove passenger from the database
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM customerInfos WHERE id = ?"
                );
                preparedStatement.setInt(1, customerInfoToRemove.getId());
                preparedStatement.executeUpdate();

                customerInfos.remove(customerInfoToRemove);
                System.out.println("Reservation canceled successfully.");
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println("Failed to cancel reservation.");
            }
        } else {
            System.out.println("Passenger not found. Cancellation failed.");
        }
    }
    public void displayCustomerInfoById(int Id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM customerinfo WHERE customer_id = ?"
            );
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CustomerInfo customerInfo = createCustomerInfoFromResultSet(resultSet);
                customerInfo.displayDetails();
            } else {
                System.out.println("Customer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    // Other methods...

    private Trip findTripByNumber(String tripNumber) {
        for (Trip trip : trips) {
            if (trip.getTripNumber().equals(tripNumber)) {
                return trip;
            }
        }
        return null;
    }
    public static void deletePassenger(int ID) {
        String deleteCustomerinfoQuery = "DELETE FROM customerinfo WHERE customer_id = ?;";
        try (PreparedStatement deleteCustomerInfoStatement = connection.prepareStatement(deleteCustomerinfoQuery)) {
            deleteCustomerInfoStatement.setInt(1, ID);
            int rowsAffected = deleteCustomerInfoStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Passenger deleted successfully");
            } else {
                System.out.println("No passenger found with the specified ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static void deleteTrip(int id) {
        // First, delete associated customerinfo records
        String deleteCustomerInfoQuery = "DELETE FROM customerinfo WHERE trip_id = ?;";
        try (PreparedStatement deleteCustomerInfoStatement = connection.prepareStatement(deleteCustomerInfoQuery)) {
            deleteCustomerInfoStatement.setInt(1, id);
            deleteCustomerInfoStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Then, delete the trip
        String deleteTripQuery = "DELETE FROM trips WHERE id = ?;";
        try (PreparedStatement deleteTripStatement = connection.prepareStatement(deleteTripQuery)) {
            deleteTripStatement.setInt(1, id);
            int rowAffected = deleteTripStatement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Trip deleted successfully");
            } else {
                System.out.println("No Trip found!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTripByadmin() {

        System.out.println("Are you sure you want to add the Trip?");
        System.out.print("Enter 'Yes' to confirm, 'No' to cancel: ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Yes")) {

            System.out.println("Enter details for the new trip:");
            System.out.print("Trip Number: ");
            String tripNumber = scanner.nextLine();
            System.out.print("Departure City: ");
            String departureCity = scanner.nextLine();
            System.out.print("Destination City: ");
            String destinationCity = scanner.nextLine();
            System.out.print("Departure Time: ");
            String departureTime = scanner.nextLine();
            System.out.print("Arrival Time: ");
            String arrivalTime = scanner.nextLine();
            System.out.print("Available Seats: ");
            int availableSeats = scanner.nextInt();

            scanner.nextLine(); // Consume the newline character
            System.out.println("Trip Number: " + tripNumber);
            System.out.println("Departure City: " + departureCity);
            System.out.println("Destination City: " + destinationCity);
            System.out.println("Departure Time: " + departureTime);
            System.out.println("Arrival Time: " + arrivalTime);
            System.out.println("Available Seats: " + availableSeats);
            Trip newTrip = new Trip(tripNumber, departureCity, destinationCity, departureTime, arrivalTime, availableSeats,connection);
            addTrip(newTrip);
            System.out.println("New trip added successfully.");
        } else {
            System.out.println("Trip addition canceled.");
        }

    }


    private Trip createTripFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String tripNumber = resultSet.getString("trip_number");
        String departureCity = resultSet.getString("departure_city");
        String destinationCity = resultSet.getString("destination_city");
        String departureTime = resultSet.getString("departure_time");
        String arrivalTime = resultSet.getString("arrival_time");
        int availableSeats = resultSet.getInt("available_seats");

        return new Trip(tripNumber, departureCity, destinationCity, departureTime, arrivalTime, availableSeats, connection);
    }

    private CustomerInfo createCustomerInfoFromResultSet(ResultSet resultSet) throws SQLException {

        String customerName = resultSet.getString("customer_name");
        Date bookingDate = resultSet.getDate("booking_date");
        String customerContacts = resultSet.getString("customer_contacts");
        String bookingReference = resultSet.getString("booking_reference");
        int tripId = resultSet.getInt("tripid");

        // Retrieve the associated Trip from the database
        if (isTrIdValid(tripId)) {
            return new CustomerInfo(customerName, bookingDate, customerContacts, bookingReference, tripId, connection);
        } else {
            // Handle the case where the flightId is not valid
            System.out.println("Invalid flightId for passenger: " + customerName);
            return null;
        }


    }
    private boolean isTrIdValid(int tripId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM trips WHERE id = ?"
            );
            preparedStatement.setInt(1, tripId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // true if the flightId exists in the flights table
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }



    // Add this method to your BookingSystem class to retrieve a Trip by ID
    private Trip getTripById(int tripId) {
        for (Trip trip : trips) {
            if (trip.getId() == tripId) {
                return trip;
            }
        }
        return null; // Handle the case where the trip is not found
    }


    private void handleSQLException(SQLException e) {
        e.printStackTrace(); // Add appropriate error handling
    }
}

