import java.util.InputMismatchException;
import java.util.List;
import java.util.Map; // Map is an object that maps keys to values

// Belongs to Java Time package, sublass of Runtime Exception
// This exception is thrown when there are issues related to date and time operations
import java.time.DateTimeException; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Model.User;
import Model.HistoryEntry;
import Model.Habit;
import Model.CustomHabit;
import Model.DefaultHabit;
import Model.WaterIntakeHabit;
import Model.Achievement;
import Model.CaloriesTracker;
import Model.SleepHabit;
import Model.ExerciseHabit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private ArrayList<User> users = new ArrayList<>();
    private HashMap<String, Achievement> achievements = new HashMap<>();
    private Scanner s = new Scanner(System.in);
    private LocalDate currentDate;
    private User currentUser;
    private int day;
    private int month;
    private int year; 
    private int habitCount;
    private int calorieGoal ; // Default calorie goal
    private int waterGoal; // Default water goal in liters
    private int sleepGoal; // Default sleep goal in hours
    private int exerciseGoal; // Default exercise goal in minutes
    int todayWaterIntake = 0;
    int todayCalories = 0;
    int todaySleepDuration = 0;
    int todayExerciseDuration = 0;
    int tmp_calorieGoal = calorieGoal;
    int tmp_waterGoal = waterGoal;
    int tmp_sleepGoal = sleepGoal;
    int tmp_exerciseGoal = exerciseGoal;

    public Main() {
        achievements.put("JustOneStep", new Achievement("Just One Step", "Setiap perjalanan panjang dimulai dari satu langkah."));
        achievements.put("ConsistencyIsKey", new Achievement("Consistency is Key", "Konsistensi adalah kunci untuk mencapai tujuan."));
        achievements.put("HealthyMindHealthyBody", new Achievement("Healthy Mind, Healthy Body", "Jaga kesehatan mental dan fisikmu!"));
        achievements.put("ProgressNotPerfection", new Achievement("Progress Not Perfection", "Fokus pada kemajuan, bukan kesempurnaan."));
        achievements.put("SmallWins", new Achievement("Small Wins", "Rayakan setiap kemenangan kecil dalam perjalananmu!"));
        achievements.put("GoalGetter", new Achievement("Goal Getter", "Setiap pencapaian dimulai dengan keputusan untuk mencoba."));
        achievements.put("HabitBuilder", new Achievement("Habit Builder", "Bangun kebiasaan baik setiap hari!"));
        achievements.put("HealthyLifestyle", new Achievement("Healthy Lifestyle", "Gaya hidup sehat adalah investasi terbaik untuk masa depanmu!"));
        achievements.put("ConsistencyKing", new Achievement("Consistency King", "Setiap hari adalah kesempatan baru untuk menjadi lebih baik."));
        achievements.put("MindfulLiving", new Achievement("Mindful Living", "Hiduplah dengan kesadaran penuh dan nikmati setiap momen."));
    
        // Load all existing users from database at startup
        loadUsersFromDatabase();
        
        // currentUser remains null until someone logs in
        currentUser = null;
    }

    private void loadUsersFromDatabase() {
    File databaseFolder = new File("Database");
    File[] files = databaseFolder.listFiles((dir, name) -> name.startsWith("data_") && name.endsWith(".txt"));

    if (files != null) {
        for (File file : files) {
            // FileReader = reads characters from a file
            // Buffered Reader = reads characters more efficiently (line-by-line)
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String username = "";
                String password = "";

                // Instantiates a new user object from each username
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Username=")) {
                        username = line.split("=")[1];
                    } else if (line.startsWith("Password=")) {
                        password = line.split("=")[1];
                    } 
                }

                User user = new User(username, password);


                users.add(user);
            } catch (IOException e) {
                System.out.println("Error reading file: " + file.getName());
                // e.printStackTracke() prints type exception & message about what went wrong
                // The Stack Trace is a list of the method calls that led to the error
                e.printStackTrace();
            }
        }
    }
}

    // METHOD TO PARSE & SET GOALS FOR EXISTING USERS
    public void parseAndSetGoals() {
    // Gets the filePath of the user data
    String filePath = "Database/data_" + currentUser.getUsername() + ".txt";
    boolean fileReadSuccess = false;
    
    // Tries to read file line-by-line
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean inGoalsSection = false;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }
            
            // Check for section start
            if (line.equals("=== GOALS ===")) {
                inGoalsSection = true;
                continue;
            }
            
            // If we're in the GOALS section, parse the lines
            if (inGoalsSection) {
                // Check for section end
                if (line.startsWith("===")) {
                    break;
                }
                
                // Parse key=value pairs - FIXED: Check for "=" instead of "Goal="
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2); // Split on first = only
                    if (parts.length == 2) {
                        // trim() is a method 
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        
                        try {
                            int intValue = Integer.parseInt(value);
                            switch (key) {
                                case "CaloriesGoal":
                                    calorieGoal = intValue;
                                    tmp_calorieGoal = intValue; // Also update the temp variable
                                    fileReadSuccess = true;
                                    System.out.println("Loaded CaloriesGoal: " + intValue); // Debug output
                                    break;
                                case "WaterGoal":
                                    waterGoal = intValue;
                                    tmp_waterGoal = intValue; // Also update the temp variable
                                    fileReadSuccess = true;
                                    System.out.println("Loaded WaterGoal: " + intValue); // Debug output
                                    break;
                                case "SleepGoal":
                                    sleepGoal = intValue;
                                    tmp_sleepGoal = intValue; // Also update the temp variable
                                    fileReadSuccess = true;
                                    System.out.println("Loaded SleepGoal: " + intValue); // Debug output
                                    break;
                                case "ExerciseGoal":
                                    exerciseGoal = intValue;
                                    tmp_exerciseGoal = intValue; // Also update the temp variable
                                    fileReadSuccess = true;
                                    System.out.println("Loaded ExerciseGoal: " + intValue); // Debug output
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format for " + key + ": " + value);
                        }
                    }
                }
            }
        }
    } catch (FileNotFoundException e) {
        System.err.println("Goals file not found: " + filePath);
    } catch (IOException e) {
        System.err.println("Error reading goals file: " + e.getMessage());
    }
    
    // Only set defaults if we completely failed to read any goals
    if (!fileReadSuccess) {
        setDefaultGoals();
        System.out.println("Using default goals as fallback");
    } else {
        System.out.println("Successfully loaded goals from file");
    }
}

    private void setDefaultGoals() {
        this.calorieGoal = 2000;       // Default calorie goal
        this.waterGoal = 2;            // Default water goal in liters
        this.sleepGoal = 8;            // Default sleep goal in hours
        this.exerciseGoal = 30;        // Default exercise goal in minutes
    }

    // STARTING SCREEN
    public void enterDate() {
        // ASK CURRENT DATE
        while (true) {
        try {
            System.out.println("=== WELCOME TO HABITICA ===");
            System.out.println("Please enter the current date to start tracking your habits.");
            System.out.print("Enter current year (e.g. 2025): ");
            year = s.nextInt();
            if (year < 1900 || year > 2100) {
                System.out.println("Please enter a valid year between 1900 and 2100.");
                continue;
            }
            break;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid year (number).");
            s.nextLine(); // clear buffer
        }
    }

    while (true) {
        try {
            System.out.print("Enter current month (1-12): ");
            month = s.nextInt();
            if (month < 1 || month > 12) {
                System.out.println("Month must be between 1 and 12.");
                continue;
            }
            break;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the month.");
            s.nextLine(); // clear buffer
        }
    }

    while (true) {
        try {
            System.out.print("Enter current date (1-31): ");
            day = s.nextInt();
            if (day < 1 || day > 31) {
                System.out.println("Date must be between 1 and 31.");
                continue;
            }
            break;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the date.");
            s.nextLine(); // clear buffer
        }
    }

    startMenu();

    }

    public void startMenu() {
        int input=0;
        do {
            System.out.println("""

                    ========================================
                                🌱 HABITICA 🌱
                        Health Habit Tracker Application
                    ========================================
                    Welcome to Habitica!
                    [1] Sign Up
                    [2] Log In
                    [3] Exit
                    Stay healthy by keeping track of your habits!""");

            System.out.print("Option: ");

            try {
                input = s.nextInt();
                s.nextLine(); // clear newline left behind

                switch (input) {
                    case 1 -> signUp();
                    case 2 -> logIn();
                    case 3 -> exit();
                    default -> System.out.println("Invalid option. Try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                s.nextLine(); // clear invalid input from scanner buffer
            }

        } while (input != 3);
    }

    // SIGN UP & LOG IN
    public void signUp() {
        System.out.println("\n=== SIGN UP ===");
        System.out.print("Enter username: ");
        String username = s.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already taken. Try another.");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = s.nextLine();

        User newUser = new User(username, password);
        users.add(newUser);

        // Set default goals after sign up
        setGoalStart(newUser); 
    }
    

    // METHOD FOR NEW USERS TO SET DEFAULT GOALS
    public void setGoalStart(User user) {
        System.out.println("\n=== GOAL SETTING ===");
        System.out.println("Set your daily goals for default habits you can track:");
        
        try {
                System.out.println("1. Calories Tracker - How many calories you want to consume each day? (e.g. 2000 kcal)");
                System.out.print("Input: ");
                calorieGoal = s.nextInt();
                tmp_calorieGoal = calorieGoal; // Save the initial goal
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the calories tracker.");
            s.nextLine(); // clear buffer
        }

        try {
            System.out.println("\n2. Water Intake Habit - How many liters of water you want to drink each day? (e.g. 2 liters)");
            System.out.print("Input: ");
            waterGoal = s.nextInt();
            tmp_waterGoal = waterGoal; // Save the initial goal
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the water intake habit.");
            s.nextLine(); // clear buffer
        }

        try {
            System.out.println("\n3. Sleep Habit - How many hours of sleep you want to get each day? (e.g. 8 hours)");
            System.out.print("Input: ");
            sleepGoal = s.nextInt();
            tmp_sleepGoal = sleepGoal; // Save the initial goal
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the sleep habit.");
            s.nextLine(); // clear buffer
        }

        try {
            System.out.println("\n4. Exercise Habit - How many minutes of exercise you want to do each day? (e.g. 30 minutes)");
            System.out.print("Input: ");
            exerciseGoal = s.nextInt();
            tmp_exerciseGoal = exerciseGoal; // Save the initial goal
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for the exercise habit.");
            s.nextLine(); // clear buffer
        }

        setupUserFile(user.getUsername(), user.getPassword(), calorieGoal, waterGoal, sleepGoal, exerciseGoal);
        System.out.println("\nSign-up successful! You can now log in.");
    }

    public void logIn() {
        System.out.println("\n=== LOG IN ===");
        System.out.print("Enter username: ");
        String username = s.nextLine();

        System.out.print("Enter password: ");
        String password = s.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                
                currentUser = user;
                System.out.println("\nWelcome back, " + username + "!");
                
                parseAndSetGoals();
                loadCustomHabitTemplates(currentUser);
                loadUserAchievements(currentUser);
                menu(); // go to the main habit menu
                return;
            }
        }
        
    }

    public void exit() {
        System.out.println("\nThank you for using Habitica! Stay healthy 🌱");
        System.exit(0);
    }

    // USER INTERFACE
    public void menu() {
        int input = 0;

        do {
            System.out.println("""

                    ========================================
                                🌱 HABITICA 🌱
                        Health Habit Tracker Application
                    ========================================""");
            System.out.println(currentUser.getUsername() + "'s Progress for " + year + "-" + month + "-" + day);
            System.out.println(todayCalories + "/" + calorieGoal + " kcal");
            System.out.println(todayWaterIntake + "/" + waterGoal + " liters of water");
            System.out.println(todaySleepDuration + "/" + sleepGoal + " hours of sleep");
            System.out.println(todayExerciseDuration + "/" + exerciseGoal + " minutes of exercise");

            // Custom Habit Progress Display
            LocalDate today = LocalDate.of(year, month, day); // Use program's date, not LocalDate.now()
            ArrayList<CustomHabit> customTemplates = currentUser.getCustomHabitTemplates();
            for (CustomHabit ch : customTemplates) {
                int progress = ch.getProgressForDate(today);
                int goal = ch.getGoal();
                String unit = ch.getGoalUnit();
                System.out.println(progress + "/" + goal + " " + unit + " of " + ch.getName());
            }

            System.out.println("""
                    ========================================
                    Main Menu :
                    [1] Next Day
                    [2] Add Habit Progress
                    [3] Create Custom Habit
                    [4] View History
                    [5] Achievements
                    [6] Log Out
                    """);

            System.out.print("Option: ");

            try {
                input = s.nextInt();
                s.nextLine(); // clear newline

                switch (input) {
                    case 1 -> nextDay();
                    case 2 -> addHabitMenu();
                    case 3 -> createCustomHabit();
                    case 4 -> viewHistory();
                    case 5 -> achievement();
                    case 6 -> {
                        System.out.println("Logging out...");
                        startMenu();
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                s.nextLine(); // clear invalid input
            }

        } while (input != 6);
    }

    // DAY PROGRESSION
    public void nextDay() {
        try {
            System.out.println("Are you sure you want to progress to the next day? (yes/no)");
            String confirm = s.next()+s.nextLine().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("Cancelled. Returning to main menu.");
                return;
            }

            // Create current date for saving
            currentDate = LocalDate.of(year, month, day);
            
            // Save current day progress
            saveDay(currentUser.getUsername(), currentDate, calorieGoal, todayCalories, 
                    waterGoal, todayWaterIntake, sleepGoal, todaySleepDuration, 
                    exerciseGoal, todayExerciseDuration);
            
            System.out.println("\n=== PROGRESS TO NEXT DAY ===");
            
            // Add one day
            LocalDate nextDate = currentDate.plusDays(1);
            
            // Update values
            year = nextDate.getYear();
            month = nextDate.getMonthValue();
            day = nextDate.getDayOfMonth();
            habitCount = 0; // Reset habit count for the new day
            
            System.out.println("Progressed to the next day: " + nextDate);
        } catch (DateTimeException e) {
            System.out.println("Error progressing to the next day. Current date might be invalid.");
        }

        // Reset daily progress
        todayWaterIntake = 0;
        todayCalories = 0;
        todaySleepDuration = 0;
        todayExerciseDuration = 0;

        // Reset goals to default values
        calorieGoal = tmp_calorieGoal; 
        waterGoal = tmp_waterGoal;
        sleepGoal = tmp_sleepGoal;
        exerciseGoal = tmp_exerciseGoal;
        
        // Note: Custom habit progress is automatically handled by the date-based system
        // No need to manually reset as each date has its own progress tracking
    }

    // HABIT MANAGEMENT
    public void addHabitMenu() {
        int choice = 0;

        System.out.println("\n=== ADD HABIT PROGRESS ===");
        System.out.println("Choose a habit type:");

        // 1-4: Built-in habits
        System.out.println("[1] Calories Tracker");
        System.out.println("[2] Water Intake Habit");
        System.out.println("[3] Sleep Habit");
        System.out.println("[4] Exercise Habit");

        // Starting option number for custom templates
        int optionNumber = 5;

        // List custom habit templates dynamically
        ArrayList<CustomHabit> customTemplates = currentUser.getCustomHabitTemplates();
        for (int i = 0; i < customTemplates.size(); i++) {
            CustomHabit ch = customTemplates.get(i);
            // Get current date progress for display
            LocalDate today = LocalDate.of(year, month, day);
            int currentProgress = ch.getProgressForDate(today);
            System.out.printf("[%d] Custom: %s (Progress: %d/%d %s)\n", 
                optionNumber, ch.getName(), currentProgress, ch.getGoal(), ch.getGoalUnit());
            optionNumber++;
        }

        // Cancel option
        System.out.printf("[%d] Cancel\n", optionNumber);

        try {
            System.out.print("Option: ");
            choice = s.nextInt();
            s.nextLine(); // clear newline

            int builtInCount = 4;
            int cancelOption = builtInCount + customTemplates.size() + 1;

            if (choice >= 1 && choice <= builtInCount) {
                switch (choice) {
                    case 1 -> CaloriesTrackerHabit();
                    case 2 -> addWaterIntakeHabit();
                    case 3 -> sleepHabit();
                    case 4 -> ExerciseHabit();
                }
            } else if (choice > builtInCount && choice < cancelOption) {
                // User chose a custom habit, so we get the INDEX
                int habitIndex = choice - builtInCount - 1;

                // Get the EXISTING habit from the user's list of templates
                CustomHabit selectedHabit = customTemplates.get(habitIndex);

                // Use the current game date
                LocalDate today = LocalDate.of(year, month, day);

                // Ask for progress to ADD
                System.out.printf("\nYou selected: %s\n", selectedHabit.getName());
                System.out.printf("Goal: %d %s\n", selectedHabit.getGoal(), selectedHabit.getGoalUnit());
                System.out.print("Enter today's progress to add: ");
                int progressToAdd = s.nextInt();
                s.nextLine(); // clear newline

                // Get the current progress and ADD the new progress to it
                int currentProgress = selectedHabit.getProgressForDate(today);
                selectedHabit.setProgressForDate(today, currentProgress + progressToAdd);
                // DO NOT create a new habit or add it to the user again.

                // Show the result with the corrected printf
                System.out.println("✅ Progress for '" + selectedHabit.getName() + "' updated successfully!");
                // The last format specifier must be %s for the String unit

                // print format
                System.out.printf("New Progress: %d / %d %s\n", 
                    selectedHabit.getProgressForDate(today), 
                    selectedHabit.getGoal(), 
                    selectedHabit.getGoalUnit());
            } else if (choice == cancelOption) {
                System.out.println("Cancelled.");
            } else {
                System.out.println("Invalid choice.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            s.nextLine(); // clear buffer
        }
    }

    // CUSTOM HABITS SECTION - START
    // Add this method to save custom habit templates to the database file
    public void saveCustomHabitTemplate(CustomHabit habit) {
        String filePath = "./Database/data_" + currentUser.getUsername() + ".txt";
        
        try {
            // Read existing content
            // StringBuilder is a Java class used to build or edit text efficiently
            // In Java, String is immutable, that means once created it can't be changed (Java creates a new String behind the scenes)
            // StringBuilder allows you to modify the same string object without creating new ones over and over
            StringBuilder fileContent = new StringBuilder();
            File file = new File(filePath);
            
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean inCustomHabitsSection = false;
                    boolean customHabitsWritten = false;
                    
                    while ((line = reader.readLine()) != null) {
                        // Check if we're entering custom habits section
                        if (line.equals("=== CUSTOM HABIT TEMPLATES ===")) {
                            inCustomHabitsSection = true;
                            fileContent.append(line).append("\n");
                            
                            // Write all existing custom habits plus the new one
                            for (CustomHabit ch : currentUser.getCustomHabitTemplates()) {
                                fileContent.append("CustomHabitName=").append(ch.getName()).append("\n");
                                fileContent.append("CustomHabitDescription=").append(ch.getDescription()).append("\n");
                                fileContent.append("CustomHabitGoal=").append(ch.getGoal()).append("\n");
                                fileContent.append("CustomHabitUnit=").append(ch.getGoalUnit()).append("\n");
                                fileContent.append("---\n"); // Separator
                            }
                            customHabitsWritten = true;
                            continue;
                        }
                        
                        // Skip existing custom habits section content
                        if (inCustomHabitsSection) {
                            if (line.startsWith("===") && !line.equals("=== CUSTOM HABIT TEMPLATES ===")) {
                                inCustomHabitsSection = false;
                                fileContent.append(line).append("\n");
                            }
                            // Skip lines in custom habits section as we're rewriting them
                            continue;
                        }
                        
                        fileContent.append(line).append("\n");
                    }
                    
                    // If custom habits section doesn't exist, add it before daily progress
                    if (!customHabitsWritten) {
                        // Find the right place to insert (before daily progress or at the end)
                        String content = fileContent.toString();
                        // indexOf finds the position (index) of the first occurence of a substring in a string
                        int insertIndex = content.indexOf("=== DAILY PROGRESS ===");
                        
                        // indexOf returns -1 if the string is not found
                        // the line below is saying if the string exists, we can insert the content before that section
                        if (insertIndex != -1) {
                            // Insert before daily progress
                            StringBuilder newContent = new StringBuilder();

                            // append() adds text to the end of a StringBuilder object
                            newContent.append(content.substring(0, insertIndex));
                            newContent.append("=== CUSTOM HABIT TEMPLATES ===\n");
                            
                            // Add all custom habits
                            for (CustomHabit ch : currentUser.getCustomHabitTemplates()) {
                                newContent.append("CustomHabitName=").append(ch.getName()).append("\n");
                                newContent.append("CustomHabitDescription=").append(ch.getDescription()).append("\n");
                                newContent.append("CustomHabitGoal=").append(ch.getGoal()).append("\n");
                                newContent.append("CustomHabitUnit=").append(ch.getGoalUnit()).append("\n");
                                newContent.append("---\n");
                            }
                            newContent.append("\n");
                            newContent.append(content.substring(insertIndex));
                            fileContent = newContent;
                        } else {
                            // Add at the end
                            fileContent.append("\n=== CUSTOM HABIT TEMPLATES ===\n");
                            for (CustomHabit ch : currentUser.getCustomHabitTemplates()) {
                                fileContent.append("CustomHabitName=").append(ch.getName()).append("\n");
                                fileContent.append("CustomHabitDescription=").append(ch.getDescription()).append("\n");
                                fileContent.append("CustomHabitGoal=").append(ch.getGoal()).append("\n");
                                fileContent.append("CustomHabitUnit=").append(ch.getGoalUnit()).append("\n");
                                fileContent.append("---\n");
                            }
                        }
                    }
                }
            }
            
            // Write the updated content back to file
            try (FileWriter writer = new FileWriter(file, false)) { // false = overwrite
                writer.write(fileContent.toString());
            }
            
            System.out.println("✅ Custom habit template saved successfully!");
            
        } catch (IOException e) {
            System.out.println("❌ Error saving custom habit template: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add this method to load custom habit templates when user logs in
    public void loadCustomHabitTemplates(User user) {
        String filePath = "Database/data_" + user.getUsername() + ".txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inCustomHabitsSection = false;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.equals("=== CUSTOM HABIT TEMPLATES ===")) {
                    inCustomHabitsSection = true;
                    continue;
                }
                
                if (inCustomHabitsSection) {
                    // Check for section end
                    if (line.startsWith("===") && !line.equals("=== CUSTOM HABIT TEMPLATES ===")) {
                        break;
                    }
                    
                    // Parse custom habit
                    if (line.startsWith("CustomHabitName=")) {
                        String name = line.split("=", 2)[1];
                        
                        // Read description
                        line = reader.readLine().trim();
                        String description = "";
                        if (line.startsWith("CustomHabitDescription=")) {
                            // 2 means: "only split once, into 2 pieces"
                            description = line.split("=", 2)[1];
                        }
                        
                        // Read goal
                        line = reader.readLine().trim();
                        int goal = 0;
                        if (line.startsWith("CustomHabitGoal=")) {
                            // 2 means: "only split once, into 2 pieces"
                            goal = Integer.parseInt(line.split("=", 2)[1]);
                        }
                        
                        // Read unit
                        line = reader.readLine().trim();
                        String unit = "";
                        if (line.startsWith("CustomHabitUnit=")) {
                            unit = line.split("=", 2)[1];
                        }
                        
                        // Create and add the custom habit template
                        CustomHabit customHabit = new CustomHabit(name, description, goal, unit);
                        user.addCustomHabitTemplate(customHabit);
                        
                        System.out.println("Loaded custom habit: " + name);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing custom habits found for user: " + user.getUsername());
        } catch (IOException e) {
            System.err.println("Error loading custom habits: " + e.getMessage());
        }
    }

    // Modified createCustomHabit method to save the template
    public void createCustomHabit() {
        System.out.println("\n=== CREATE CUSTOM HABIT ===");

        System.out.print("Enter name of your habit: ");
        String name = s.nextLine();

        System.out.print("Enter description: ");
        String description = s.nextLine();

        System.out.print("Enter your goal (e.g., 10 repetitions, 100 pages): ");
        String goalInput = s.next() + s.nextLine(); // Read the goal input

        String[] parts = goalInput.split(" ", 2);

        int goalAmount = 0;
        String goalUnit = "";

        try {
            goalAmount = Integer.parseInt(parts[0]); // First part is number
            goalUnit = parts[1]; // Second part is unit
        } catch (Exception e) {
            System.out.println("Invalid format. Please enter a number followed by a unit (e.g., 10 repetitions).");
            return; // Exit early if input is invalid
        }

        // Create the custom habit
        CustomHabit customHabit = new CustomHabit(name, description, goalAmount, goalUnit);

        // Add to user's templates
        currentUser.addCustomHabitTemplate(customHabit);
        
        // Save to database file
        saveCustomHabitTemplate(customHabit);

        System.out.println("✅ Custom habit added successfully!");
        customHabit.printDetails(); // Show details
    }
    // CUSTOM HABITS SECTION - END

    // HISTORY START
    private List<HistoryEntry> loadHistoryEntries() {
    ArrayList<HistoryEntry> entries = new ArrayList<>();
    String filePath = "Database/data_" + currentUser.getUsername() + ".txt";
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        // Declaration = telling Java what type 
        String line;
        HistoryEntry currentEntry = null;
        boolean inDailyProgress = false;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("=== DAILY PROGRESS ===")) {
                inDailyProgress = true;
                continue;
            }
            
            if (line.equals("=== END OF DAY ===")) {
                inDailyProgress = false;
                currentEntry = null;
                continue;
            }
            
            if (inDailyProgress) {
                if (line.startsWith("Date=")) {
                    currentEntry = new HistoryEntry(LocalDate.parse(line.split("=")[1]));
                    entries.add(currentEntry);
                } 
                else if (currentEntry != null) {
                    if (line.startsWith("CaloriesGoal=")) {
                        processDefaultHabit(reader, currentEntry, "Calories", line);
                    } 
                    else if (line.startsWith("WaterGoal=")) {
                        processDefaultHabit(reader, currentEntry, "Water", line);
                    } 
                    else if (line.startsWith("SleepGoal=")) {
                        processDefaultHabit(reader, currentEntry, "Sleep", line);
                    } 
                    else if (line.startsWith("ExerciseGoal=")) {
                        processDefaultHabit(reader, currentEntry, "Exercise", line);
                    }
                    else if (line.equals("=== CUSTOM HABITS ===")) {
                        processCustomHabits(reader, currentEntry);
                    }
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading history: " + e.getMessage());
    }
    
    return entries;
}

private void processDefaultHabit(BufferedReader reader, HistoryEntry entry, 
                            String habitName, String goalLine) throws IOException {
    int goal = Integer.parseInt(goalLine.split("=")[1]);
    String progressLine = reader.readLine().trim();
    int progress = Integer.parseInt(progressLine.split("=")[1]);
    String metLine = reader.readLine().trim();
    boolean met = Boolean.parseBoolean(metLine.split("=")[1]);
    entry.addDefaultHabit(habitName, goal, progress, met);
}

// PROCESS CUSTOM HABITS METHOD - START
private void processCustomHabits(BufferedReader reader, HistoryEntry entry) throws IOException {
    String line;
    while ((line = reader.readLine()) != null && !(line = line.trim()).equals("---")) {
        if (line.startsWith("CustomHabitName=")) {
            String name = line.split("=", 2)[1];
            
            // Read goal
            line = reader.readLine().trim();
            int goal = 0;
            if (line.startsWith("CustomHabitGoal=")) {
                goal = Integer.parseInt(line.split("=", 2)[1]);
            }
            
            // Read progress
            line = reader.readLine().trim();
            int progress = 0;
            if (line.startsWith("CustomHabitProgress=")) {
                progress = Integer.parseInt(line.split("=", 2)[1]);
            }
            
            // Read unit
            line = reader.readLine().trim();
            String unit = "";
            if (line.startsWith("CustomHabitUnit=")) {
                unit = line.split("=", 2)[1];
            }
            
            // Read met status (optional, we can calculate it)
            line = reader.readLine().trim();
            boolean met = false;
            if (line.startsWith("CustomHabitMet=")) {
                met = Boolean.parseBoolean(line.split("=", 2)[1]);
            }
            
            // Add to history entry with proper progress tracking
            entry.addCustomHabitProgress(name, "", goal, progress, unit);
        } else if (line.equals("NoCustomHabits=true")) {
            // No custom habits for this day
            break;
        } else {
            continue;
        }
    }
    }

public void viewHistory() {
    // Prints a title and loads the saved history into a list.
    System.out.println("\n=== VIEW HISTORY ===");
    List<HistoryEntry> entries = loadHistoryEntries();
    
    //  If there’s no history, print a message and exit the method.
    if (entries.isEmpty()) {
        System.out.println("No history entries found.");
        return;
    }
    
    // Sort by date (newest first)
    // Sort is a method that comes from the java.util.List interface
    // Sorts the history **by date**, with the **newest first**.
    entries.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
    // it uses a lambda expression (short-hand for creating an anonymous function)
    // if e2 is more recent than e1, it puts e2 before e1.
    // compareTo is from the "Comparable" interface (can be implemented by LocalDate, etc)

    // PAGINATION SETUP
    int pageSize = 1;
    int currentPage = 0;
    
    // entries.size() returns the total number of history entries
    // (double) entries.size() / pageSize divides the number of entries by how many you want to show per page.
    // It casts entries.size() to a double so the division isn't rounded down.
    int totalPages = (int) Math.ceil((double) entries.size() / pageSize);
    
    while (true) {
        System.out.println("\n=== HISTORY (Page " + (currentPage + 1) + " of " + totalPages + ") ===");
        
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, entries.size());
        
        for (int i = start; i < end; i++) {
            System.out.println("\n" + entries.get(i).toString());
        }
        
        System.out.println("History Menu Options:");
        System.out.println("[1] Previous Page");
        System.out.println("[2] Next Page");
        System.out.println("[3] View Details");
        System.out.println("[4] Back to Menu");
        System.out.print("Option: ");
        
        try {
            int choice = s.nextInt();
            s.nextLine();
            
            switch (choice) {
                case 1:
                    if (currentPage > 0) currentPage--;
                    else System.out.println("You're on the first page.");
                    break;
                case 2:
                    if (currentPage < totalPages - 1) currentPage++;
                    else System.out.println("You're on the last page.");
                    break;
                case 3:
                    System.out.print("Enter entry number to view details: ");
                    int entryNum = s.nextInt();
                    s.nextLine();
                    if (entryNum >= 1 && entryNum <= entries.size()) {
                        viewDetailedHistory(entries.get(entryNum - 1));
                    } else {
                        System.out.println("Invalid entry number.");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            s.nextLine();
        }
    }
}

// Enhanced viewDetailedHistory method
private void viewDetailedHistory(HistoryEntry entry) {
    System.out.println("\n=== DETAILED HISTORY FOR " + entry.getDate() + " ===");
    
    // Overall summary
    System.out.println("\n📊 DAILY SUMMARY");
    System.out.println("Total Habits Completed: " + entry.getTotalHabitsCompleted() + 
                      "/" + entry.getTotalHabitsTracked());
    System.out.println("Completion Rate: " + String.format("%.1f", entry.getCompletionPercentage()) + "%");
    
    // Default habits section
    System.out.println("\n--- Default Habits ---");
    if (entry.getGoal("Calories") > 0) {
        System.out.println("🍽️ Calories: " + entry.getProgress("Calories") + 
                        "/" + entry.getGoal("Calories") + " kcal - " + 
                        (entry.isGoalMet("Calories") ? "✅ Met" : "❌ Not Met"));
    }

    if (entry.getGoal("Water") > 0) {
        System.out.println("💧 Water: " + entry.getProgress("Water") + 
                        "/" + entry.getGoal("Water") + " L - " + 
                        (entry.isGoalMet("Water") ? "✅ Met" : "❌ Not Met"));
    }

    if (entry.getGoal("Sleep") > 0) {
        System.out.println("💤 Sleep: " + entry.getProgress("Sleep") + 
                        "/" + entry.getGoal("Sleep") + " hours - " + 
                        (entry.isGoalMet("Sleep") ? "✅ Met" : "❌ Not Met"));
    }

    if (entry.getGoal("Exercise") > 0) {
        System.out.println("🏋️ Exercise: " + entry.getProgress("Exercise") + 
                        "/" + entry.getGoal("Exercise") + " minutes - " + 
                        (entry.isGoalMet("Exercise") ? "✅ Met" : "❌ Not Met"));
    }

    // Custom habits section with detailed progress
    System.out.println("\n--- Custom Habits ---");
    if (entry.getCustomHabitsProgress().isEmpty()) {
        System.out.println("No custom habits tracked this day.");
    } else {
        for (HistoryEntry.CustomHabitProgress chp : entry.getCustomHabitsProgress()) {
            System.out.println("🎯 " + chp.getName() + ": " + 
                            chp.getProgress() + "/" + chp.getGoal() + " " + chp.getUnit() + 
                            " - " + (chp.isGoalMet() ? "✅ Met" : "❌ Not Met"));
            
            // Show description if available
            if (chp.getDescription() != null && !chp.getDescription().trim().isEmpty()) {
                System.out.println("   📝 Description: " + chp.getDescription());
            }
            
            // Show completion percentage for this habit
            double habitPercentage = chp.getGoal() > 0 ? 
                                   (double) chp.getProgress() / chp.getGoal() * 100 : 0;
            System.out.println("   📈 Progress: " + String.format("%.1f", habitPercentage) + "%");
            System.out.println();
        }
    }
    
    System.out.println("Press enter to continue...");
    s.nextLine();
}
    // HISTORY END

    // ACHIEVEMENTS START
    // Secara keseluruhan, sistem ini bekerja dengan tiga fungsi utama:
    // checkAchievement(): Mesin logika yang memeriksa apakah pengguna memenuhi syarat untuk mendapatkan achievement baru.
    // loadUserAchievements(): Fungsi yang memuat data achievement yang sudah didapat pengguna dari file saat mereka login.
    // achievement(): Antarmuka (UI) yang menampilkan daftar semua achievement (baik yang terkunci maupun yang sudah terbuka) kepada pengguna.
    public void achievement() {
        checkAchievement();
        if (currentUser.getAchievementsMap().isEmpty()) {
            System.out.println("No achievements unlocked yet.");
        } else {
            System.out.println("Your Achievements:");
            for (Map.Entry<String, Achievement> entry : achievements.entrySet()) {
                String achievementKey = entry.getKey();
                Achievement achievement = entry.getValue();
        
                // Cek apakah PENGGUNA memiliki achievement ini
                if (currentUser.getAchievementsMap().containsKey(achievementKey)) {
                    // Jika ya, cetak UNLOCKED
                    System.out.println("[UNLOCKED] " + achievement.getTitle() + ": " + achievement.getDescription());
                } else {
                    // Jika tidak, cetak LOCKED
                    System.out.println("[LOCKED]   " + achievement.getTitle() + ": " + achievement.getDescription());
                }
            }
        }        
    }
    
    // Tambahkan fungsi ini di dalam class Main Anda
    private void loadUserAchievements(User user) {
        String filePath = "Database/data_" + user.getUsername() + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Cari baris yang menyimpan data achievement
                if (line.startsWith("AchievementsUnlocked=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length > 1 && !parts[1].equalsIgnoreCase("None")) {
                        String[] unlockedKeys = parts[1].split(",");
                        for (String key : unlockedKeys) {
                            // Ambil objek Achievement dari map global berdasarkan key
                            if (achievements.containsKey(key)) {
                                // Tambahkan achievement ke object user saat ini
                                user.addAchievement(achievements.get(key));
                            }
                        }
                    }
                    // Setelah ditemukan, kita bisa berhenti membaca file
                    break; 
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading achievements for user " + user.getUsername() + ": " + e.getMessage());
        }
    }

    // Ganti seluruh fungsi checkAchievement() Anda dengan ini
public void checkAchievement() {
    if (currentUser == null) return;

    // Cek #1: Just One Step (punya 1+ habit)
    // Kondisi ini lebih baik dicek berdasarkan progres, bukan hanya definisi habit
    if (currentUser.getHabits().size() >= 1 && !currentUser.getAchievementsMap().containsKey("JustOneStep")) {
        System.out.println("🌟 Achievement Unlocked: Just One Step!");
        currentUser.addAchievement(achievements.get("JustOneStep"));
    }

    // Cek #2: Consistency is Key (menyelesaikan 3+ habit hari ini)
    if (habitCount >= 3 && !currentUser.getAchievementsMap().containsKey("ConsistencyIsKey")) {
        System.out.println("🌟 Achievement Unlocked: Consistency is Key!");
        currentUser.addAchievement(achievements.get("ConsistencyIsKey"));
    }

    // Cek #3: Healthy Mind, Healthy Body (menyelesaikan 5+ habit hari ini)
    if (habitCount >= 5 && !currentUser.getAchievementsMap().containsKey("HealthyMindHealthyBody")) {
        System.out.println("🌟 Achievement Unlocked: Healthy Mind, Healthy Body!");
        currentUser.addAchievement(achievements.get("HealthyMindHealthyBody"));
    }

    // Cek #4: Progress Not Perfection (minum 2 liter air hari ini)
    if (todayWaterIntake >= 2 && !currentUser.getAchievementsMap().containsKey("ProgressNotPerfection")) {
        System.out.println("🌟 Achievement Unlocked: Progress Not Perfection!");
        currentUser.addAchievement(achievements.get("ProgressNotPerfection"));
    }

    // Cek #5: Small Wins (tidur 8 jam hari ini)
    if (todaySleepDuration >= 8 && !currentUser.getAchievementsMap().containsKey("SmallWins")) {
        System.out.println("🌟 Achievement Unlocked: Small Wins!");
        currentUser.addAchievement(achievements.get("SmallWins"));
    }
    
    // Cek #6: Goal Getter (olahraga 30 menit hari ini)
    if (todayExerciseDuration >= 30 && !currentUser.getAchievementsMap().containsKey("GoalGetter")) {
        System.out.println("🌟 Achievement Unlocked: Goal Getter!");
        currentUser.addAchievement(achievements.get("GoalGetter"));
    }

    // Cek #7: Habit Builder (memenuhi target kalori hari ini)
    if (todayCalories >= calorieGoal && !currentUser.getAchievementsMap().containsKey("HabitBuilder")) {
        System.out.println("🌟 Achievement Unlocked: Habit Builder!");
        currentUser.addAchievement(achievements.get("HabitBuilder"));
    }

    // Cek #8: Healthy Lifestyle (minum 3 liter air hari ini)
    if (todayWaterIntake >= 3 && !currentUser.getAchievementsMap().containsKey("HealthyLifestyle")) {
        System.out.println("🌟 Achievement Unlocked: Healthy Lifestyle!");
        currentUser.addAchievement(achievements.get("HealthyLifestyle"));
    }

    // Cek #9: Consistency King (olahraga 60 menit hari ini)
    if (todayExerciseDuration >= 60 && !currentUser.getAchievementsMap().containsKey("ConsistencyKing")) {
        System.out.println("🌟 Achievement Unlocked: Consistency King!");
        currentUser.addAchievement(achievements.get("ConsistencyKing"));
    }
    
    // Cek #10: Mindful Living (membuat setidaknya satu custom habit)
    if (currentUser.getCustomHabitTemplates().size() > 0 && !currentUser.getAchievementsMap().containsKey("MindfulLiving")) {
        System.out.println("🌟 Achievement Unlocked: Mindful Living!");
        currentUser.addAchievement(achievements.get("MindfulLiving"));
    }
}

    // ACHIEVEMENTS END

    // GOAL SETTING
    public void goalSetting() {
        System.out.println("=== GOAL SETTING ===");
        System.out.println("Current Goals :");
        // Implement goal setting logic here
        

        System.out.println("=====================");
        System.out.println("Which goals would you like to change?");
        
        System.out.print("Option: ");
        int option = s.nextInt();
    }

    // Habit Specific Menu UI's
    public void CaloriesTrackerHabit() {
        System.out.println("\n=== CALORIES TRACKER 🥗 ===");
        System.out.println("Do you want to use the default calorie goal or set a custom one?");
        System.out.println("1. Use default");
        System.out.println("2. Set custom calorie goal");
        System.out.print("Option: ");
        int option = s.nextInt();
        int dailyCalorieGoal = calorieGoal; 
        s.nextLine(); // clear newline
        if(option == 1){
            System.out.println("You have chosen to use the default calorie goal of " + calorieGoal + " kcal.");
        
    }else{
        // Ask for calorie goal
        System.out.println("Select your daily calorie goal:");
        System.out.println("""
            1. 1500 kcal
            2. 1800 kcal
            3. 2000 kcal
            4. 2500 kcal
            5. Custom""");
        System.out.print("Choice: ");
        int choice = s.nextInt();
        System.out.println("");

        dailyCalorieGoal = switch (choice) {
            case 1 -> 1500;
            case 2 -> 1800;
            case 3 -> 2000;
            case 4 -> 2500;
            case 5 -> {
                System.out.print("Enter your custom daily calorie goal: ");
                yield s.nextInt();
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to 2000 kcal.");
                yield 2000;
            }
        };
        
        calorieGoal = dailyCalorieGoal;
    }
        // Ask for current calorie intake
        System.out.print("\nEnter the number of calories you've consumed today: ");
        int caloriesConsumed = s.nextInt();
    
        CaloriesTracker caloriesTracker = new CaloriesTracker(dailyCalorieGoal);
        caloriesTracker.logCalories(caloriesConsumed);
        currentUser.setCaloriesTracker(caloriesTracker);
        currentUser.getHabits().add(caloriesTracker);
    
        System.out.println("Calories Tracker Habit added!");
        System.out.println("Goal: " + dailyCalorieGoal + " kcal");
        System.out.println("Calories consumed: " + caloriesConsumed + " kcal");
        System.out.println("Goal met: " + (caloriesTracker.goalMet()));

        this.todayCalories += caloriesConsumed;


        if(this.todayCalories >= calorieGoal) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
    }
    checkAchievement();
    }
    

    public void sleepHabit() {
        System.out.println("=== SLEEP HABIT 💤 ===");
        System.out.println("\nDo you want to use the default sleep duration or set a custom one?");
        System.out.println("1. Use default");
        System.out.println("2. Set custom sleep duration");
        System.out.print("Option: ");
        int option = s.nextInt();
        s.nextLine(); // clear newline
        int targetSleepDuration = sleepGoal; // Default to 8 hours
        if (option ==1){
            System.out.println("Your default sleep duration is set to " + targetSleepDuration + " hours.");
        }else {
        System.out.println("Enter Your Sleep Duration Target");
        System.out.println("""
                1. 6 hours
                2. 7 hours
                3. 8 hours
                4. 9 hours
                5. Other
                """);
        System.out.print("Select a target: ");
        int target = s.nextInt();
        s.nextLine(); // clear newline
        targetSleepDuration = switch (target) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> {
                System.out.print("Enter your custom sleep target (in hours): ");
                yield s.nextInt();
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to 8 hours.");
                yield 8;
            }
        };
    
        sleepGoal = targetSleepDuration; // Update the sleepGoal variable
    }
    
        System.out.print("Enter today's sleep duration (in hours): ");
        int sleepDuration = s.nextInt();
        s.nextLine();
    
        System.out.print("Enter sleep quality (Good/Average/Poor): ");
        String sleepQuality = s.nextLine();
    
        SleepHabit sleepHabit = new SleepHabit(
            sleepDuration, 
            sleepQuality, 
            targetSleepDuration
        );
        currentUser.setSleepHabit(sleepHabit);
        currentUser.getHabits().add(sleepHabit);

    
        // Tampilkan hasil (jika mau)
        System.out.println("Your Sleep Habit :");
        System.out.println("Target: " + sleepHabit.getTargetSleepDuration() + " jam");
        System.out.println("Duration: " + sleepHabit.getSleepDuration() + " jam");
        System.out.println("Quality: " + sleepHabit.getSleepQuality());
        System.out.println("Habit added successfully!");
        this.todaySleepDuration += sleepDuration;


        if(this.todaySleepDuration >= targetSleepDuration) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
    }
    checkAchievement();
    }

    // Method to update custom habit progress for today
public void updateCustomHabitProgress() {
    if (currentUser.getCustomHabitTemplates().isEmpty()) {
        System.out.println("No custom habits available. Create a custom habit first!");
        return;
    }
    
    System.out.println("\n=== UPDATE CUSTOM HABIT PROGRESS ===");
    System.out.println("Available Custom Habits:");
    
    List<CustomHabit> templates = currentUser.getCustomHabitTemplates();
    for (int i = 0; i < templates.size(); i++) {
        CustomHabit habit = templates.get(i);
        int currentProgress = habit.getProgressForDate(LocalDate.now());
        System.out.println((i + 1) + ". " + habit.getName() + 
                          " (Current: " + currentProgress + "/" + habit.getGoal() + 
                          " " + habit.getGoalUnit() + ")");
    }
    
    System.out.print("Select habit to update (1-" + templates.size() + "): ");
    try {
        int choice = s.nextInt();
        s.nextLine(); // consume newline
        
        if (choice >= 1 && choice <= templates.size()) {
            CustomHabit selectedHabit = templates.get(choice - 1);
            int currentProgress = selectedHabit.getProgressForDate(LocalDate.now());
            
            System.out.println("\nSelected: " + selectedHabit.getName());
            System.out.println("Current progress: " + currentProgress + "/" + 
                             selectedHabit.getGoal() + " " + selectedHabit.getGoalUnit());
            System.out.print("Enter new progress: ");
            
            int newProgress = s.nextInt();
            s.nextLine(); // consume newline
            
            if (newProgress >= 0) {
                selectedHabit.setProgressForDate(LocalDate.now(), newProgress);
                System.out.println("✅ Progress updated successfully!");
                System.out.println("New progress: " + newProgress + "/" + 
                                 selectedHabit.getGoal() + " " + selectedHabit.getGoalUnit());
                
                // Check if goal is met
                if (newProgress >= selectedHabit.getGoal()) {
                    System.out.println("🎉 Goal achieved! Great job!");
                    habitCount++; // Update habit count for achievements
                }
                
                checkAchievement(); // Check for new achievements
            } else {
                System.out.println("❌ Invalid progress value. Progress cannot be negative.");
            }
        } else {
            System.out.println("❌ Invalid choice.");
        }
    } catch (Exception e) {
        System.out.println("❌ Invalid input. Please enter a number.");
        s.nextLine(); // consume invalid input
    }
}

    public void ExerciseHabit() {
        int targetduration; // Deklarasi variabel
        System.out.println("=== EXERCISE HABIT 🚴🏻 ===");
        System.out.println("\nDo you want to use the default exercise duration or set a custom one?");
        System.out.println("1. Use default exercise duration (" + exerciseGoal + " minutes)");
        System.out.println("2. Set custom exercise duration");
        System.out.print("Option: ");
        int option = s.nextInt();
        s.nextLine(); // clear newline
    
        if (option == 1) {
            System.out.println("You have chosen to use the default exercise duration.");
            // PERBAIKAN: Inisialisasi targetduration dengan goal default
            targetduration = exerciseGoal; 
        } else {
            System.out.println("Enter Your Exercise Duration Target");
            System.out.println("""
                    1. 30 minutes
                    2. 60 minutes (1 hour)
                    3. 90 minutes (1.5 hours)
                    4. 120 minutes (2 hours)
                    5. Other
                    """);
            System.out.print("Select a target: ");
            int target = s.nextInt();
            s.nextLine(); // clear newline
    
            targetduration = switch (target) {
                case 1 -> 30;
                case 2 -> 60;
                case 3 -> 90;
                case 4 -> 120;
                case 5 -> {
                    System.out.print("Enter your custom exercise target (in minutes): ");
                    yield s.nextInt();
                }
                default -> {
                    System.out.println("Invalid choice. Defaulting to 60 minutes.");
                    yield 60;
                }
            };

           exerciseGoal = targetduration; // Update the exerciseGoal variable
        }
            
        System.out.print("Enter your exercise duration (in minutes): ");
        int duration = s.nextInt();
        System.out.println ("Enter your exercise type (e.g., Cardio, Strength): ");
        String type = s.nextLine() + s.next();

        ExerciseHabit exerciseHabit = new ExerciseHabit(
            targetduration,
            duration, 
            type
        );
        currentUser.setExerciseHabit(exerciseHabit);
        currentUser.getHabits().add(exerciseHabit);

        
    
        // Anda mungkin ingin menambahkan habit ini ke list habit user
        // currentUser.addHabit(exerciseHabit); 
        // currentUser.setExerciseHabit(exerciseHabit); // Jika Anda punya setter spesifik
    
        System.out.println("\nYour Exercise Habit :");
        System.out.println("Target: " + exerciseHabit.getTargetduration() + " minutes");
        System.out.println("Duration: " + exerciseHabit.getDuration() + " minutes");
        System.out.println("Type: " + exerciseHabit.getType());
        System.out.println("Habit added successfully!");
    
        this.todayExerciseDuration += duration;


        if(this.todayExerciseDuration >= targetduration) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
    }
    checkAchievement();
}

    public void addWaterIntakeHabit() {
        int goal;
        System.out.println("\n=== WATER INTAKE HABIT 🥛 ===");
        System.out.println("Your current water intake goal is " + waterGoal + " liters.");
        System.out.println("You can choose from the following options or enter a custom goal:");
        System.out.println("1. Following the default goal");
        System.out.println("2. Set a custom goal");
        int option = s.nextInt();
        s.nextLine(); // clear newline
        if (option == 1){
        System.out.println("You have chosen to follow the default goal of " + waterGoal + " liters.");
        goal = waterGoal; // Use the default goal
        System.out.println("Your daily water intake goal is set to " + goal + " liters.");
        } else {
        System.out.print("Enter your daily water intake goal (in liters) ");
        System.out.println("""

                1. 1 liters
                2. 2 liters
                3. 3 liters
                """);
        System.out.println("Option: ");
        int goals = s.nextInt();
        s.nextLine();

        goal = switch (goals) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> {
                System.out.print("Invalid choice. Please enter your custom goal (in liters): ");
                yield s.nextInt();
            }
        };
        // Save the previous goal
        waterGoal = goal; // Update the waterGoal variable
    }

        System.out.print("Enter your current water intake (in liters): ");
        int waterIntake = s.nextInt();
        s.nextLine();

        WaterIntakeHabit waterHabit = new WaterIntakeHabit(
            "Water Intake", 
            "Track your daily water intake", 
            goal
        );
        currentUser.setWaterIntakeHabit(waterHabit);
    currentUser.getHabits().add(waterHabit);


        this.todayWaterIntake += waterIntake;


        System.out.println("✅ Water Intake Habit added successfully.");
        waterHabit.printDetails();

        if(this.todayWaterIntake >= goal) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
        }
        checkAchievement();
    }

    // FILE HANDLING
    public void setupUserFile(String username, String password, int calorieGoal, int waterGoal, int sleepGoal, int exerciseGoal) {
        String folderPath = "./Database/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs(); // Create the folder if not exist
        }

        String filename = folderPath + "data_" + username + ".txt";
        File file = new File(filename);

        try {
            if (file.createNewFile()) {
                System.out.println("User file created: " + filename);
            } else {
                System.out.println("User file already exists. Overwriting content.");
            }

            FileWriter writer = new FileWriter(file, false); // false = overwrite

            // User Info Section
            writer.write("=== USER INFO ===\n");
            writer.write("Username=" + username + "\n");
            writer.write("Password=" + password + "\n");

            // Goals Section
            writer.write("=== GOALS ===\n");
            writer.write("CaloriesGoal=" + calorieGoal + "\n");
            writer.write("WaterGoal=" + waterGoal + "\n");
            writer.write("SleepGoal=" + sleepGoal + "\n");
            writer.write("ExerciseGoal=" + exerciseGoal + "\n");

            writer.close();
            System.out.println("User data saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file!");
            e.printStackTrace();
        }
        
    }

    // Enhanced saveDay method to properly save custom habit progress
