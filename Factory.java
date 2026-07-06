import java.util.Scanner;

public class Factory {
    public static void main(String[] args) {
        int choice, testChoice, flag = 0;
        Regular regular = null;
        Scanner keyboard = new Scanner(System.in);
        do{
            System.out.println("\n===== Vending Machine Factory =====");
            System.out.println("Welcome to the Vending Machine Factory!");
            System.out.println("Please select what you would like to do:");
            System.out.println("1. Create a new Vending Machine");
            System.out.println("2. Test an existing Vending Machine");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("\nCreating a new Vending Machine...");
                    regular = new Regular();
                    flag = 1;
                    System.out.println("Vending Machine created" +
                    " successfully!");
                    System.out.println("Press enter to continue...");
                    keyboard.nextLine();
                    break;
                case 2:
                    if(flag == 0){
                        System.out.println("\nNo Vending Machine has been"
                        +" created yet. Please create one first.");
                        System.out.println("Press enter to continue...");
                        keyboard.nextLine();
                    } else {
                        do{
                            System.out.println("\nWhat features would you like"
                            +" to test?");
                            System.out.println("1. Vending Features");
                            System.out.println("2. Management Features");
                            System.out.println("3. Return to Main Menu");
                            System.out.print("Enter your choice: ");
                            testChoice = keyboard.nextInt();
                            keyboard.nextLine();
                            switch (testChoice) {
                                case 1:
                                    regular.vending();
                                    break;
                                case 2:
                                    regular.management();
                                    break;
                                case 3:
                                    System.out.println("\nReturning to"
                                    +" Main Menu...");
                                    break;
                                default:
                                    System.out.println("\nInvalid choice."
                                    +" Please try again.");
                            }
                        } while (testChoice != 3);
                    }
                    break;
                case 3:
                    System.out.println("\nDon't Forget to Clean the Vending "
                    + "Machine Before You Leave!\n");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
                }
        } while (choice != 3);
        keyboard.close();
    }
}
