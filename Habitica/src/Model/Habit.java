package Model;

public abstract class Habit {
    protected String name;
    protected String description;
    protected int level;
    protected int frequency;
    protected int streak;

    public Habit(String name, String description, int level, int frequency) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.frequency = frequency;
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

    public int getlevel() {
        return level;
    }

    public void setlevel(int level) {
        this.level = level;
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

}
