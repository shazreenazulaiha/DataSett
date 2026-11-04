import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Interface 1: anything that can be measured
interface Measurable {
    double getMeasure();
    String getName();
}

// Interface 2: anything that collects Measurable objects and reports statistics
interface Statistics {
    void add(Measurable x);
    double getAverage();
    Measurable getMaximum();
    int getCount();
}

// BankAccount class
class BankAccount implements Measurable {
    private int accNumber;
    private double balance;
    private static int lastAssignedNumber = 0;

    public BankAccount() {
        lastAssignedNumber++;
        accNumber = lastAssignedNumber;
        balance = 0.0;
    }

    public BankAccount(double balance) {
        lastAssignedNumber++;
        accNumber = lastAssignedNumber;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accNumber;
    }

    @Override
    public double getMeasure() {
        return balance;
    }

    @Override
    public String getName() {
        return "Bank Account";
    }
}

// Coin class
class Coin implements Measurable {
    private double value;
    private String name;

    public Coin(double value, String name) {
        this.value = value;
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double getMeasure() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}

// Student class
class Student implements Measurable {
    private String name;
    private double height;

    public Student(String name, double height) {
        this.name = name;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getMeasure() {
        return height;
    }

    @Override
    public String getName() {
        return name;
    }
}

// DataSet implements Statistics
class DataSet implements Statistics {
    private double sum;
    private Measurable max;
    private int count;

    public DataSet() {
        sum = 0;
        count = 0;
        max = null;
    }

    @Override
    public void add(Measurable x) {
        sum += x.getMeasure();
        if (count == 0 || x.getMeasure() > max.getMeasure()) {
            max = x;
        }
        count++;
    }

    @Override
    public double getAverage() {
        if (count == 0) return 0;
        return sum / count;
    }

    @Override
    public Measurable getMaximum() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }
}

// GUI
public class DataSetApp extends JFrame {
    private Statistics accounts = new DataSet();
    private Statistics coins = new DataSet();
    private Statistics students = new DataSet();

    private JComboBox typeBox = new JComboBox(new String[] {"Bank Account", "Coin", "Student"});
    private JTextField nameField = new JTextField();
    private JTextField valueField = new JTextField();
    private JLabel nameLabel = new JLabel("Name (Coin / Student):");
    private JTextArea output = new JTextArea(10, 40);

    public DataSetApp() {
        super("DataSet Management System");

        // ----- title -----
        JLabel title = new JLabel("DATASET MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 90, 150));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // ----- form -----
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add New Record"));

        JButton addBtn = new JButton("Add Data");
        JButton summaryBtn = new JButton("Calculate Summary");

        form.add(new JLabel("Data Type:"));
        form.add(typeBox);
        form.add(nameLabel);
        form.add(nameField);
        form.add(new JLabel("Balance / Value / Height:"));
        form.add(valueField);
        form.add(addBtn);
        form.add(summaryBtn);

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
        typeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateForm();
            }
        });
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addData();
            }
        });
        summaryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSummary();
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accounts = new DataSet();
                coins = new DataSet();
                students = new DataSet();
                output.setText("");
                nameField.setText("");
                valueField.setText("");
            }
        });

        updateForm();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 560);
        setLocationRelativeTo(null);
    }

    // Bank accounts have no name, so disable the name field for them
    private void updateForm() {
        boolean needsName = typeBox.getSelectedIndex() != 0;
        nameField.setEnabled(needsName);
        nameLabel.setEnabled(needsName);
        if (!needsName) nameField.setText("");
    }

    private void addData() {
        int type = typeBox.getSelectedIndex();
        String name = nameField.getText().trim();
        double value;

        try {
            value = Double.parseDouble(valueField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type != 0 && name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter a name.",
                    "Missing name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type == 0) {
            BankAccount acc = new BankAccount(value);
            accounts.add(acc);
            output.append("Added: Bank Account " + acc.getAccountNumber()
                    + " | Balance: " + value + "\n");
        } else if (type == 1) {
            coins.add(new Coin(value, name));
            output.append("Added: Coin " + name + " | Value: " + value + "\n");
        } else {
            students.add(new Student(name, value));
            output.append("Added: Student " + name + " | Height: " + value + "\n");
        }

        nameField.setText("");
        valueField.setText("");
    }

    private void showSummary() {
        output.append("\n========== DATASET SUMMARY ==========\n");

        if (accounts.getCount() == 0 && coins.getCount() == 0 && students.getCount() == 0) {
            output.append("No records available.\n");
            return;
        }

        if (accounts.getCount() > 0) {
            output.append("Bank Average Balance: " + accounts.getAverage() + "\n");
            output.append("Bank Highest Balance: " + accounts.getMaximum().getMeasure() + "\n");
        }
        if (coins.getCount() > 0) {
            output.append("Coin Average Value: " + coins.getAverage() + "\n");
            output.append("Most Valuable Coin: " + coins.getMaximum().getName() + "\n");
        }
        if (students.getCount() > 0) {
            output.append("Average Height: " + students.getAverage() + "\n");
            output.append("Tallest Student: " + students.getMaximum().getName() + "\n");
        }
        output.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DataSetApp().setVisible(true);
            }
        });
    }
}
