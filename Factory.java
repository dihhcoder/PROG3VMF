import java.util.Scanner;

public class Factory {
    public static void main(String[] args) {
        int choice, testChoice, flag = 0;
        Scanner keyboard = new Scanner(System.in);
        do{
            System.out.println("Welcome to the Vending Machine Factory!");
            System.out.println("Please select what you would like to do:");
            System.out.println("1. Create a new Vending Machine");
            System.out.println("2. Test an existing Vending Machine");
            System.out.println("3. Exit");
            choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Creating a new Vending Machine...");
                    Regular regular = new Regular();
                    flag = 1;
                    break;
                case 2:
                    if(flag == 0){
                        System.out.println("No Vending Machine has been created yet. Please create one first.");
                    } else {
                        do{
                            System.out.println("Testing the existing Vending Machine...");
                            System.out.println("What features would you like to test?");
                            System.out.println("1. Vending Features");
                            System.out.println("2. Management Features");
                            System.out.println("3. Return to Main Menu");
                            testChoice = keyboard.nextInt();
                            keyboard.nextLine();
                            switch (testChoice) {
                                case 1:
                                    System.out.println("Testing Vending Features...");
                                    // Add code to test vending features here
                                    break;
                                case 2:
                                    System.out.println("Testing Management Features...");
                                    // Add code to test management features here
                                    break;
                                case 3:
                                    System.out.println("Returning to Main Menu...");
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        } while (testChoice != 3);
                    }
                    break;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                }
        } while (choice != 3);
        keyboard.close();
    }
}
