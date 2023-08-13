/* ATM Interface */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String pin;
    protected double balance;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount);
        transactionHistory.add(transaction);
    }
}

public class ATM_interface {
    private static User currentUser;
    private static List<User> users;

    public static void main(String[] args) {
        initializeUsers();

        System.out.println("\n\n        ****   ATM Interface   ****");
        System.out.println("------------------------------------------------------------");
        System.out.println("      Demo User Accounts Credentials:");
        System.out.println("      UserId: 123456 , Password: 1234 , Balance: 5000");
        System.out.println("      UserId: 987654 , Password: 4321 , Balance: 10000");
        System.out.println("------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        System.out.print("\n** Successfully Login **\n");

        if (authenticateUser(userId, pin)) {
            displayMainMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            while (choice != 5) {
                switch (choice) {
                    case 1:
                        displayTransactionHistory();
                        break;
                    case 2:
                        withdrawMoney(scanner);
                        break;
                    case 3:
                        depositMoney(scanner);
                        break;
                    case 4:
                        transferMoney(scanner);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                displayMainMenu();
                choice = Integer.parseInt(scanner.nextLine());
            }

            System.out.println("\n*** Thank you for using the ATM. Goodbye! ***\n");
        } else {
            System.out.println("Invalid Credentials. Exiting...");
        }
    }

    private static void initializeUsers() {
        users = new ArrayList<>();
        User user1 = new User("123456", "1234", 5000);
        User user2 = new User("987654", "4321", 10000);
        users.add(user1);
        users.add(user2);
    }

    private static boolean authenticateUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private static void displayMainMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("1. Transactions History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit\n");
        System.out.print("Enter your choice: ");
    }

    private static void displayTransactionHistory() {
        List<Transaction> transactionHistory = currentUser.getTransactionHistory();

        System.out.println("\n---------------------------------");
        System.out.println("Transaction History:");
        for (Transaction transaction : transactionHistory) {
            System.out.println("Type: " + transaction.getType() + ", Amount: " + transaction.getAmount());
        }
        System.out.println("---------------------------------");
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.print("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (amount > 0 && amount <= currentUser.getBalance()) {
            currentUser.addTransaction("Withdraw", amount);
            currentUser.balance -= amount;
            System.out.println("\n---------------------------------");
            System.out.println("Actual Balance: " + (currentUser.getBalance() + amount));
            System.out.println("Amount withdrawn: " + amount);
            System.out.println("Remaining balance: " + currentUser.getBalance());
            System.out.println("---------------------------------");
        } else {
            System.out.println("Sorry, Insufficient Balance.");
            System.out.println("You can withdraw up to " + currentUser.getBalance() + " only.");
        }
    }

    private static void depositMoney(Scanner scanner) {
        System.out.print("Enter the amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (amount > 0) {
            currentUser.addTransaction("Deposit", amount);
            currentUser.balance += amount;
            System.out.println("\n---------------------------------");
            System.out.println("Actual Balance: " + (currentUser.getBalance() - amount));
            System.out.println("Amount deposited: " + amount);
            System.out.println("Updated balance: " + currentUser.getBalance());
            System.out.println("---------------------------------");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    private static void transferMoney(Scanner scanner) {
        System.out.print("Enter the recipient's User ID: ");
        String recipientId = scanner.nextLine();

        User recipient = null;
        for (User user : users) {
            if (user.getUserId().equals(recipientId)) {
                recipient = user;
                break;
            }
        }

        if (recipient != null) {
            System.out.print("Enter the amount to transfer: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.addTransaction("Transfer to " + recipientId, amount);
                currentUser.balance -= amount;
                recipient.addTransaction("Transfer from " + currentUser.getUserId(), amount);
                recipient.balance += amount;
                System.out.println("\n---------------------------------");
                System.out.println("Transfer to: " + recipientId);
                System.out.println("Transfer from: " + currentUser.getUserId());
                System.out.println("\nActual Balance: " + (currentUser.getBalance() + amount));
                System.out.println("Amount transferred: " + amount);
                System.out.println("Remaining balance: " + currentUser.getBalance());
                System.out.println("---------------------------------");
            } else {
                System.out.println("Sorry, Insufficient Balance.");
                System.out.println("You can transfer up to " + currentUser.getBalance() + " only.");
            }
        } else {
            System.out.println("Recipient with User ID " + recipientId + " not found.");
        }
    }
}
