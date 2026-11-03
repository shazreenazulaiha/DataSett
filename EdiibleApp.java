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

public class EdibleApp {
    public static void main(String[] args) {
        Edible[] objects = {new Animal("Chicken"), new Apple("Red Apple"), new Orange("Navel Orange")};

        Person p1 = new Person("Ali");
        Person p2 = new Person("Sarah");

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof Animal) {
                System.out.println(p1.eating(objects[i]));
            } else {
                System.out.println(p2.eating(objects[i]));
            }
        }
    }
}
