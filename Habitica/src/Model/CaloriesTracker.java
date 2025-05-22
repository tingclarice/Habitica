package Model;

public class CaloriesTracker extends DefaultHabit {
    private int dailyCalorieGoal;
    private int caloriesConsumed;

    public CaloriesTracker(int goal) {
        super("Calories Tracker", "Track your calorie intake");
        this.dailyCalorieGoal = goal;
        this.caloriesConsumed = 0;
    }

    public void logCalories(int amount) {
        caloriesConsumed += amount;
    }

    public boolean goalMet() {
        return caloriesConsumed <= dailyCalorieGoal;
    }

    public void reset() {
        this.caloriesConsumed = 0;
    }

    public void printDetails() {
        System.out.println("Calories Intake : " + caloriesConsumed + "/" + dailyCalorieGoal + "calories");
    }
}
