package Model;

import java.util.ArrayList;

public class User {
    protected String username;
    protected String password;
    protected ArrayList<Habit> habits;
    private ArrayList<CustomHabit> customHabitTemplates = new ArrayList<>();
    private ArrayList<Achievement> achievements = new ArrayList<>();
    
    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.habits = new ArrayList<>();
        this.achievements = new ArrayList<>();
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

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }
    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public void countachievements() {
    
    }

    public int getcompleteachievements(){
        return achievements.size();
    }
    
    
}
