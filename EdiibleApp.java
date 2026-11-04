import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

interface Edible {
    String howToEat();
}

class Animal implements Edible {
    private String name;

    public Animal(String name) { this.name = name; }

    @Override
    public String howToEat() {
        return name + ". " + name + " needs to be cooked first before eating.";
    }
}

abstract class Fruit implements Edible {
    protected String name;

    public Fruit(String name) { this.name = name; }
}

class Orange extends Fruit {
    public Orange(String name) { super(name); }

    @Override
    public String howToEat() {
        return name + ". " + name + " can make orange butter cake.";
    }
}

class Apple extends Fruit {
    public Apple(String name) { super(name); }

    @Override
    public String howToEat() {
        return name + ". " + name + " can make apple pie.";
    }
}

class Person {
    private String name;
    private Edible eat;

    public Person(String name) {
        this.name = name;
    }

    public String eating(Edible eat) {
        return name + " is eating " + eat.howToEat();
    }
}

// GUI
public class EdibleApp extends JFrame {
    private Person p1 = new Person("Ali");
    private Person p2 = new Person("Sarah");

    private JComboBox personBox = new JComboBox(new String[] {"Ali", "Sarah"});
    private JComboBox typeBox = new JComboBox(new String[] {"Animal", "Apple", "Orange"});
    private JTextField nameField = new JTextField();
    private JTextArea output = new JTextArea(10, 40);

    public EdibleApp() {
        super("Edible Management System");

        // ----- title -----
        JLabel title = new JLabel("EDIBLE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 90, 150));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // ----- form -----
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Who Is Eating What?"));

        JButton eatBtn = new JButton("Eat");
        JButton demoBtn = new JButton("Run Demo");

        form.add(new JLabel("Person:"));
        form.add(personBox);
        form.add(new JLabel("Food Type:"));
        form.add(typeBox);
        form.add(new JLabel("Food Name:"));
        form.add(nameField);
        form.add(eatBtn);
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
        eatBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eat();
            }
        });
        nameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eat();
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
                nameField.setText("");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 520);
        setLocationRelativeTo(null);
    }

    // Eat the food chosen in the form
    private void eat() {
        String name = nameField.getText().trim();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter a food name.",
                    "Missing name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Edible food;
        int type = typeBox.getSelectedIndex();
        if (type == 0) {
            food = new Animal(name);
        } else if (type == 1) {
            food = new Apple(name);
        } else {
            food = new Orange(name);
        }

        Person p = (personBox.getSelectedIndex() == 0) ? p1 : p2;
        output.append(p.eating(food) + "\n");
        nameField.setText("");
        nameField.requestFocus();
    }

    // Same logic as your original main: animals go to Ali, fruits go to Sarah
    private void runDemo() {
        Edible[] objects = {new Animal("Chicken"), new Apple("Red Apple"),
                            new Orange("Navel Orange")};

        output.append("\n========== DEMO ==========\n");
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof Animal) {
                output.append(p1.eating(objects[i]) + "\n");
            } else {
                output.append(p2.eating(objects[i]) + "\n");
            }
        }
        output.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EdibleApp().setVisible(true);
            }
        });
    }
}
