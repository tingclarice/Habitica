package History;
import java.time.LocalDate;

public class HistoryEntry {
    private LocalDate date;
    private String habitName;
    private int progress;
    private int goal;
    private boolean completed;

    public HistoryEntry(LocalDate date, String habitName, int progress, int goal) {
        this.date = date;
        this.habitName = habitName;
        this.progress = progress;
        this.goal = goal;
        this.completed = progress >= goal;
    }

    @Override
    public String toString() {
        return String.format("%s | %-20s | Progress: %3d/%3d | %s",
                date.toString(),
                habitName,
                progress,
                goal,
                completed ? "✅ Completed" : "❌ Not Completed");
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }
    
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    
    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
    
    public boolean isCompleted() {
        return completed;
    }
}