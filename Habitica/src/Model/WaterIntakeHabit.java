package Model;

public class WaterIntakeHabit extends DefaultHabit {
    private int waterIntake; // in liters
    private int goal; // in liters

    public WaterIntakeHabit(String name, String description, int goal) {
        super(name, description);
        this.waterIntake = 0;
        this.goal = goal;
    }

    public void logWaterIntake(int amount) {
        waterIntake += amount;
    }

    public int getWaterIntake() {
        return waterIntake;
    }
    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public boolean goalMet() {
        return waterIntake >= goal;
    }

    public void reset() {
        this.waterIntake = 0;
    }

    public void printDetails() {}
}
