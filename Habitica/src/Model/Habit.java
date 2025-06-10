package Model;

public abstract class Habit {
    protected String name;
    protected String description;


    public Habit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Enforced method for subclasses
    public abstract boolean goalMet();


    public abstract void printDetails();

}
