package Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CustomHabit extends Habit {
    private int goal;
    private Map<LocalDate, Integer> progressMap = new HashMap<>();
    private String goalUnit;

    public CustomHabit(String name, String description, int goal, String goalUnit) {
        super(name, description);
        this.goal = goal;
        this.goalUnit = goalUnit;
    }

    public void logProgress(LocalDate date, int amount) {
        int current = progressMap.getOrDefault(date, 0);
        progressMap.put(date, current + amount);
    }

    public int getProgressForDate(LocalDate date) {
        return progressMap.getOrDefault(date, 0);
    }

    public void setProgressForDate(LocalDate date, int progress) {
        progressMap.put(date, progress);
    }

    public void resetProgressForDate(LocalDate date) {
        progressMap.remove(date);
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getGoalUnit() {
        return goalUnit;
    }

    public void setGoalUnit(String goalUnit) {
        this.goalUnit = goalUnit;
    }

    public boolean goalMetForDate(LocalDate date) {
        return getProgressForDate(date) >= goal;
    }


    @Override
    public boolean goalMet() {
        LocalDate today = LocalDate.now();
        return getProgressForDate(today) >= goal;
    }

    @Override
    public void reset() {
        LocalDate today = LocalDate.now();
        progressMap.put(today,0);
    }

    @Override
    public void printDetails() {
        LocalDate today = LocalDate.now();
        int todayProgress = getProgressForDate(today);
        
        System.out.println("Custom Habit: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Progress: " + todayProgress + "/" + goal + " " + goalUnit);
        System.out.println("Goal Met: " + goalMet());
    }


    
}
