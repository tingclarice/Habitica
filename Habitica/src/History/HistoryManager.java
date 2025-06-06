package History;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private List<HistoryEntry> entries = new ArrayList<>();
    private String currentUsername;

    public void setUser(String username) {
        this.currentUsername = username;
        loadHistory();
    }

    private String getHistoryFilename() {
        return "history_" + currentUsername + ".txt";
    }

    public void addEntry(LocalDate date, String habitName, int progress, int goal) {
        HistoryEntry entry = new HistoryEntry(date, habitName, progress, goal);
        entries.add(entry);
        saveToFile(entry);
    }

    public void displayHistory() {
        if (entries.isEmpty()) {
            System.out.println("No history available for " + currentUsername);
            return;
        }

        System.out.println("\n=== HISTORY FOR " + currentUsername + " ===");
        System.out.println("Date       | Habit Name          | Progress  | Status");
        System.out.println("-----------------------------------------------------");
        
        for (HistoryEntry entry : entries) {
            System.out.println(entry);
        }
    }

    private void saveToFile(HistoryEntry entry) {
        try (PrintWriter out = new PrintWriter(new FileWriter(getHistoryFilename(), true))) {
            out.println(entryToString(entry));
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    public void loadHistory() {
        entries.clear();
        File historyFile = new File(getHistoryFilename());
        
        if (!historyFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HistoryEntry entry = parseEntry(line);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading history for " + currentUsername);
        }
    }

    // Add this method to convert entry to string for file storage
    private String entryToString(HistoryEntry entry) {
        return String.join(",",
                entry.getDate().toString(),
                entry.getHabitName(),
                String.valueOf(entry.getProgress()),
                String.valueOf(entry.getGoal()),
                String.valueOf(entry.isCompleted()));
    }

    // Add this method to parse a line from the file into a HistoryEntry
    private HistoryEntry parseEntry(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 5) {
                return null;
            }
            
            LocalDate date = LocalDate.parse(parts[0]);
            String habitName = parts[1];
            int progress = Integer.parseInt(parts[2]);
            int goal = Integer.parseInt(parts[3]);
            boolean completed = Boolean.parseBoolean(parts[4]);
            
            return new HistoryEntry(date, habitName, progress, goal);
        } catch (Exception e) {
            System.out.println("Error parsing history entry: " + line);
            return null;
        }
    }
}