import java.util.InputMismatchException;
import java.util.Map;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.HashMap;

import Model.User;
import Model.Habit;
// import Model.CustomHabit;
import Model.DefaultHabit;
import Model.WaterIntakeHabit;
import Model.Achievement;
import Model.CaloriesTracker;
import Model.CustomHabit;
import Model.SleepHabit;
import Model.ExerciseHabit;
import Model.DailyHistory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

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

    private void loadUsersFromDatabase() {
    File databaseFolder = new File("Database");
    File[] files = databaseFolder.listFiles((dir, name) -> name.startsWith("data_") && name.endsWith(".txt"));

    if (files != null) {
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String username = "";
                String password = "";

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
                e.printStackTrace();
            }
        }
    }
}

    public void parseAndSetGoals() {
    String filePath = "Database/data_" + currentUser.getUsername() + ".txt";
    boolean fileReadSuccess = false;
    
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
        setGoalStart(newUser); // Set default goals after sign up
    }
    

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
                    [4] History
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
                    // case 4 -> history();
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
                // User chose a custom habit template
                int templateIndex = choice - builtInCount - 1;
                CustomHabit template = customTemplates.get(templateIndex);

                // Use the current game date instead of LocalDate.now()
                LocalDate today = LocalDate.of(year, month, day);
                int currentProgress = template.getProgressForDate(today);

                // Ask for progress
                System.out.printf("You selected: %s\n", template.getName());
                System.out.printf("Goal: %d\n", template.getGoal());
                System.out.print("Enter today's progress: ");
                int progress = s.nextInt();
                s.nextLine();

                // Create a new CustomHabit and set progress
                CustomHabit newCustomHabit = new CustomHabit(
                    template.getName(), 
                    template.getDescription(), 
                    template.getGoal(), 
                    template.getGoalUnit());
                
                newCustomHabit.setProgressForDate(today, progress);
                currentUser.addHabit(newCustomHabit);

                // Show result
                System.out.println("✅ Custom Habit '" + newCustomHabit.getName() + "' added successfully!");
                System.out.printf("Progress: %d / %d %d\n", newCustomHabit.getProgressForDate(today), newCustomHabit.getGoal(), newCustomHabit.getGoalUnit());
                System.out.println("Goal met: " + (newCustomHabit.goalMet() ? "✅ Yes" : "❌ No"));
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

    // ADD HABIT MENU FOR EACH HABITs
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

        // (Optional) Add to a list of habits if you maintain one
        currentUser.addCustomHabitTemplate(customHabit); // Assuming habitList is a List<Habit> you maintain

        System.out.println("✅ Custom habit added successfully!");
        customHabit.printDetails(); // Show details
        menu();
    }

    // HISTORY START
    // Complete History System - Add these methods to your Main class

    
    // HISTORY END

    // ACHIEVEMENTS START
    public void achievement() {
        checkAchievement();
        if (currentUser.getAchievementsMap().isEmpty()) {
            System.out.println("No achievements unlocked yet.");
        } else {
            System.out.println("Your Achievements:");
            for (Map.Entry<String, Achievement> entry : currentUser.getAchievementsMap().entrySet()) {
                System.out.println(entry.getValue());
            }
        }        
    }
    

    public void checkAchievement(){
        if(currentUser == null) return;

        // Just One Step: Jika user memiliki setidaknya 1 habit
        if (currentUser.getHabits().size() >= 1) {
            currentUser.addAchievement(achievements.get("JustOneStep"));
        }

        // Consistency is Key: Jika user menyelesaikan 3 habit dalam sehari
        if (habitCount >= 3) {
            currentUser.addAchievement(achievements.get("ConsistencyIsKey"));
        }

        // Healthy Mind, Healthy Body: Jika user menyelesaikan 5 habit dalam sehari
        if (habitCount >= 5) {
            currentUser.addAchievement(achievements.get("HealthyMindHealthyBody"));
        }

        // Progress Not Perfection: Jika user minum 2 liter air
        WaterIntakeHabit waterHabit = currentUser.getWaterIntake();
        if (waterHabit != null && waterHabit.getWaterIntake() >= 2) {
            currentUser.addAchievement(achievements.get("ProgressNotPerfection"));
        }

        // Small Wins: Jika user tidur 8 jam
        SleepHabit sleepHabit = currentUser.getSleepHabit();
        if (sleepHabit != null && sleepHabit.getSleepDuration() >= 8) {
            currentUser.addAchievement(achievements.get("SmallWins"));
        }
        
        // Goal Getter: Jika user berolahraga 30 menit
        ExerciseHabit exerciseHabit = currentUser.getExerciseHabit();
        if (exerciseHabit != null && exerciseHabit.getDuration() >= 30) {
            currentUser.addAchievement(achievements.get("GoalGetter"));
        }

        // Habit Builder: Jika user memenuhi target kalori (goalMet())
        CaloriesTracker caloriesTracker = currentUser.getCaloriesTracker();
        if (caloriesTracker != null && caloriesTracker.goalMet()) {
            currentUser.addAchievement(achievements.get("HabitBuilder"));
        }

        // Healthy Lifestyle: Jika user minum 3 liter air
        if (waterHabit != null && waterHabit.getWaterIntake() >= 3) {
            currentUser.addAchievement(achievements.get("HealthyLifestyle"));
        }

        // Consistency King: Jika user berolahraga 1 jam
        if (exerciseHabit != null && exerciseHabit.getDuration() >= 60) {
            currentUser.addAchievement(achievements.get("ConsistencyKing"));
        }
        
        // Mindful Living: Jika user membuat setidaknya satu custom habit template
        if (currentUser.getCustomHabitTemplates().size() > 0) {
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
        currentUser.getHabits().add(caloriesTracker);
    
        System.out.println("Calories Tracker Habit added!");
        System.out.println("Goal: " + dailyCalorieGoal + " kcal");
        System.out.println("Calories consumed: " + caloriesConsumed + " kcal");
        System.out.println("Goal met: " + (caloriesTracker.goalMet()));

        this.todayCalories += caloriesConsumed;


        if(this.todayCalories >= calorieGoal) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
    }
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

        this.todayWaterIntake += waterIntake;


        System.out.println("✅ Water Intake Habit added successfully.");
        waterHabit.printDetails();

        if(this.todayWaterIntake >= goal) { // Gunakan this.todayWaterIntake untuk pengecekan
            habitCount++;
        }
        
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

    public void saveDay(String username, LocalDate date,
                                    int calorieGoal, int calorieProgress,
                                    int waterGoal, int waterProgress,
                                    int sleepGoal, int sleepProgress,
                                    int exerciseGoal, int exerciseProgress) 
    {
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

            // Save custom habits
            saveCustomHabitsForDay(writer, date);
            
            writer.write("=== END OF DAY ===\n");

            System.out.println("✅ History saved for " + date);
        } catch (IOException e) {
            System.out.println("❌ Error writing history.");
            e.printStackTrace();
        }
    }

    private void saveCustomHabitsForDay(FileWriter writer, LocalDate date) throws IOException {
    ArrayList<CustomHabit> customTemplates = currentUser.getCustomHabitTemplates();
    
    if (!customTemplates.isEmpty()) {
        writer.write("=== CUSTOM HABITS ===\n");
        
        for (CustomHabit habit : customTemplates) {
            int progress = habit.getProgressForDate(date);
            int goal = habit.getGoal();
            boolean met = progress >= goal;
            
            writer.write("CustomHabitName=" + habit.getName() + "\n");
            writer.write("CustomHabitGoal=" + goal + "\n");
            writer.write("CustomHabitProgress=" + progress + "\n");
            writer.write("CustomHabitUnit=" + habit.getGoalUnit() + "\n");
            writer.write("CustomHabitMet=" + met + "\n");
            writer.write("---\n"); // Separator between custom habits
        }
    }
}

    }

