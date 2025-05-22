import java.util.InputMismatchException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import Model.CaloriesTracker;
import Model.Habit;
import Model.User;

public class Main {
    private ArrayList<User> users = new ArrayList<>();
    private Scanner s = new Scanner(System.in);
    private LocalDate currentDate;
    private User currentUser = null;
    private int day;
    private int month;
    private int year; 

    // STARTING SCREEN
    public void start() {
        int input = 0;

        // ASK CURRENT DATE
        while (true) {
        try {
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
        System.out.println("=== SIGN UP ===");
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
        System.out.println("=== LOG IN ===");
        System.out.print("Enter username: ");
        String username = s.nextLine();

        System.out.print("Enter password: ");
        String password = s.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Welcome back, " + username + "!");
                menu(); // go to the main habit menu
                return;
            }
        }
        System.out.println("Invalid username or password. Try again.");
    }

    public void exit() {
        System.out.println("Thank you for using Habitica! Stay healthy 🌱");
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
                    ========================================
                    [1] Next Day
                    [2] Add Habit
                    [3] Edit Habit
                    [4] Delete Habit
                    [5] Create Custom Habit
                    [6] History
                    [7] Achievements
                    [8] Log Out""");

            System.out.print("Option: ");

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
                    case 7 -> achievement();
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

        } while (input != 5);
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

        System.out.println("""
            === ADD NEW HABIT ===
            Choose a habit type:
            [1] Calories Tracker
            [2] Water Intake Habit
            [3] Sleep Habit
        """);

        // ADD: loop through custom habits, and add 'cancel' option

        try {
            System.out.print("Option: ");
            choice = s.nextInt();
            s.nextLine();

            switch (choice) {
                case 1 -> addCaloriesTracker();
                // case 2 -> addWaterIntakeHabit();
                // case 3 -> addSleepHabit();
                case 4 -> createCustomHabit();
                // case 5 -> addFromCustomTemplates();
                case 6 -> System.out.println("Cancelled.");
                default -> System.out.println("Invalid choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            s.nextLine(); // clear buffer
        }
    }

    // ADD HABIT MENU FOR EACH HABITs
    private void addCaloriesTracker() {
        // Check if user already has a CaloriesTracker
        for (Habit habit : currentUser.getHabits()) {
            if (habit instanceof CaloriesTracker) {
                System.out.println("⚠️ You already have a Calories Tracker.");
                return;
            } else {
                try {
                    System.out.print("Enter your daily calorie goal: ");
                    int goal = s.nextInt();
                    s.nextLine();

                    CaloriesTracker ct = new CaloriesTracker(
                        "Calories Tracker", 
                        "Track your daily calorie intake", // CHECK THE NEEDED PARAMS AGAIN PLS!!
                        1, 1, goal
                    );

                    currentUser.addHabit(ct);
                    System.out.println("✅ Calories Tracker added successfully.");
                } catch (InputMismatchException e) {
                    System.out.println("❌ Please enter a valid number for calorie goal.");
                    s.nextLine(); // clear buffer
                }
            }
        }
    }

    public void editHabit() {}

    public void deleteHabit() {}

    public void createCustomHabit() {}

    public void history() {}

    public void achievement() {}
}
