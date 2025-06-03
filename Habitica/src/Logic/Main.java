import java.util.InputMismatchException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

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

public class Main {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Achievement> achievements = new ArrayList<>();
    private Scanner s = new Scanner(System.in);
    private LocalDate currentDate;
    private User currentUser = null;
    private int day;
    private int month;
    private int year; 
    private int habitCount = 0;

    public Main() {
        achievements.add(new Achievement("Just One Step", "Setiap perjalanan panjang dimulai dari satu langkah."));
        achievements.add(new Achievement("Consistency is Key", "Konsistensi adalah kunci untuk mencapai tujuan."));
        achievements.add(new Achievement("Healthy Mind, Healthy Body", "Jaga kesehatan mental dan fisikmu!"));
        achievements.add(new Achievement("Progress Not Perfection", "Fokus pada kemajuan, bukan kesempurnaan."));
        achievements.add(new Achievement("Small Wins", "Rayakan setiap kemenangan kecil dalam perjalananmu!"));
        achievements.add(new Achievement("Goal Getter", "Setiap pencapaian dimulai dengan keputusan untuk mencoba."));
        achievements.add(new Achievement("Habit Builder", "Bangun kebiasaan baik setiap hari!"));
        achievements.add(new Achievement("Healthy Lifestyle", "Gaya hidup sehat adalah investasi terbaik untuk masa depanmu!"));
        achievements.add(new Achievement("Consistency King", "Setiap hari adalah kesempatan baru untuk menjadi lebih baik."));
        achievements.add(new Achievement("Mindful Living", "Hiduplah dengan kesadaran penuh dan nikmati setiap momen."));
    }

