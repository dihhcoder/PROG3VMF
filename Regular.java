import java.util.ArrayList;
import java.util.Scanner;
public class Regular {
    
    private CashStorage cashStorage;
    private ArrayList<Slots> slotList;
    private CashStorage cashInserted;

    public Regular() {
        this.cashStorage = new CashStorage(new ArrayList<Denomination>());
        this.slotList = new ArrayList<Slots>();
        for (int i = 0; i < 8; i++) {
            this.slotList.add(new Slots(null, 10, 0.0));
        }
        this.cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    public void vending() {
        Scanner vendingScan = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== VENDING INTERFACE =====");
            System.out.printf("Current Inserted Balance: P%.2f%n", checkCash(0));
            System.out.println("1. View Available Items");
            System.out.println("2. Insert Coins or Bills");
            System.out.println("3. Purchase an Item");
            System.out.println("4. Cancel And/Or Return");
            System.out.print("Select an option: ");
            choice = vendingScan.nextInt();
            vendingScan.nextLine();

            switch (choice) {
                case 1:
                    displayItems(); 
                    break;

                case 2:
                    System.out.println("\nValid Denominations: 0.05, 0.10, 0.25, 1, 5, 10, 20, 50, 100, 200, 500, 1000");
                    System.out.print("Enter Denomination Value: ");
                    double value = vendingScan.nextDouble();
                    
                    if (!validDenomination(value)) {
                        System.out.println("Invalid currency denomination. Machine rejected the payment.");
                        vendingScan.nextLine(); 
                    } else {
                        System.out.print("Enter Quantity: ");
                        int qty = vendingScan.nextInt();
                        vendingScan.nextLine(); 
                        recieveCash(new Denomination(value, qty));
                    }
                    break;

                case 3:
                    System.out.print("\nEnter the exact name of the item you want to buy: ");
                    String targetName = vendingScan.nextLine();
                    
                    Slots selectedSlot = slotFinder(targetName);

                    if (selectedSlot == null) {
                        System.out.println("That item does not exist in this machine.");
                    } else if (selectedSlot.getCurrentStock() == 0) {
                        System.out.println("Sorry, that item is completely out of stock.");
                    } else {
                        double itemPrice = selectedSlot.getPrice();
                        double totalInserted = checkCash(0);

                        if (totalInserted < itemPrice) {
                            System.out.printf("Insufficient funds. Item costs P%.2f but you only inserted P%.2f.%n", itemPrice, totalInserted);
                        } else {
                            double changeOwed = totalInserted - itemPrice;
                            changeOwed = Math.round(changeOwed * 100.0) / 100.0; 
                            
                            if (changeOwed > 0 && !checkChangeAvailability(changeOwed)) {
                                System.out.println("\nTransaction Cancelled: Machine cannot provide exact change.");
                                cancelTransaction();
                                choice = 4; 
                            } else {
                                Item purchasedItem = dispenseItem(targetName);
                                if (purchasedItem != null) {
                                    
                                    System.out.println("Calorie Content: " + purchasedItem.getCalories() + " calories");
                                    
                                    transferCashToStorage(); 

                                    if (changeOwed > 0) {
                                        giveChange(changeOwed);
                                    } else {
                                        System.out.println("Exact amount paid. No change required.");
                                    }
                                    choice = 4; 
                                }
                            }
                        }
                    }
                    break;

                case 4:
                    cancelTransaction(); 
                    System.out.println("Returning to main control system...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    public void management() {
        Scanner manageScan = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== OPERATOR MANAGEMENT DASHBOARD =====");
            System.out.println("1. Configure & Restock Slots");
            System.out.println("2. Replenish / Collect Vault Cash");
            System.out.println("3. Print Sales Audit & Transaction Summary");
            System.out.println("4. Exit Management Mode");
            System.out.print("Enter your choice: ");
            choice = manageScan.nextInt();
            manageScan.nextLine();

            switch (choice) {
                case 1:
                    selectSlot();
                    break;
                case 2:
                    manageCashStorage();
                    break;
                case 3:
                    printTransactionSummary();
                    break;
                case 4:
                    System.out.println("Exiting Maintenance Services Mode.");
                    break;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        } while (choice != 4);
    }

    public void displayItems() {
        System.out.println("Displaying available items:");
        for (int i = 0; i < slotList.size(); i++) {
            if (slotList.get(i).getItem() == null) {
                System.out.println((i + 1) + ": Empty Slot");
            } else {
                System.out.println((i + 1) + ". Item: " + slotList.get(i).getItem().getName() + 
                                    "           Calorie Count: " + slotList.get(i).getItem().getCalories() + 
                                    "           Price: P" + slotList.get(i).getPrice() +
                                    "           Stock: " + slotList.get(i).getCurrentStock());
            }
        }
    }

    public void recieveCash(Denomination cash) {
        if (cash.getValue() > 0 && cash.getQuantity() > 0) {
            boolean check = false;
            for (int i = 0; i < cashInserted.getCashList().size() && !check; i++) {
                Denomination temp = cashInserted.getCashList().get(i);
                if (temp.getValue() == cash.getValue()) {
                    int newQuantity = temp.getQuantity() + cash.getQuantity();
                    cashInserted.getCashList().set(i, new Denomination(temp.getValue(), newQuantity));
                    check = true;
                }
            }
        } else {
            System.out.println("Invalid cash denomination or quantity.");
        }
    }

    public double checkCash(double price) {
        double totalCash = 0;
        for (Denomination d : cashInserted.getCashList()) {
            totalCash += d.getValue() * d.getQuantity();
        }
        if (totalCash >= price) {
            return totalCash;
        } else {
            System.out.println("Insufficient cash inserted.");
            return 0;
        }
    }

    public void returnCash() {
        System.out.println("Returning inserted cash...");
        for (Denomination d : cashInserted.getCashList()) {
            if (d.getQuantity() > 0) {
                System.out.println("Returning " + d.getQuantity() + " of P" + d.getValue());
            }
        }
        cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    public void transferCashToStorage() {
        for (Denomination d : cashInserted.getCashList()) {
            if (d.getQuantity() > 0) {
                cashStorage.addCash(d);
            }
        }
        cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    public boolean checkChangeAvailability(double changeAmount) {
        CashStorage tempCashList = new CashStorage(new ArrayList<Denomination>());
        for (Denomination d : cashStorage.getCashList()) {
            tempCashList.addCash(new Denomination(d.getValue(), d.getQuantity()));
        }
        for (int i = tempCashList.getCashList().size() - 1; i >= 0; i--) {
            Denomination temp = tempCashList.getCashList().get(i);
            while (changeAmount >= temp.getValue() && temp.getQuantity() > 0) {
                changeAmount -= temp.getValue();
                changeAmount = Math.round(changeAmount * 100.0) / 100.0;
                tempCashList.removeCash(new Denomination(temp.getValue(), 1));
                temp = tempCashList.getCashList().get(i);
            }
        }
        if (changeAmount == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void giveChange(double changeAmount) {
        System.out.println("Calculating change for P" + changeAmount);
        CashStorage changeDispensed = new CashStorage(new ArrayList<Denomination>());
        for (int i = cashStorage.getCashList().size() - 1; i >= 0; i--) {
            Denomination temp = cashStorage.getCashList().get(i);
            while (changeAmount >= temp.getValue() && temp.getQuantity() > 0) {
                changeAmount -= temp.getValue();
                changeAmount = Math.round(changeAmount * 100.0) / 100.0;
                cashStorage.removeCash(new Denomination(temp.getValue(), 1));
                changeDispensed.addCash(new Denomination(temp.getValue(), 1));
                temp = cashStorage.getCashList().get(i);
            }
        }
        System.out.println("Change provided successfully.");
        System.out.println("Returning change to customer...");
        for (Denomination d : changeDispensed.getCashList()) {
            if (d.getQuantity() > 0) {
                System.out.println("Returning " + d.getQuantity() + " of P" + d.getValue());
            }
        }
    }

    public Item dispenseItem(String name) {
        boolean itemFound = false;
        Item item = null;
        for (int i = 0; i < slotList.size() && !itemFound; i++) {
            Slots s = slotList.get(i);
            if (s.getItem() != null && s.getItem().getName().equalsIgnoreCase(name)) {
                itemFound = true;
                if (!s.getItemList().isEmpty()) {
                    item = s.getItemList().remove(0);
                    s.incrementSold();
                    System.out.println("Dispensing item: " + item.getName());
                }
            }
        }
        if (!itemFound) {
            System.out.println("Item not found.");
        } else if (item == null) {
            System.out.println("Item is out of stock.");
        }
        return item;
    }

    public Slots slotFinder(String name) {
        for (Slots s : slotList) {
            if (s.getItem() != null && s.getItem().getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public boolean validDenomination(double value) {
        for (Denomination d : cashStorage.getCashList()) {
            if (d.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public void cancelTransaction() {
        System.out.println("Transaction cancelled. Returning inserted cash.");
        returnCash();
    }

    public void selectSlot() {
        System.out.println("Managing slots...");
        Scanner slot = new Scanner(System.in);
        int slotChoice = -1;
        do {
            displayItems();
            System.out.println("Select a slot number to manage (1-8) or 0 to return:");
            slotChoice = slot.nextInt();
            slot.nextLine();
            if (slotChoice < 1 || slotChoice > 8) {
                System.out.println("Invalid slot number. Please try again.");
            } else if (slotChoice == 0) {
                System.out.println("Returning to previous menu.");
            } else {
                manageSlot(slotChoice - 1);
            }
        } while (slotChoice != 0);
    }

    public void manageSlot(int slotIndex) {
        Scanner manaSlot = new Scanner(System.in);
        Slots slot = slotList.get(slotIndex);
        System.out.println("Managing slot for item: " + (slot.getItem() != null ? slot.getItem().getName() : "Empty Slot"));
        System.out.println("1. Assign Item ");
        System.out.println("2. Add Item");
        System.out.println("3. Remove Assigned Item");
        System.out.println("4. Change Item Price");
        System.out.println("5. Return to previous menu");
        int choice = manaSlot.nextInt();
        manaSlot.nextLine();
        switch (choice) {
            case 1:
                int quantity = 0;
                System.out.println("Enter item name to assign:");
                String itemName = manaSlot.nextLine();
                System.out.println("Enter item calorie count:");
                int calories = manaSlot.nextInt();
                manaSlot.nextLine();
                System.out.println("Enter item price:");
                double price = manaSlot.nextDouble();
                manaSlot.nextLine();
                do {
                    System.out.println("Enter quantity of items to add: (Maximum 10)");
                    quantity = manaSlot.nextInt();
                    manaSlot.nextLine();
                } while (quantity < 1 || quantity > 10);
                slot.setItem(new Item(itemName, calories));
                slot.setPrice(price);
                for (int i = 0; i < quantity; i++) {
                    slot.addItem(new Item(itemName, calories));
                }
                System.out.println("Item assigned successfully.");
                break;
            case 2:
                if (slot.getItem() == null) {
                    System.out.println("No item assigned to this slot. Please assign an item first.");
                } else {
                    int quantityToAdd = 0;
                    int currentStock = 0;
                    for (int i = 0; i < slotList.size(); i++) {
                        if (slotList.get(i).getItem() != null && slotList.get(i).getItem().getName().equalsIgnoreCase(slot.getItem().getName())) {
                            currentStock++;
                        }
                    }
                    do {
                    System.out.println("Enter quantity of items to add: (Total stock should not exceed 10)");
                    System.out.println("Current stock for '" + slot.getItem().getName() + "': " + currentStock);
                        quantityToAdd = manaSlot.nextInt();
                        manaSlot.nextLine();
                    } while (quantityToAdd + currentStock < 1 || quantityToAdd + currentStock > 10);
                    for (int i = 0; i < quantityToAdd; i++) {
                        slot.addItem(new Item(slot.getItem().getName(), slot.getItem().getCalories()));
                    }
                    System.out.println("Items added successfully.");
                }
                break;
            case 3:
                if (slot.getItem() == null) {
                    System.out.println("No item assigned to this slot.");
                } else {
                    slot.setItem(null);
                    slot.getItemList().clear();
                    slot.setPrice(0.0);
                    System.out.println("Assigned item removed successfully.");
                }
                break;
            case 4:
                if (slot.getItem() == null) {
                    System.out.println("No item assigned to this slot. Please assign an item first.");
                } else {
                    System.out.println("Enter new price for the item:");
                    double newPrice = manaSlot.nextDouble();
                    manaSlot.nextLine();
                    slot.setPrice(newPrice);
                    System.out.println("Item price updated successfully.");
                }
                break;
            case 5:
                System.out.println("Returning to previous menu.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void manageCashStorage() {
        int choice = -1;
        do{
            System.out.println("Managing cash storage...");
            Scanner cashStor = new Scanner(System.in);
            System.out.println("1. Add Cash");
            System.out.println("2. Remove Cash");
            System.out.println("3. Return to previous menu");
            choice = cashStor.nextInt();
            cashStor.nextLine();
            switch (choice) {
                case 1:
                    boolean validInput = false;
                    double addValue = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        addValue = cashStor.nextDouble();
                        cashStor.nextLine();
                        if (validDenomination(addValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid denomination. Please try again.");
                        }
                    } while (!validInput);
                    int addQuantity = 0;
                    validInput = false;
                    do{
                        System.out.println("Enter quantity to add:");
                        addQuantity = cashStor.nextInt();
                        cashStor.nextLine();
                        if (addQuantity > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid quantity. Please enter a positive number.");
                            validInput = false;
                        }
                    } while (!validInput);
                    cashStorage.addCash(new Denomination(addValue, addQuantity));
                    System.out.println("Cash added successfully.");
                    break;
                case 2:
                    validInput = false;
                    double removeValue = 0;
                    int removeQuantity = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        removeValue = cashStor.nextDouble();
                        cashStor.nextLine();
                        if (validDenomination(removeValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid denomination. Please try again.");
                        }
                    } while (!validInput);
                    validInput = false;
                    do{
                        System.out.println("Enter quantity to remove:");
                        removeQuantity = cashStor.nextInt();
                        cashStor.nextLine();
                        if (removeQuantity > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid quantity. Please enter a positive number.");
                            validInput = false;
                        }
                    } while (!validInput);
                    cashStorage.removeCash(new Denomination(removeValue, removeQuantity));
                    System.out.println("Cash removed successfully.");
                    break;
                case 3:
                    System.out.println("Returning to previous menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                }
        } while(choice != 3); 
    }

    public void printTransactionSummary() {
        System.out.println("Transaction Summary:");
        System.out.println("Items Sold:");
        double totalSalesRevenue = 0;
        System.out.printf("%-4s | %-15s | %-7s | %-12s | %-10s | %-6s | %-9s%n",
            "Slot", "Item Name", "Price", "Starting Stock", "Ending Stock", "Sold", "Revenue");
        System.out.println("---------------------------------------------------------------------------------");
        
        for (int i = 0; i < slotList.size(); i++) {
            Slots s = slotList.get(i);
            if (s.getItem() != null) {
                double itemRevenue = s.getSold() * s.getPrice();
                totalSalesRevenue += itemRevenue;
                
                System.out.printf(" [%d]  | %-15s | P%-6.2f | %-14d | %-12d | %-4d | P%.2f%n",
                    (i + 1), s.getItem().getName(), s.getPrice(), s.getBeforeStock(), s.getCurrentStock(), s.getSold(), itemRevenue);
            } else {
                System.out.printf(" [%d]  | %-15s | %-6s | %-14s | %-12s | %-4s | %-9s%n",
                    (i + 1), "           N/A ", "-", "-", "-", "-", "-");
            }
        }
        System.out.println("Current Cash Audit:");
        double totalVaultBalance = 0;
        for (Denomination d : cashStorage.getCashList()) {
            double lineTotal = d.getValue() * d.getQuantity();
            totalVaultBalance += lineTotal;
            if (d.getQuantity() > 0) {
                System.out.printf("  - Denomination: P%-7.2f | Count: %-4d | Subtotal: P%.2f%n", 
                    d.getValue(), d.getQuantity(), lineTotal);
            }
        }
        System.out.printf("Total Cash In Machine: P%.2f%n%n", totalVaultBalance);
        System.out.printf("Revenue Collected: P%.2f%n", totalSalesRevenue);
    }
}
