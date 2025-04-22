//AirwaysReservationSystem
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private String date;
    private String time;
    private String duration;
    private double baseFareEconomy;
    private double baseFarePremiumEconomy;
    private double baseFareBusiness;
    private double baseFareFirstClass;
    private Map<String, Integer> classSeats;

    public Flight(String flightNumber, String origin, String destination, String date, String time, String duration,
                  int economySeats, int premiumEconomySeats, int businessSeats, int firstClassSeats,
                  double baseFareEconomy, double baseFarePremiumEconomy, double baseFareBusiness, double baseFareFirstClass) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.baseFareEconomy = baseFareEconomy;
        this.baseFarePremiumEconomy = baseFarePremiumEconomy;
        this.baseFareBusiness = baseFareBusiness;
        this.baseFareFirstClass = baseFareFirstClass;

        classSeats = new HashMap<>();
        classSeats.put("Economy", economySeats);
        classSeats.put("Premium Economy", premiumEconomySeats);
        classSeats.put("Business", businessSeats);
        classSeats.put("First Class", firstClassSeats);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public double getBaseFare(String classType) {
        switch (classType) {
            case "Economy":
                return baseFareEconomy;
            case "Premium Economy":
                return baseFarePremiumEconomy;
            case "Business":
                return baseFareBusiness;
            case "First Class":
                return baseFareFirstClass;
            default:
                return baseFareEconomy;
        }
    }

    public int getAvailableSeats(String classType) {
        return classSeats.getOrDefault(classType, 0);
    }

    public boolean bookSeat(String classType) {
        if (classSeats.containsKey(classType) && classSeats.get(classType) > 0) {
            classSeats.put(classType, classSeats.get(classType) - 1);
            return true;
        }
        return false;
    }

    public void cancelSeat(String classType) {
        if (classSeats.containsKey(classType)) {
            classSeats.put(classType, classSeats.get(classType) + 1);
        }
    }

    @Override
    public String toString() {
        return "Flight Number: " + flightNumber +
                ", Origin: " + origin +
                ", Destination: " + destination +
                ", Date: " + date +
                ", Time: " + time +
                ", Duration: " + duration +
                ", Available Seats - Economy: " + getAvailableSeats("Economy") +
                ", Premium Economy: " + getAvailableSeats("Premium Economy") +
                ", Business: " + getAvailableSeats("Business") +
                ", First Class: " + getAvailableSeats("First Class");
    }
}

class Reservation {
    private String passengerName;
    private Flight flight;
    private String classType;
    private String fareType;
    private double baseFare;
    private double finalFare;
    private double taxes;
    private double additionalCharges;
    private double foodCharges;
    private double totalAmount;

    public Reservation(String passengerName, Flight flight, String classType, String fareType, double baseFare, double finalFare, double taxes, double additionalCharges, double foodCharges, double totalAmount) {
        this.passengerName = passengerName;
        this.flight = flight;
        this.classType = classType;
        this.fareType = fareType;
        this.baseFare = baseFare;
        this.finalFare = finalFare;
        this.taxes = taxes;
        this.additionalCharges = additionalCharges;
        this.foodCharges = foodCharges;
        this.totalAmount = totalAmount;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    @Override
    public String toString() {
        return "\n--- Reservation Details ---" +
                "\nPassenger Name: " + passengerName +
                "\nFlight Number: " + flight.getFlightNumber() +
                "\nClass: " + classType +
                "\nFare Type: " + fareType +
                "\nBase Fare: $" + baseFare +
                "\nDiscounted Fare (after applying " + fareType + " discount): $" + finalFare +
                "\nTaxes (5% GST): $" + taxes +
                "\nAdditional Charges (Service Fee): $" + additionalCharges +
                "\nFood Charges: $" + foodCharges +
                "\nTotal Amount: $" + totalAmount +
                "\nOrigin: " + flight.getOrigin() +
                "\nDestination: " + flight.getDestination() +
                "\nDate: " + flight.getDate() +
                "\nTime: " + flight.getTime();
    }
}

public class AirwaysReservationSystem extends JFrame {
    private List<Flight> flights = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private JTextArea outputArea;
    private JTextField nameField;
    private JTextField flightNumberField;

    public AirwaysReservationSystem() {
        setTitle("Mahayogi Gorakhnath airport");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(135, 206, 235)); // Light Sky Blue

