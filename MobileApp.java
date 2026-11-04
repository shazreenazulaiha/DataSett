import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

interface RadioTuner {
    void setStation(String station);
    void setFrequency(double frequency);
}

class MobileDevice implements RadioTuner {
    protected String manufacturer;
    protected String model;
    public String station;
    public double frequency;

    public MobileDevice(String manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public void printDetails() {
        System.out.println("Manufacturer: " + manufacturer);
        System.out.println("Model: " + model);
    }

    @Override
    public void setStation(String station) { this.station = station; }

    @Override
    public void setFrequency(double frequency) { this.frequency = frequency; }
}

class SmartPhone extends MobileDevice {
    private String type;

    public SmartPhone(String manufacturer, String model, String type) {
        super(manufacturer, model);
        this.type = type;
    }

    @Override
    public void printDetails() {
        System.out.println("SmartPhone Detail:");
        super.printDetails();
        System.out.println("Type: " + type);
    }
}

class BasicPhone extends MobileDevice {
    public BasicPhone(String manufacturer, String model) {
        super(manufacturer, model);
    }

    @Override
    public void printDetails() {
        System.out.println("Basic Phone Detail:");
        super.printDetails();
    }
}

// GUI
public class MobileApp extends JFrame {
    private JComboBox deviceBox = new JComboBox(new String[] {"SmartPhone", "Basic Phone"});
    private JTextField manufacturerField = new JTextField();
    private JTextField modelField = new JTextField();
    private JLabel typeLabel = new JLabel("Type (SmartPhone only):");
    private JTextField typeField = new JTextField();
    private JTextField stationField = new JTextField();
    private JTextField frequencyField = new JTextField();
    private JTextArea output = new JTextArea(12, 40);

    public MobileApp() {
        super("Mobile Device Management System");

        // ----- title -----
        JLabel title = new JLabel("MOBILE DEVICE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 90, 150));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // ----- form -----
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add New Device"));

        JButton addBtn = new JButton("Add Device");
        JButton demoBtn = new JButton("Run Demo");

        form.add(new JLabel("Device Type:"));
        form.add(deviceBox);
        form.add(new JLabel("Manufacturer:"));
        form.add(manufacturerField);
        form.add(new JLabel("Model:"));
        form.add(modelField);
        form.add(typeLabel);
        form.add(typeField);
        form.add(new JLabel("Radio Station:"));
        form.add(stationField);
        form.add(new JLabel("Frequency (MHz):"));
        form.add(frequencyField);
        form.add(addBtn);
        form.add(demoBtn);

        // ----- output -----
        output.setEditable(false);
        output.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        // ----- clear button -----
        JButton clearBtn = new JButton("Clear All");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(clearBtn);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        center.add(form, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // ----- events -----
        deviceBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateForm();
            }
        });
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDevice();
            }
        });
        demoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runDemo();
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.setText("");
                manufacturerField.setText("");
                modelField.setText("");
                typeField.setText("");
                stationField.setText("");
                frequencyField.setText("");
            }
        });

        updateForm();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 640);
        setLocationRelativeTo(null);
    }

    // Basic phones have no "type", so disable that field for them
    private void updateForm() {
        boolean isSmart = deviceBox.getSelectedIndex() == 0;
        typeField.setEnabled(isSmart);
        typeLabel.setEnabled(isSmart);
        if (!isSmart) typeField.setText("");
    }

    // printDetails() prints to the console, so capture that text for the output box
    private String captureDetails(MobileDevice d) {
        PrintStream old = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        try {
            d.printDetails();
        } finally {
            System.out.flush();
            System.setOut(old);
        }
        return bos.toString().replace("\r", "");
    }

    private void showDevice(MobileDevice d) {
        output.append(captureDetails(d));
        output.append("Station: " + d.station + "\n");
        output.append("Frequency: " + d.frequency + "\n\n");
    }

    private void addDevice() {
        String manufacturer = manufacturerField.getText().trim();
        String model = modelField.getText().trim();
        String type = typeField.getText().trim();
        String station = stationField.getText().trim();
        boolean isSmart = deviceBox.getSelectedIndex() == 0;

        if (manufacturer.length() == 0 || model.length() == 0 || station.length() == 0
                || (isSmart && type.length() == 0)) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.",
                    "Missing data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double frequency;
        try {
            frequency = Double.parseDouble(frequencyField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid frequency.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MobileDevice d;
        if (isSmart) {
            d = new SmartPhone(manufacturer, model, type);
        } else {
            d = new BasicPhone(manufacturer, model);
        }
        d.setStation(station);
        d.setFrequency(frequency);
        showDevice(d);

        manufacturerField.setText("");
        modelField.setText("");
        typeField.setText("");
        stationField.setText("");
        frequencyField.setText("");
        manufacturerField.requestFocus();
    }

    // Same data as your original main
    private void runDemo() {
        MobileDevice m1 = new SmartPhone("Samsung", "S20", "Galaxy");
        MobileDevice m2 = new BasicPhone("Sonny Ericson", "Walkman");
        m1.setStation("Hot FM");
        m1.setFrequency(97.60);
        m2.setStation("Fly FM");
        m2.setFrequency(109.8);

        output.append("========== DEMO ==========\n");
        showDevice(m1);
        showDevice(m2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MobileApp().setVisible(true);
            }
        });
    }
}
