package Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryEntry {
    private LocalDate date;
    private Map<String, Integer> goals = new HashMap<>();
    private Map<String, Integer> progress = new HashMap<>();
    private Map<String, Boolean> goalsMet = new HashMap<>();
    private List<CustomHabit> customHabits = new ArrayList<>();

    public HistoryEntry(LocalDate date) {
        this.date = date;
    }

    // Add default habit data
    public void addDefaultHabit(String habitName, int goal, int progress, boolean met) {
        goals.put(habitName, goal);
        this.progress.put(habitName, progress);
        goalsMet.put(habitName, met);
    }

    // Add custom habit
    public void addCustomHabit(CustomHabit habit) {
        customHabits.add(habit);
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public int getGoal(String habitName) {
        return goals.getOrDefault(habitName, 0);
    }

    public int getProgress(String habitName) {
        return progress.getOrDefault(habitName, 0);
    }

    public boolean isGoalMet(String habitName) {
        return goalsMet.getOrDefault(habitName, false);
    }

    public List<CustomHabit> getCustomHabits() {
        return customHabits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(date).append("\n");
        
        // Default habits
        for (String habit : goals.keySet()) {
            sb.append(getHabitDisplayName(habit))
              .append(": ").append(getProgress(habit))
              .append("/").append(getGoal(habit))
              .append(" ").append(getHabitUnit(habit))
              .append(" - ").append(isGoalMet(habit) ? "✅" : "❌")
              .append("\n");
        }
        
        // Custom habits
        if (!customHabits.isEmpty()) {
            sb.append("\nCustom Habits:\n");
            for (CustomHabit habit : customHabits) {
                sb.append("- ").append(habit.getName())
                  .append(": ").append(habit.getProgressForDate(date))
                  .append("/").append(habit.getGoal())
                  .append(" ").append(habit.getGoalUnit())
                  .append(" - ").append(habit.getProgressForDate(date) >= habit.getGoal() ? "✅" : "❌")
                  .append("\n");
            }
        }
        
        return sb.toString();
    }

    private String getHabitDisplayName(String habit) {
        switch (habit) {
            case "Calories": return "🍽️ Calories";
            case "Water": return "💧 Water";
            case "Sleep": return "💤 Sleep";
            case "Exercise": return "🏋️ Exercise";
            default: return habit;
        }
    }

    private String getHabitUnit(String habit) {
        switch (habit) {
            case "Calories": return "kcal";
            case "Water": return "L";
            case "Sleep": return "hrs";
            case "Exercise": return "min";
            default: return "";
        }
    }
}