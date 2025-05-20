import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Scanner;
import Model.User;

public class Main {
    private ArrayList<User> users = new ArrayList<>();
    private Scanner s = new Scanner(System.in);
    private User currentUser = null;

    // STARTING SCREEN
    public void start() {
        int input = 0;

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
        System.out.println("""
                ========================================
                            🌱 HABITICA 🌱
                    Health Habit Tracker Application
                ========================================

                [1] Add Habit
                [2] View Habit Log
                [3] Edit Habit
                [4] Delete Habit
                [5] Log Out

                """);
        
        System.out.print("Option: ");
        // Switch case
    }

    // HABIT MANAGEMENT
    public void addHabitMenu() {}

    public void viewHabitLog() {}

    public void editHabit() {}

    public void deleteHabit() {}

    

}
