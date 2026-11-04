import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

interface Price {
    void getPrice(Customer c);
}

class Customer {
    private String name;
    private double salary;

    public Customer(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() { return name; }
    public double getSalary() { return salary; }
}

class Product implements Price {
    private String type;
    private Customer buy;

    public Product(String type) { this.type = type; }

    public String getType() { return type; }

    @Override
    public String toString() { return "The product is "; }

    @Override
    public void getPrice(Customer c) {
        buy = c;
        System.out.println(this);
        if (buy.getSalary() > 4000) {
            System.out.println("The customer's name is " + buy.getName()
                    + " with salary RM" + buy.getSalary() + " can buy this product.");
            System.out.println("Your salary is higher than the price's product.");
        } else {
            System.out.println("The customer's name is " + buy.getName()
                    + " with salary RM" + buy.getSalary() + " cannot buy this product.");
            System.out.println("The product price is more than your salary.");
        }
    }
}

class SmartPhone extends Product {
    private String nameSP;
    private double priceSP;

    public SmartPhone(String type, String nameSP, double priceSP) {
        super(type);
        this.nameSP = nameSP;
        this.priceSP = priceSP;
    }

    @Override
    public String toString() { return super.toString() + nameSP + "."; }
}

class Laptop extends Product {
    private String nameL;
    private double priceL;

    public Laptop(String type, String nameL, double priceL) {
        super(type);
        this.nameL = nameL;
        this.priceL = priceL;
    }

    @Override
    public String toString() { return super.toString() + nameL + "."; }
}

class Car implements Price {
    private String brand;
    private double priceCar;
    private Customer buy;

    public Car(String brand, double priceCar) {
        this.brand = brand;
        this.priceCar = priceCar;
    }

    public String getBrand() { return brand; }

    @Override
    public String toString() {
        return "The car brand is " + brand + " with price RM" + priceCar + ".";
    }

    @Override
    public void getPrice(Customer c) {
        buy = c;
        System.out.println(this);
        if (buy.getSalary() > 8000) {
            System.out.println("The customer's name is " + buy.getName()
                    + " with salary RM" + buy.getSalary()
                    + " can buy this car. Your salary is higher than the price's car.");
        } else {
            System.out.println("The customer's name is " + buy.getName()
                    + " with salary RM" + buy.getSalary()
                    + " cannot buy this car. The car price is more than your salary.");
        }
    }
}

// GUI
public class PriceApp extends JFrame {
    private JComboBox itemBox = new JComboBox(new String[] {"SmartPhone", "Laptop", "Car"});
    private JLabel itemNameLabel = new JLabel("Product Name:");
    private JTextField itemNameField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField customerField = new JTextField();
    private JTextField salaryField = new JTextField();
    private JTextArea output = new JTextArea(12, 40);

    public PriceApp() {
        super("Price Management System");

        // ----- title -----
        JLabel title = new JLabel("PRICE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 90, 150));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // ----- form -----
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Customer Wants To Buy"));

        JButton checkBtn = new JButton("Check Purchase");
        JButton demoBtn = new JButton("Run Demo");

        form.add(new JLabel("Item Type:"));
        form.add(itemBox);
        form.add(itemNameLabel);
        form.add(itemNameField);
        form.add(new JLabel("Price (RM):"));
        form.add(priceField);
        form.add(new JLabel("Customer Name:"));
        form.add(customerField);
        form.add(new JLabel("Customer Salary (RM):"));
        form.add(salaryField);
        form.add(checkBtn);
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
        itemBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateForm();
            }
        });
        checkBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkPurchase();
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
                clearFields();
            }
        });

        updateForm();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(660, 640);
        setLocationRelativeTo(null);
    }

    // Cars use a brand, products use a name
    private void updateForm() {
        if (itemBox.getSelectedIndex() == 2) {
            itemNameLabel.setText("Car Brand:");
        } else {
            itemNameLabel.setText("Product Name:");
        }
    }

    private void clearFields() {
        itemNameField.setText("");
        priceField.setText("");
        customerField.setText("");
        salaryField.setText("");
    }

    // getPrice() prints to the console, so capture that text for the output box
    private String capture(Price item, Customer c) {
        PrintStream old = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        try {
            item.getPrice(c);
        } finally {
            System.out.flush();
            System.setOut(old);
        }
        return bos.toString().replace("\r", "");
    }

    private void checkPurchase() {
        String itemName = itemNameField.getText().trim();
        String customerName = customerField.getText().trim();

        if (itemName.length() == 0 || customerName.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.",
                    "Missing data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        double salary;
        try {
            price = Double.parseDouble(priceField.getText().trim());
            salary = Double.parseDouble(salaryField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and salary must be valid numbers.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Price item;
        int type = itemBox.getSelectedIndex();
        if (type == 0) {
            item = new SmartPhone("SmartPhone", itemName, price);
        } else if (type == 1) {
            item = new Laptop("Laptop", itemName, price);
        } else {
            item = new Car(itemName, price);
        }

        Customer c = new Customer(customerName, salary);
        output.append(capture(item, c) + "\n");

        clearFields();
        itemNameField.requestFocus();
    }

    // Same data as your original main
    private void runDemo() {
        SmartPhone sp = new SmartPhone("SmartPhone", "Iphone 14 ProMax", 5500.00);
        Laptop lp = new Laptop("Laptop", "Lenovo Yoga", 6000.00);
        Car c = new Car("BMW", 200000);
        Customer c1 = new Customer("Sally", 3000.00);
        Customer c2 = new Customer("Aaron", 8000.00);
        Customer c3 = new Customer("Simon", 10000.00);

        output.append("========== DEMO ==========\n");
        output.append(capture(sp, c1) + "\n");
        output.append(capture(lp, c2) + "\n");
        output.append(capture(c, c3) + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PriceApp().setVisible(true);
            }
        });
    }
}
