package Model;

public abstract class Habit {
    protected String name;
    protected String description;
    protected int frequency;
    protected int streak;

    public Habit(String name, String description) {
        this.name = name;
        this.description = description;

        // How often the user wants to complete habit
        // e.g. 1 = Daily; 2 = Weekly; 3= Monthly;
        this.frequency = 1;

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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    // Enforced method for subclasses
    public abstract boolean goalMet();

    public abstract void reset();

    public abstract void printDetails();

}
