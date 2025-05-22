package Model;

public class CustomHabit extends Habit {
    private int goal;
    private int progress;

    public CustomHabit(String name, String description, int frequency, int goal) {
        super(name, description, frequency);
        this.goal = goal;
        this.progress = 0;
    }

    public void logProgress(int amount) {
        progress += amount;
    }

    public boolean goalMet() {
        return progress >= goal;
    }

    public int getProgress() {
        return progress;
    }

    public void resetProgress() {
        this.progress = 0;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
