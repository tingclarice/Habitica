package Model;

public class CaloriesTracker extends DefaultHabit {
    private int dailyCalorieGoal;
    private int caloriesConsumed;

    // Constructor with only calorie goal
    public CaloriesTracker(int goal) {
        super("Calories Tracker", "Track your daily calorie intake");
        this.dailyCalorieGoal = goal;
        this.caloriesConsumed = 0;
    }

    // Full constructor if needed later
    public CaloriesTracker(String name, String description, int goal) {
        super(name, description);
        this.dailyCalorieGoal = goal;
        this.caloriesConsumed = 0;
    }

    public void logCalories(int amount) {
        caloriesConsumed += amount;
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public int getGoal() {
        return dailyCalorieGoal;
    }

    public void setGoal(int goal) {
        this.dailyCalorieGoal = goal;
    }

    @Override
    public boolean goalMet() {
        return caloriesConsumed <= dailyCalorieGoal;
    }

    @Override
    public void printDetails() {
        System.out.println("Calories Tracker: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Calories Consumed: " + caloriesConsumed + "/" + dailyCalorieGoal + " calories");
        System.out.println("Goal Met: " + goalMet());
    }
}
