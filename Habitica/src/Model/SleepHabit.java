package Model;

public class SleepHabit extends Habit {
    private int sleepDuration; // in hours
    private String sleepQuality; // e.g., "Good", "Average", "Poor"

    public SleepHabit(String name, String description, int difficulty, int frequency, int sleepDuration, String sleepQuality) {
        super(name, description, difficulty, frequency);
        this.sleepDuration = sleepDuration;
        this.sleepQuality = sleepQuality;
    }

    public int getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(int sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

}