        // Initialize Flights
        initializeFlights();

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(255, 255, 224)); // Light Yellow
        outputArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255)); // Dodger Blue
        JLabel headerLabel = new JLabel("Welcome to Mahayogi Gorakhnath Airport");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBackground(new Color(135, 206, 235));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Flight Number:"));
        flightNumberField = new JTextField();
        inputPanel.add(flightNumberField);
        
        JButton bookButton = new JButton("Book Flight");
        bookButton.setBackground(new Color(0, 204, 102)); // Green
        bookButton.setForeground(Color.WHITE);
        bookButton.addActionListener(new BookFlightListener());
        inputPanel.add(bookButton);

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setBackground(new Color(255, 69, 0)); // Red
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new CancelBookingListener());
        inputPanel.add(cancelButton);

        JButton viewFlightsButton = new JButton("View Available Flights");
        viewFlightsButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        viewFlightsButton.setForeground(Color.WHITE);
        viewFlightsButton.addActionListener(new ViewFlightsListener());
        inputPanel.add(viewFlightsButton);

        JButton viewReservationsButton = new JButton("View Reservations");
        viewReservationsButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        viewReservationsButton.setForeground(Color.WHITE);
        viewReservationsButton.addActionListener(new ViewReservationsListener());
        inputPanel.add(viewReservationsButton);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void initializeFlights() {
        flights.add(new Flight("A101", "Gorakhpur", "Banglore", "2024-10-20", "08:00 AM", "5h 30m", 5, 3, 2, 1, 3000, 4500, 6000, 8000));
        flights.add(new Flight("B202", "Gorakhpur", "Hyderabad", "2024-10-21", "09:30 AM", "3h 15m", 10, 5, 3, 2, 2000, 3500, 5000, 7000));
        flights.add(new Flight("C303", "Gorakhpur", "Lucknow", "2024-10-22", "11:00 AM", "6h 0m", 8, 4, 2, 1, 4000, 5500, 7000, 9000));
    }

    private void displayMessage(String message) {
        outputArea.setText(message);
    }

    private class ViewFlightsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Available Flights:\n");
            for (Flight flight : flights) {
                sb.append(flight.toString()).append("\n");
            }
            displayMessage(sb.toString());
        }
    }

    private class BookFlightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String passengerName = nameField.getText();
            String flightNumber = flightNumberField.getText();
            Flight selectedFlight = null;

            for (Flight flight : flights) {
                if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                    selectedFlight = flight;
                    break;
                }
            }

            if (selectedFlight == null) {
                displayMessage("Flight not found: " + flightNumber);
                return;
            }

            String[] classOptions = {"Economy", "Premium Economy", "Business", "First Class"};
            String classType = (String) JOptionPane.showInputDialog(null, "Select Class", "Booking Class",
                    JOptionPane.QUESTION_MESSAGE, null, classOptions, classOptions[0]);

            if (classType == null || !selectedFlight.bookSeat(classType)) {
                displayMessage("No available seats in " + classType + " class.");
                return;
            }

            double baseFare = selectedFlight.getBaseFare(classType);
            int applyDiscount = JOptionPane.showConfirmDialog(null, "Apply discount for fare type? (0 for No, 1 for Yes)");
            double discount = (applyDiscount == 1) ? 0.1 * baseFare : 0;
            double finalFare = baseFare - discount;
            double taxes = finalFare * 0.05; // 5% GST
            double additionalCharges = 200; // Service Fee
            double foodCharges = Double.parseDouble(JOptionPane.showInputDialog("Enter food charges (0 for none):"));
            double totalAmount = finalFare + taxes + additionalCharges + foodCharges;

            Reservation newReservation = new Reservation(passengerName, selectedFlight, classType, "Fare Type", baseFare, finalFare, taxes, additionalCharges, foodCharges, totalAmount);
            reservations.add(newReservation);
            displayMessage("Reservation successful!\n" + newReservation);
        }
    }

    private class CancelBookingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String passengerName = nameField.getText();
            Reservation reservationToCancel = null;
            for (Reservation reservation : reservations) {
                if (reservation.getPassengerName().equalsIgnoreCase(passengerName)) {
                    reservationToCancel = reservation;
                    break;
                }
            }

            if (reservationToCancel == null) {
                displayMessage("No reservation found for: " + passengerName);
                return;
            }

            reservationToCancel.getFlight().cancelSeat(reservationToCancel.getFlight().getFlightNumber());
            reservations.remove(reservationToCancel);
            displayMessage("Reservation for " + passengerName + " has been successfully canceled.");
        }
    }

    private class ViewReservationsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Reservations:\n");
            if (reservations.isEmpty()) {
                sb.append("No reservations found.");
            } else {
                for (Reservation reservation : reservations) {
                    sb.append(reservation.toString()).append("\n");
                }
            }
            displayMessage(sb.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirwaysReservationSystem system = new AirwaysReservationSystem();
            system.setVisible(true);
        });
    }
}