    // STARTING SCREEN
    public void start() {
        int input = 0;

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

    // GOAL SETTING
    System.out.println("\n=== GOAL SETTING ===");
    System.out.println("Set your daily goals for default habits you can track:");
    
    try {
            System.out.println("1. Calories Tracker - How many calories you want to consume each day? (e.g. 2000 kcal)");
            day = s.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number for the calories tracker.");
        s.nextLine(); // clear buffer
    }

    try {
        System.out.println("2. Water Intake Habit - How many liters of water you want to drink each day? (e.g. 2 liters)");
        month = s.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number for the water intake habit.");
        s.nextLine(); // clear buffer
    }

    try {
        System.out.println("3. Sleep Habit - How many hours of sleep you want to get each day? (e.g. 8 hours)");
        year = s.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number for the sleep habit.");
        s.nextLine(); // clear buffer
    }

    try {
        System.out.println("4. Exercise Habit - How many minutes of exercise you want to do each day? (e.g. 30 minutes)");
        day = s.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number for the exercise habit.");
        s.nextLine(); // clear buffer
    }

        // START MENU
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
        System.out.println("Sign-up successful! You can now log in.");
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
                menu(); // go to the main habit menu
                return;
            }
        }
        System.out.println("Invalid username or password. Try again.");
    }

    public void exit() {
        System.out.println("\nThank you for using Habitica! Stay healthy 🌱");
        System.exit(0);
    }

    // USER INTERFACE
    public void menu() {
        int input = 0;
        String username = currentUser.getUsername();
        String date = year + "-" + month + "-" + day;
        String menuTitle = "🌱 HABITICA 🌱";
        String progress = username + "'s Progress — " + date;

        do {
            // Calculate the total box width
            int boxWidth = 53;

            // Top border
            System.out.println("╔" + "═".repeat(boxWidth) + "╗");

            // Centered title
            printCenteredLine(" "+ menuTitle, boxWidth);

            System.out.println("╠" + "═".repeat(boxWidth) + "╣");
            for (Habit habit : currentUser.getHabits()) {
                String status = String.format("%s: %d/%d %s",
                    habit.getName(),
                    habit.getProgress(),
                    habit.getGoal(),
                    habit.goalMet() ? "✅" : "❌");

                // Shorten if too long
                if (status.length() > boxWidth - 4) {
                    status = status.substring(0, boxWidth - 7) + "...";
                }

                printCenteredLine(status, boxWidth);
            }

            System.out.println("╠" + "═".repeat(boxWidth) + "╣");

            // Divider
            System.out.println("╠" + "═".repeat(boxWidth) + "╣");

            // Menu items
            String[] menuItems = {
                " [1] Next Day",
                " [2] Add Habit",
                " [3] Edit Habit",
                " [4] Delete Habit",
                " [5] Create Custom Habit",
                " [6] History",
                " [8] Log Out"
            };

            for (String item : menuItems) {
                int padding = boxWidth - item.length() - 1; // 1 space after '║'
                System.out.println("║ " + item + " ".repeat(padding) + "║");
            }

            // Bottom border
            System.out.println("╚" + "═".repeat(boxWidth) + "╝");

            // Input prompt
            System.out.print("➡️  Option: ");

            try {
                input = s.nextInt();
                s.nextLine(); // clear newline

                switch (input) {
                    case 1 -> nextDay();
                    case 2 -> addHabitMenu();
                    case 3 -> editHabit();
                    case 4 -> deleteHabit();
                    case 5 -> createCustomHabit();
                    case 6 -> history();
                    // case 7 -> achievement();
                    case 8 -> {
                        System.out.println("Logging out...");
                        currentUser = null;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                s.nextLine(); // clear invalid input
            }

        } while (input != 8);
    }

    // Helper method to print centered lines inside the box
    public static void printCenteredLine(String content, int boxWidth) {
        int leftPadding = (boxWidth - content.length()) / 2;
        int rightPadding = boxWidth - leftPadding - content.length();
        System.out.printf("║%" + (leftPadding + content.length()) + "s%" + rightPadding + "s║\n", content, "");
    }

    // DAY PROGRESSION
    public void nextDay() {
        try {
            // Create LocalDate from current values
            currentDate = LocalDate.of(year, month, day);
            
            // Add one day
            LocalDate nextDate = currentDate.plusDays(1);
            
            // Update values
            year = nextDate.getYear();
            month = nextDate.getMonthValue();
            day = nextDate.getDayOfMonth();
            
            System.out.println("Progressed to the next day: " + nextDate);
        } catch (DateTimeException e) {
            System.out.println("Error progressing to the next day. Current date might be invalid.");
        }
    }

    // HABIT MANAGEMENT
    public void addHabitMenu() {
        int choice = 0;

        System.out.println("\n=== ADD NEW HABIT ===");
        System.out.println("Choose a habit type:");

        // 1-3: Built-in habits
        System.out.println("[1] Calories Tracker");
        System.out.println("[2] Water Intake Habit");
        System.out.println("[3] Sleep Habit");

        // Starting option number for custom templates
        int optionNumber = 4;

        // List custom habit templates dynamically
        ArrayList<CustomHabit> customTemplates = currentUser.getCustomHabitTemplates();
        for (int i = 0; i < customTemplates.size(); i++) {
            CustomHabit ch = customTemplates.get(i);
            System.out.printf("[%d] Custom: %s (Goal: %d)\n", optionNumber, ch.getName(), ch.getGoal());
            optionNumber++;
        }

        // Cancel option
        System.out.printf("[%d] Cancel\n", optionNumber);

        try {
            System.out.print("Option: ");
            choice = s.nextInt();
            s.nextLine(); // clear newline

            int builtInCount = 3;
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

                // Ask for progress
                System.out.printf("You selected: %s\n", template.getName());
                System.out.printf("Goal: %d\n", template.getGoal());
                System.out.print("Enter today's progress: ");
                int progress = s.nextInt();
                s.nextLine();

                // Create a new CustomHabit and set progress
                CustomHabit newCustomHabit = new CustomHabit(template.getName(), template.getDescription(), template.getGoal());
                newCustomHabit.setProgress(progress);
                currentUser.addHabit(newCustomHabit);

                // Show result
                System.out.println("✅ Custom Habit '" + newCustomHabit.getName() + "' added successfully!");
                System.out.printf("Progress: %d / %d\n", newCustomHabit.getProgress(), newCustomHabit.getGoal());
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

    public void editHabit() {}

    public void deleteHabit() {}

    public void createCustomHabit() {
        System.out.println("\n=== CREATE CUSTOM HABIT ===");

        System.out.print("Enter name of your habit: ");
        String name = s.nextLine();

        System.out.print("Enter description: ");
        String description = s.nextLine();

        System.out.print("Enter your goal (e.g., 10 repetitions, 100 pages): ");
        int goal = 0;

        try {
            goal = s.nextInt();
            s.nextLine(); // clear buffer
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Goal must be a number.");
            s.nextLine(); // clear invalid input
            return; // exit early
        }

        // Create the custom habit
        CustomHabit customHabit = new CustomHabit(name, description, goal);

        // (Optional) Add to a list of habits if you maintain one
        currentUser.addCustomHabitTemplate(customHabit); // Assuming habitList is a List<Habit> you maintain

        System.out.println("✅ Custom habit added successfully!");
        customHabit.printDetails(); // Show details
        menu();
    }

// <<<<<<< HEAD
//     // Create the custom habit
//     CustomHabit customHabit = new CustomHabit(name, description, goal);

//     // (Optional) Add to a list of habits if you maintain one
//     currentUser.addCustomHabitTemplate(customHabit); // Assuming habitList is a List<Habit> you maintain

//     System.out.println("✅ Custom habit added successfully!");
//     customHabit.printDetails(); // Show details
// }

//     // HISTORY & ACHIEVEMENTS

    public void history() {}

    // public void achievement() {
    //     System.out.println("=== ACHIEVEMENTS 🏆 ===");
        
    //     if(habitCount == 0){
    //         System.out.println("You have no achievements yet. Keep tracking your habits!");
    //     } else if (habitCount == 1){
    //         System.out.println("");
            
    //     }else {
    //         for (int i = 0; i < habitCount && i < achievements.size(); i++) {
    //             System.out.println("- " + achievements.get(i).getTitle());
    //             System.out.println("  " + achievements.get(i).getDescription());
    //         }
    //     }
    // }

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

        int dailyCalorieGoal = switch (choice) {
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
    
        // if (caloriesTracker.goalMet()) {
        //     currentUser.getAchievements().add(
        //         new Achievement("Calorie Goal", "You met your calorie goal today!")
        //     );

        // if(calorieGoal == caloriesTracker.getCaloriesConsumed()) {
        //     habitCount++;
        //     User.getAchievements().add(new Achievement());
        // }
    }
    

    public void sleepHabit() {
        System.out.println("=== SLEEP HABIT 💤 ===");
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
        int targetSleepDuration = switch (target) {
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
        if(sleepDuration >= sleepHabit.getTargetSleepDuration()) {
            habitCount++;
        }
    }

    public void ExerciseHabit() {
        System.out.println("=== EXERCISE HABIT 🚴🏻 ===");
        System.out.println("Enter Your Exercise Duration Target");
        System.out.println("""
                1. 30 minutes
                2. 1 hour
                3. 1.5 hours
                4. 2 hours
                5. Other
                """);
        System.out.print("Select a target: ");
        int target = s.nextInt();
        s.nextLine();

        int targetduration= switch (target) {
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
        
        System.out.print("Enter your exercise duration (in minutes): ");
        int duration = s.nextInt();
        System.out.println ("Enter your exercise type (e.g., Cardio, Strength): ");
        String type = s.nextLine();
        // System.out.print("Enter your exercise frequency (e.g., Daily, Weekly): ");
        // String exerciseFrequency = s.nextLine();

        ExerciseHabit exerciseHabit = new ExerciseHabit(
            targetduration,
            duration, 
            type
        );

        System.out.println("Your Exercise Habit :");
        System.out.println("Target: " + exerciseHabit.getTargetduration() + " minutes");
        System.out.println("Duration: " + exerciseHabit.getDuration() + " minutes");
        System.out.println("Type: " + exerciseHabit.getType());
        System.out.println("Habit added successfully!");

        // if(exerciseDuration >= exerciseHabit.getTargetduration()) {
        //     habitCount++;
        // }
    }

    public void addWaterIntakeHabit() {
        System.out.println("\n=== WATER INTAKE HABIT 🥛 ===");
        System.out.print("Enter your daily water intake goal (in liters) ");
        System.out.println("""

                1. 1 liters
                2. 2 liters
                3. 3 liters
                """);
        System.out.println("Option: ");
        int goals = s.nextInt();
        s.nextLine();

        int goal = switch (goals) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> {
                System.out.print("Invalid choice. Please enter your custom goal (in liters): ");
                yield s.nextInt();
            }
        };

        System.out.print("Enter your current water intake (in liters): ");
        int waterIntake = s.nextInt();
        s.nextLine();

        WaterIntakeHabit waterHabit = new WaterIntakeHabit(
            "Water Intake", 
            "Track your daily water intake", 
            goal
        );

        waterHabit.logWaterIntake(waterIntake);
        currentUser.addHabit(waterHabit);


        System.out.println("✅ Water Intake Habit added successfully.");
        waterHabit.printDetails();

        // if(currentIntake >= waterHabit.getGoal()) {
        //     habitCount++;
        // }
        
    }

    
}
