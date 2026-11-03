import java.text.DecimalFormat;

// Interface
interface Measurable {
    double getMeasure();
    String getName();
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

class DataSet implements Measurable {
    private double sum;
    private Measurable max;
    private int count;

    public DataSet() {
        sum = 0;
        count = 0;
        max = null;
    }

    public void add(Measurable x) {
        sum += x.getMeasure();
        if (count == 0 || x.getMeasure() > max.getMeasure()) {
            max = x;
        }
        count++;
    }

    public double getAverage() {
        if (count == 0) return 0;
        return sum / count;
    }

    public Measurable getMaximum() {
        return max;
    }

    public int getCount() {
        return count;
    }

    @Override
    public double getMeasure() {
        return getAverage();
    }

    @Override
    public String getName() {
        return "DataSet";
    }
}

public class DataSetApp {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        DataSet accounts = new DataSet();
        accounts.add(new BankAccount(0));
        accounts.add(new BankAccount(10000));
        accounts.add(new BankAccount(2000));
        System.out.println("Average balance: " + df.format(accounts.getAverage()));
        System.out.println("Highest balance: " + df.format(accounts.getMaximum().getMeasure()));

        DataSet coins = new DataSet();
        coins.add(new Coin(0.25, "quarter"));
        coins.add(new Coin(0.1, "dime"));
        coins.add(new Coin(0.05, "nickel"));
        System.out.println("Average coin value: " + df.format(coins.getAverage()));
        System.out.println("Most valuable coin: " + coins.getMaximum().getName());

        DataSet students = new DataSet();
        students.add(new Student("Ali", 1.72));
        students.add(new Student("Siti", 1.65));
        students.add(new Student("Raj", 1.80));
        System.out.println("Average height: " + df.format(students.getAverage()));
        System.out.println("Tallest student: " + students.getMaximum().getName());
    }
}