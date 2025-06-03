package Model;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    protected String username;
    protected String password;
    protected ArrayList<Habit> habits;
    private ArrayList<CustomHabit> customHabitTemplates = new ArrayList<>();
    private HashMap<String, Achievement> achievements = new HashMap<>();
    
    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.habits = new ArrayList<>();
        this.achievements = new HashMap<>();
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public void removeHabit(int index) {
        if (index >= 0 && index < habits.size()) {
            habits.remove(index);
        }
    }

    public ArrayList<CustomHabit> getCustomHabitTemplates() {
        return customHabitTemplates;
    }

    public void addCustomHabitTemplate(CustomHabit habit) {
        customHabitTemplates.add(habit);
}

    public void removeCustomHabitTemplate(int index) {
        if (index >= 0 && index < customHabitTemplates.size()) {
            customHabitTemplates.remove(index);
        }
    }

    // Method to reset all habits
    public void resetAllHabits() {
        for (Habit habit : habits) {
            habit.reset();
        }
    }

    // Method to print all habits
    public void printAllHabits() {
        for (Habit habit : habits) {
            habit.printDetails();
        }
    }

    // view all templates 
    public void viewCustomHabitTemplates() {
        if (customHabitTemplates.isEmpty()) {
            System.out.println("No custom habit templates saved.");
            return;
        }
        for (int i = 0; i < customHabitTemplates.size(); i++) {
            CustomHabit ch = customHabitTemplates.get(i);
            System.out.printf("[%d] %s - %s (Goal: %d)\n", i + 1, ch.getName(), ch.getDescription(), ch.getGoal());
        }
    }

    public HashMap<String, Achievement> getAchievementsMap() {
        return achievements;
    }

    
    public void addAchievement(Achievement achievement) {
        if (!achievements.containsKey(achievement.getTitle())) {
            achievements.put(achievement.getTitle(), achievement);
        } else {
            System.out.println("Achievement already exists.");
        }
    }
    
    public Achievement getTitle(String title) {
        return achievements.get(title);
    }

    // tidak membutuhkan setter karena sifat datanya tetap

    public WaterIntakeHabit getWaterIntake() {
        for (Habit habit : habits) {
            if (habit instanceof WaterIntakeHabit) {
                WaterIntakeHabit waterHabit = (WaterIntakeHabit) habit;
                System.out.println("Water Intake Habit: " + waterHabit.getName() + ", Intake: " + waterHabit.getWaterIntake() + " liters, Goal: " + waterHabit.getGoal() + " liters");
            }
        }
        return null;
    }
    
    public SleepHabit getSleepHabit() {
        for (Habit habit : habits) {
            if (habit instanceof SleepHabit) {
                SleepHabit sleepHabit = (SleepHabit) habit;
                System.out.println("Sleep Habit: " + sleepHabit.getName() + ", Duration: " + sleepHabit.getSleepDuration() + " hours, Quality: " + sleepHabit.getSleepQuality() + ", Target: " + sleepHabit.getTargetSleepDuration() + " hours");
            }
        }
        return null;
    }
    public ExerciseHabit getExerciseHabit() {
        for (Habit habit : habits) {
            if (habit instanceof ExerciseHabit) {
                ExerciseHabit exerciseHabit = (ExerciseHabit) habit;
                System.out.println("Exercise Habit: " + exerciseHabit.getName() + ", Duration: " + exerciseHabit.getDuration() + " minutes, Type: " + exerciseHabit.getType());
            }
        }
        return null;
    }
    public CaloriesTracker getCaloriesTracker() {
        for (Habit habit : habits) {
            if (habit instanceof CaloriesTracker) {
                CaloriesTracker caloriesHabit = (CaloriesTracker) habit;
                System.out.println("Calories Tracker: " + caloriesHabit.getName() + ", Calories Consumed: " + caloriesHabit.getCaloriesConsumed() + ", Goal: " + caloriesHabit.getGoal() + " calories");
            }
        }
        return null;
    }
}


