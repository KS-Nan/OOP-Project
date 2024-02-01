import java.sql.*;
import java.time.LocalDate;

public class CustomerInfo {

    private int id;
    private String customerName;
    private LocalDate bookingDate;
    private String customerContacts;
    private String bookingReference;
    private int tripId; // Reference to the associated trip
    private Connection connection;

    public CustomerInfo(String customerName, LocalDate bookingDate, String customerContacts,String tripNumber ,Connection connection) {
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.customerContacts = customerContacts;
        this.tripId = getTripIdFromDatabase(tripNumber, connection);
        this.connection = connection;

        generateBookingReference();

        try {
            // Insert booking data into the database
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO customerinfo (customer_name, booking_date, customer_contacts, booking_reference, tripid) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, this.customerName);
            preparedStatement.setDate(2, Date.valueOf(this.bookingDate));
            preparedStatement.setString(3, this.customerContacts);
            preparedStatement.setString(4, this.bookingReference);
            preparedStatement.setInt(5, this.tripId);
            preparedStatement.executeUpdate();

            // Assuming you want to capture a generated ID for the booking, not the customer
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Assuming this is a bookingId or similar
                this.id = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException(e);
            // Consider additional error handling
        }
    }

    public CustomerInfo(int id, String customerName, LocalDate bookingDate, String customerContacts, String bookingReference, int tripId, Connection connection) {
        this.id = id;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.customerContacts = customerContacts;
        this.bookingReference = bookingReference;
        this.tripId = tripId;
        this.connection = connection;
    }

    public CustomerInfo(String customerName, Date bookingDate, String customerContacts, String bookingReference, int tripId, Connection connection) {
    }

    private void generateBookingReference() {
        this.bookingReference = "TR" + System.currentTimeMillis();
    }

    private int getTripIdFromDatabase(String tripNumber, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM trips WHERE trip_number = ?"
            );
            preparedStatement.setString(1, tripNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return -1; // Return -1 if flight ID is not found (error condition)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void displayDetails() {
        System.out.println("Customer ID: " + id);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Customer Contacts: " + customerContacts);
        System.out.println("Booking Reference: " + bookingReference);
        System.out.println("Trip ID: " + tripId);
    }

    private void handleSQLException(SQLException e) {
        // Handle SQLException, e.g., log the error or display a user-friendly message
        e.printStackTrace();
    }
}
