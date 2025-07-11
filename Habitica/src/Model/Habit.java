package Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Habit {
    protected String name;
    protected String description;
    protected int streak;

    public Habit(String name, String description) {
        this.name = name;
        this.description = description;
        this.streak = 0; // Initialize streak to 0
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
