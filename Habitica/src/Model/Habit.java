package Model;

public abstract class Habit {
    protected String name;
    protected String description;
    protected int difficulty;
    protected int frequency;
    protected int streak;

    public Habit(String name, String description, int difficulty, int frequency) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
