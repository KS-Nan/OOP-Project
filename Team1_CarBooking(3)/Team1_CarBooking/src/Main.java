import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        BookingSystem bookingSystem = new BookingSystem();

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {
            System.out.println("\n**** Car Booking System ****");
            System.out.println("1. Display Available Trips");
            System.out.println("2. Search Trip");
            System.out.println("3. Make Reservation");
            System.out.println("4. Display Passenger Details");
            System.out.println("5. Cancel Reservation");
            System.out.println("\n**** Booking System for admin ****");
            System.out.println("6. Delete Passenger");
            System.out.println("7. Delete Trip");
            System.out.println("8. Add Trip by admin");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    bookingSystem.displayTripDetails();
                    break;
                case 2:
                    System.out.print("Enter Trip Number: ");
                    String tripNumber = scanner.nextLine();
                    bookingSystem.searchTrip(tripNumber);
                    break;
                case 3:
                    System.out.print("Enter Trip Number for Reservation: ");
//                    String tripNumberForBooking = scanner.nextLine();
                    bookingSystem.makeBooking();
                    break;
                case 4:
                    System.out.print("Enter Customer Name: ");
                    String customerId    = scanner.nextLine();
                    bookingSystem.displayCustomerInfoById(Integer.parseInt(customerId));
                    break;
                case 5:
                    bookingSystem.cancelReservation();
                    break;
                case 6:
                    System.out.print("Enter Customer ID: ");
                    int passengerId = Integer.parseInt(scanner.nextLine());
                    BookingSystem.deletePassenger(passengerId);
                    break;
                case 7:
                    System.out.print("Enter Trip ID: ");
                    int tripId = Integer.parseInt(scanner.nextLine());
                    BookingSystem.deleteTrip(tripId);
                    break;
                case 8:
                    bookingSystem.addTripByadmin();
                    break;
                case 9:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Exiting the Reservation System. Thank you!");
    }
}