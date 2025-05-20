package Model;

public class WaterIntakeHabit extends Habit {
    private int waterIntake; // in liters
    private int goal; // in liters

    public WaterIntakeHabit(String name, String description, int level, int frequency, int goal) {
        super(name, description, level, frequency);
        this.waterIntake = 0;
        this.goal = goal;
    }

    public void logWaterIntake(int amount) {
        waterIntake += amount;
    }

    public boolean goalMet() {
        return waterIntake >= goal;
    }

    public int getWaterIntake() {
        return waterIntake;
    }
    
    public int getWaterInTake(){
        return waterIntake;
    }

    public void resetWaterIntake() {
        this.waterIntake = 0;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

}
