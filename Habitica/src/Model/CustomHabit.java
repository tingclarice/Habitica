package Model;

public class CustomHabit extends Habit {
    private int goal;
    private int progress;

    public CustomHabit(String name, String description, int goal) {
        super(name, description);
        this.goal = goal;
        this.progress = 0;
    }

    public void logProgress(int amount) {
        progress += amount;
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

    @Override
    public boolean goalMet() {
        return progress >= goal;
    }

    @Override
    public void reset() {
        this.progress = 0;
    }

    @Override
    public void printDetails() {
        System.out.println("Custom Habit: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Goal: " + goal);
        System.out.println("Progress: " + progress);
        System.out.println("Goal Met: " + (goalMet() ? "Yes" : "No"));
    }

    
}
