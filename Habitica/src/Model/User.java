package Model;

import java.util.ArrayList;
import Model.Habit;

public class User {
    protected String username;
    protected String password;
    protected ArrayList<Habit> habits;
    
    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.habits = new ArrayList<>();
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
}
