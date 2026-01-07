import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// ===== CLASS: Flight =====
class Flight {
    String flightName;
    String flightNumber;
    String source;
    String destination;
    int seatsAvailable;
    double price;

    Flight(String flightName, String flightNumber, String source, String destination, int seatsAvailable, double price) {
        this.flightName = flightName;
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
    }

    String getDetails() {
        return flightName + " (" + flightNumber + ") " + source + " → " + destination + "  |  Rs." + price;
    }
}

// ===== CLASS: Reservation =====
class Reservation {
    String passengerName;
    Flight selectedFlight;
    boolean isRoundTrip;

    Reservation(String passengerName, Flight selectedFlight, boolean isRoundTrip) {
        this.passengerName = passengerName;
        this.selectedFlight = selectedFlight;
        this.isRoundTrip = isRoundTrip;
    }

    String getTicketDetails() {
        double total = isRoundTrip ? selectedFlight.price * 2 : selectedFlight.price;
        return "----- Reservation Details -----\n"
                + "Passenger Name: " + passengerName + "\n"
                + "Flight: " + selectedFlight.flightName + " (" + selectedFlight.flightNumber + ")\n"
                + "From: " + selectedFlight.source + "\n"
                + "To: " + selectedFlight.destination + "\n"
                + "Trip Type: " + (isRoundTrip ? "Two Way (Round Trip)" : "One Way") + "\n"
                + "Total Fare: Rs." + total + "\n"
                + "----------------------------------\n"
                + "Booking Confirmed! Have a Safe Journey!";
    }
}

// ===== MAIN CLASS: AirReservationGUI =====
public class AirReservationGUI extends JFrame implements ActionListener, ItemListener {
    // GUI components
    JTextField nameField;
    JComboBox<String> flightBox;
    JRadioButton oneWay, twoWay;
    JButton bookBtn, clearBtn;
    JTextField priceField;
    TextArea outputArea;

    // Individual flights (no array)
    Flight f1 = new Flight("IndiGo", "SR2006", "Hyderabad", "Delhi", 20, 4500.0);
    Flight f2 = new Flight("AirIndia", "PT2007", "Bangalore", "Mumbai", 25, 4000.0);
    Flight f3 = new Flight("JetBlue", "BM2008", "Chennai", "Kolkata", 15, 5000.0);
    Flight f4 = new Flight("AkasaAir", "SSSS08", "Delhi", "Pune", 30, 4200.0);

    AirReservationGUI() {
        setTitle("Air Reservation System");
        setSize(550, 650);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Title =====
        JLabel title = new JLabel("Air Reservation System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        add(title, BorderLayout.NORTH);

        // ===== Center Panel =====
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new GridLayout(10, 1, 10, 10));
        centerPanel.setBackground(new Color(230, 240, 250));
        centerPanel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Passenger Name
        centerPanel.add(new Label("Enter Passenger Name:"));
        nameField = new JTextField();
        centerPanel.add(nameField);

        // Flight Selection
        centerPanel.add(new Label("Select Flight:"));
        flightBox = new JComboBox<>();
        flightBox.addItem(f1.getDetails());
        flightBox.addItem(f2.getDetails());
        flightBox.addItem(f3.getDetails());
        flightBox.addItem(f4.getDetails());
        flightBox.addItemListener(this);
        centerPanel.add(flightBox);

        // Trip Type
        centerPanel.add(new Label("Select Trip Type:"));
        oneWay = new JRadioButton("One Way");
        twoWay = new JRadioButton("Two Way (Round Trip)");
        ButtonGroup group = new ButtonGroup();
        group.add(oneWay);
        group.add(twoWay);
        oneWay.setSelected(true);

        oneWay.addItemListener(this);
        twoWay.addItemListener(this);

        JPanel tripPanel = new JPanel();
        tripPanel.add(oneWay);
        tripPanel.add(twoWay);
        centerPanel.add(tripPanel);

        // Price Display Block
        centerPanel.add(new Label("Ticket Price (Auto-calculated):"));
        priceField = new JTextField();
        priceField.setEditable(false);
        priceField.setBackground(Color.WHITE);
        priceField.setFont(new Font("Monospaced", Font.BOLD, 14));
        priceField.setForeground(Color.DARK_GRAY);
        centerPanel.add(priceField);

        // Buttons
        bookBtn = new JButton("Book Ticket");
        clearBtn = new JButton("Clear");
        bookBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(bookBtn);
        buttonPanel.add(clearBtn);
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Output Area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(outputArea, BorderLayout.SOUTH);

        // Initial Price Display
        updatePriceDisplay();
    }

    // ===== Update Price Block when flight/trip changes =====
    void updatePriceDisplay() {
        Flight selectedFlight = switch (flightBox.getSelectedIndex()) {
            case 0 -> f1;
            case 1 -> f2;
            case 2 -> f3;
            case 3 -> f4;
            default -> null;
        };

        if (selectedFlight != null) {
            double price = oneWay.isSelected() ? selectedFlight.price : selectedFlight.price * 2;
            priceField.setText("Rs. " + price + (twoWay.isSelected() ? " (Round Trip)" : " (One Way)"));
        }
    }

    // ===== Actions =====
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookBtn) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter passenger name!");
                return;
            }

            Flight selectedFlight = switch (flightBox.getSelectedIndex()) {
                case 0 -> f1;
                case 1 -> f2;
                case 2 -> f3;
                case 3 -> f4;
                default -> null;
            };

            boolean isRoundTrip = twoWay.isSelected();
            Reservation r = new Reservation(name, selectedFlight, isRoundTrip);
            outputArea.setText(r.getTicketDetails());
        } 
        else if (e.getSource() == clearBtn) {
            nameField.setText("");
            oneWay.setSelected(true);
            flightBox.setSelectedIndex(0);
            outputArea.setText("");
            updatePriceDisplay();
        }
    }

    // ===== Update price block when selection changes =====
    public void itemStateChanged(ItemEvent e) {
        updatePriceDisplay();
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirReservationGUI gui = new AirReservationGUI();
            gui.setVisible(true);
        });
    }
}
