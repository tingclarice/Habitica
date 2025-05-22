package Model;

public class SleepHabit extends Habit {
    private int sleepDuration; // in hours
    private String sleepQuality; // e.g., "Good", "Average", "Poor"

    public SleepHabit(String name, String description, int sleepDuration, String sleepQuality) {
        super(name, description);
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

    public boolean goalMet() {
        
    }

    public void reset() {}

    public void printDetails() {}


}
