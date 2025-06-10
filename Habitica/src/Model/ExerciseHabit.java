package Model;

public class ExerciseHabit extends DefaultHabit {
    private int targetduration; // in minutes// e.g., 1-5
    private int duration; // in minutes
    private String type; // e.g., cardio, strength training, flexibility

    // Constructor with default name and description
    public ExerciseHabit(int targetduration, int duration, String type) {
        super("Exercise Habit", "Tracks daily exercise activity and type"); // default values
        this.targetduration = targetduration;
        this.duration = duration;
        this.type = type;
    }

    // Existing full constructor
    public ExerciseHabit( String name, String description, int targetduration, int duration, String type) {
        super(name, description);
        this.targetduration = targetduration;
        this.duration = duration;
        this.type = type;
    }

    public int getTargetduration() {
        return targetduration;
    }
    public void setTargetduration(int targetduration) {
        this.targetduration = targetduration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean goalMet() {
        return duration >= targetduration; // Goal met if actual duration is greater than or equal to target
    }

    @Override
    public void printDetails() {
        System.out.println("Exercise Type: " + type);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Target Duration: " + targetduration + " minutes");
        System.out.println("Goal Met: " + goalMet());
    }
}
