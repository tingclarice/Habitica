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
    private List<CustomHabitProgress> customHabitsProgress = new ArrayList<>();

    // Inner class to store custom habit progress for a specific date
    public static class CustomHabitProgress {
        private String name;
        private String description;
        private int goal;
        private int progress;
        private String unit;
        private boolean goalMet;

        public CustomHabitProgress(String name, String description, int goal, int progress, String unit) {
            this.name = name;
            this.description = description;
            this.goal = goal;
            this.progress = progress;
            this.unit = unit;
            this.goalMet = progress >= goal;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getGoal() { return goal; }
        public int getProgress() { return progress; }
        public String getUnit() { return unit; }
        public boolean isGoalMet() { return goalMet; }

        @Override
        public String toString() {
            return name + ": " + progress + "/" + goal + " " + unit + 
                   " - " + (goalMet ? "✅ Met" : "❌ Not Met");
        }
    }

    public HistoryEntry(LocalDate date) {
        this.date = date;
    }

    // Add default habit data
    public void addDefaultHabit(String habitName, int goal, int progress, boolean met) {
        goals.put(habitName, goal);
        this.progress.put(habitName, progress);
        goalsMet.put(habitName, met);
    }

    // Add custom habit progress for this specific date
    public void addCustomHabitProgress(String name, String description, int goal, int progress, String unit) {
        customHabitsProgress.add(new CustomHabitProgress(name, description, goal, progress, unit));
    }

    // Add custom habit (backward compatibility)
    public void addCustomHabit(CustomHabit habit) {
        int progress = habit.getProgressForDate(this.date);
        addCustomHabitProgress(habit.getName(), habit.getDescription(), 
                             habit.getGoal(), progress, habit.getGoalUnit());
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

    public List<CustomHabitProgress> getCustomHabitsProgress() {
        return customHabitsProgress;
    }

    // Backward compatibility
    public List<CustomHabit> getCustomHabits() {
        List<CustomHabit> habits = new ArrayList<>();
        for (CustomHabitProgress chp : customHabitsProgress) {
            CustomHabit habit = new CustomHabit(chp.getName(), chp.getDescription(), 
                                              chp.getGoal(), chp.getUnit());
            habit.setProgressForDate(this.date, chp.getProgress());
            habits.add(habit);
        }
        return habits;
    }

    // Get total habits completed today
    public int getTotalHabitsCompleted() {
        int completed = 0;
        
        // Count default habits
        for (boolean met : goalsMet.values()) {
            if (met) completed++;
        }
        
        // Count custom habits
        for (CustomHabitProgress chp : customHabitsProgress) {
            if (chp.isGoalMet()) completed++;
        }
        
        return completed;
    }

    // Get total habits tracked today
    public int getTotalHabitsTracked() {
        return goals.size() + customHabitsProgress.size();
    }

    // Get completion percentage
    public double getCompletionPercentage() {
        int total = getTotalHabitsTracked();
        if (total == 0) return 0.0;
        return (double) getTotalHabitsCompleted() / total * 100;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("📅 Date: ").append(date).append("\n");
        
        // Summary
        sb.append("📊 Summary: ").append(getTotalHabitsCompleted())
          .append("/").append(getTotalHabitsTracked())
          .append(" habits completed (").append(String.format("%.1f", getCompletionPercentage()))
          .append("%)\n\n");
        
        // Default habits
        if (!goals.isEmpty()) {
            sb.append("--- Default Habits ---\n");
            for (String habit : goals.keySet()) {
                sb.append(getHabitDisplayName(habit))
                  .append(": ").append(getProgress(habit))
                  .append("/").append(getGoal(habit))
                  .append(" ").append(getHabitUnit(habit))
                  .append(" - ").append(isGoalMet(habit) ? "✅" : "❌")
                  .append("\n");
            }
            sb.append("\n");
        }
        
        // Custom habits
        if (!customHabitsProgress.isEmpty()) {
            sb.append("--- Custom Habits ---\n");
            for (CustomHabitProgress chp : customHabitsProgress) {
                sb.append("🎯 ").append(chp.getName())
                  .append(": ").append(chp.getProgress())
                  .append("/").append(chp.getGoal())
                  .append(" ").append(chp.getUnit())
                  .append(" - ").append(chp.isGoalMet() ? "✅" : "❌");
                
                // Add description if available
                if (chp.getDescription() != null && !chp.getDescription().trim().isEmpty()) {
                    sb.append(" (").append(chp.getDescription()).append(")");
                }
                sb.append("\n");
            }
        } else if (goals.isEmpty()) {
            sb.append("No habits tracked on this date.\n");
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