public void saveDay(String username, LocalDate date,
                    int calorieGoal, int calorieProgress,
                    int waterGoal, int waterProgress,
                    int sleepGoal, int sleepProgress,
                    int exerciseGoal, int exerciseProgress) {
    String filePath = "./Database/data_" + username + ".txt";
    File file = new File(filePath);

    try (FileWriter writer = new FileWriter(file, true)) {
        // Add a clear separator for daily entries
        writer.write("\n=== DAILY PROGRESS ===\n");
        writer.write("Date=" + date + "\n");
        
        // Default habits
        writer.write("CaloriesGoal=" + calorieGoal + "\n");
        writer.write("CaloriesProgress=" + calorieProgress + "\n");
        writer.write("CaloriesMet=" + (calorieProgress >= calorieGoal) + "\n");

        writer.write("WaterGoal=" + waterGoal + "\n");
        writer.write("WaterProgress=" + waterProgress + "\n");
        writer.write("WaterMet=" + (waterProgress >= waterGoal) + "\n");

        writer.write("SleepGoal=" + sleepGoal + "\n");
        writer.write("SleepProgress=" + sleepProgress + "\n");
        writer.write("SleepMet=" + (sleepProgress >= sleepGoal) + "\n");

        writer.write("ExerciseGoal=" + exerciseGoal + "\n");
        writer.write("ExerciseProgress=" + exerciseProgress + "\n");
        writer.write("ExerciseMet=" + (exerciseProgress >= exerciseGoal) + "\n");

        writer.write("AchievementsUnlocked=");
        if (currentUser.getAchievementsMap().isEmpty()) {
            writer.write("None\n");
        } else {
            String unlocked = String.join(",", currentUser.getAchievementsMap().keySet());
            writer.write(unlocked + "\n");
        }

        // Save custom habits with their progress for this specific date
        writer.write("=== CUSTOM HABITS ===\n");
        if (currentUser.getCustomHabitTemplates().isEmpty()) {
            writer.write("NoCustomHabits=true\n");
        } else {
            for (CustomHabit habit : currentUser.getCustomHabitTemplates()) {
                int progress = habit.getProgressForDate(date);
                boolean goalMet = progress >= habit.getGoal();
                
                writer.write("CustomHabitName=" + habit.getName() + "\n");
                writer.write("CustomHabitGoal=" + habit.getGoal() + "\n");
                writer.write("CustomHabitProgress=" + progress + "\n");
                writer.write("CustomHabitUnit=" + habit.getGoalUnit() + "\n");
                writer.write("CustomHabitMet=" + goalMet + "\n");
            }
        }
        writer.write("---\n"); // End of custom habits section

        writer.write("=== END OF DAY ===\n");

        System.out.println("✅ History saved for " + date);
    } catch (IOException e) {
        System.out.println("❌ Error writing history.");
        e.printStackTrace();
    }
}
}

