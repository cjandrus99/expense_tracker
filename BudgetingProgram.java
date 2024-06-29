import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class BudgetingProgram {
    private static final String FILE_NAME = "budget_data.txt"; // File to store budget data
    private static double totalFunds = 0; // Running total of available funds

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main program loop
        while (running) {
            // Display menu
            System.out.println("Expense Tracker Menu:");
            System.out.println("1. Add Income");
            System.out.println("2. View Previous Income");
            System.out.println("3. Add Expense");
            System.out.println("4. View Previous Expenses");
            System.out.println("5. Quit");
            System.out.println("Total Available Funds: $" + totalFunds);
            System.out.print("Choose an option: ");

            // Get user choice
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Handle user choice
            switch (choice) {
                case 1:
                    addFunds(scanner);
                    break;
                case 2:
                    viewEntries("addition");
                    break;
                case 3:
                    addExpense(scanner);
                    break;
                case 4:
                    viewEntries("expense");
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close(); // Close scanner
    }

    // Method to add funds
    private static void addFunds(Scanner scanner) {
        double amount = getValidatedAmount(scanner, "Enter amount to add: ");
        if (amount < 0) return;

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        totalFunds += amount; // Update total funds
        saveEntry("addition", amount, description); // Save entry to file
    }

    // Method to add an expense
    private static void addExpense(Scanner scanner) {
        double amount = getValidatedAmount(scanner, "Enter expense amount: ");
        if (amount < 0) return;

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        if (amount > totalFunds) {
            System.out.println("Insufficient funds. Expense not added.");
        } else {
            totalFunds -= amount; // Update total funds
            saveEntry("expense", amount, description); // Save entry to file
        }
    }

    // Method to get and validate amount input
    private static double getValidatedAmount(Scanner scanner, String prompt) {
        double amount = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (Pattern.matches("\\d+(\\.\\d{1,2})?", input)) { // Validate input
                amount = Double.parseDouble(input);
                valid = true;
            } else {
                System.out.println("Invalid amount. Please enter a number with up to two decimal places.");
            }
        }
        return amount;
    }

    // Method to save entry to file
    private static void saveEntry(String type, double amount, String description) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            writer.write(type + "," + amount + "," + description + "," + timestamp);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Method to view entries from file
    private static void viewEntries(String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(type)) {
                    System.out.println("Amount: $" + parts[1] + ", Description: " + parts[2] + ", Date: " + parts[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
}
