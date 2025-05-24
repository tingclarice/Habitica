package Model;

public class SleepHabit extends Habit {
    private int sleepDuration; // in hours
    private String sleepQuality; // e.g., "Good", "Average", "Poor"
    private int targetSleepDuration; // in hours

    // Constructor with only sleep-related inputs
    public SleepHabit(int sleepDuration, String sleepQuality, int targetSleepDuration) {
        super("Sleep Habit", "Tracks sleep duration and quality"); // default values
        this.sleepDuration = sleepDuration;
        this.sleepQuality = sleepQuality;
        this.targetSleepDuration = targetSleepDuration;
    }

    // Existing full constructor
    public SleepHabit(String name, String description, int sleepDuration, String sleepQuality, int targetSleepDuration) {
        super(name, description);
        this.sleepDuration = sleepDuration;
        this.sleepQuality = sleepQuality;
        this.targetSleepDuration = targetSleepDuration;
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

    public int getTargetSleepDuration() {
        return targetSleepDuration;
    }

    public void setTargetSleepDuration(int targetSleepDuration) {
        this.targetSleepDuration = targetSleepDuration;
    }

    @Override
    public boolean goalMet() {
        if (sleepDuration >= targetSleepDuration) {
            return true; // Goal met if actual sleep duration is greater than or equal to target
        } else {
            return false; // Goal not met
        }
    }

    @Override
    public void reset() {
        this.sleepDuration = 0; // Reset sleep duration
        this.sleepQuality = "Unknown"; // Reset sleep quality to a default value
        this.targetSleepDuration = 0; // Reset target sleep duration
    }

    @Override
    public void printDetails() {
        System.out.println("Sleep Habit: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Sleep Duration: " + sleepDuration + " hours");
        System.out.println("Sleep Quality: " + sleepQuality);
        System.out.println("Target Sleep Duration: " + targetSleepDuration + " hours");
        System.out.println("Goal Met: " + goalMet());
    }


}
