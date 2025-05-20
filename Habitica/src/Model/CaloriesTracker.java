package Model;

public class CaloriesTracker extends Habit {
    private int dailyCalorieGoal;
    private int caloriesConsumed;

    public CaloriesTracker(String name, String description, int level, int frequency, int dailyCalorieGoal) {
        super(name, description, level, frequency);
        this.dailyCalorieGoal = dailyCalorieGoal;
        this.caloriesConsumed = 0;
    }

    public void logCalories(int amount) {
        caloriesConsumed += amount;
    }

    public boolean goalMet() {
        return caloriesConsumed <= dailyCalorieGoal;
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public void resetCalories() {
        this.caloriesConsumed = 0;
    }
}
