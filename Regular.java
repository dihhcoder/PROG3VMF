import java.util.ArrayList;
import java.util.Scanner;

/**
 * Constructor class for a Regular Vending Machine
 * @author John Kendrick R. Constantino
 */
public class Regular {
    
    private CashStorage cashStorage;
    private ArrayList<Slots> slotList;
    private CashStorage cashInserted;

    /**
     * Creates a Regular vending Machine
     */
    public Regular() {
        this.cashStorage = new CashStorage(new ArrayList<Denomination>());
        this.slotList = new ArrayList<Slots>();
        for (int i = 0; i < 8; i++) {
            this.slotList.add(new Slots(null, 10, 0.0));
        }
        this.cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    /**
     * Displays and allows user controls to the vending features of the vending machine
     * @param keyboard the scanner used to get input
     */
    public void vending(Scanner keyboard) {
        int choice;
        do {
            System.out.println("\n===== Regular Vending Interface =====");
            System.out.printf("Current Balance: P%.2f%n", checkCash());
            System.out.println("1. View Available Items");
            System.out.println("2. Insert Coins or Bills");
            System.out.println("3. Purchase an Item");
            System.out.println("4. Cancel And/Or Return");
            System.out.print("Select an option: ");
            choice = keyboard.nextInt();
            keyboard.nextLine();

            switch (choice) {
                case 1:
                    displayItems(); 
                    break;

                case 2:
                    displayInsert(keyboard);
                    break;

                case 3:
                    displaySell(keyboard);
                    break;

                case 4:
                    cancelTransaction(); 
                    System.out.println("Returning to main menu.");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    /**
     * Displays and allows user controls to the management features of the vending machine
     * @param keyboard the scanner used to get input
     */
    public void management(Scanner keyboard) {
        int choice;
        do {
            System.out.println("\n===== Regular Vending Management =====");
            System.out.println("1. Configure & Restock Slots");
            System.out.println("2. Replenish / Collect Stored Cash");
            System.out.println("3. Print Sales Audit & Transaction Summary");
            System.out.println("4. Exit Management Mode");
            System.out.print("Enter your choice: ");
            choice = keyboard.nextInt();
            keyboard.nextLine();

            switch (choice) {
                case 1:
                    selectSlot(keyboard);
                    break;
                case 2:
                    manageCashStorage(keyboard);
                    break;
                case 3:
                    printTransactionSummary(keyboard);
                    break;
                case 4:
                    System.out.println("Exiting Maintenance Services Mode.");
                    break;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        } while (choice != 4);
    }

    /**
     * Displays and allows user control to insert denominations for purchase
     * @param keyboard the scanner used to get input
     */
    public void displayInsert(Scanner keyboard) {
        int qty;
        double value;
        System.out.println("\n===== Insert Coins or Bills =====");
        System.out.print("\nValid Denominations: ");
        for (Denomination d : cashInserted.getCashList()) {
            System.out.printf("P%.2f | ", d.getValue());
        }
        System.out.print("\nEnter Denomination Value: ");
        value = keyboard.nextDouble();
        keyboard.nextLine();

        if (!validDenomination(value)) {
            System.out.println("Invalid currency denomination. "
            + "Machine rejected the payment.");
        } else {
            System.out.print("Enter Quantity: ");
            qty = keyboard.nextInt();
            keyboard.nextLine(); 
            recieveCash(new Denomination(value, qty));
        }
    }

    /**
     * Displays and allows user control to make a purchase
     * Also handles the dispension of items and change
     * @param keyboard the scanner used to get input
     */
    public void displaySell(Scanner keyboard) {
        String targetName;
        Slots selectedSlot;
        double itemPrice, totalInserted, changeOwed;
        Item purchasedItem;
        System.out.println("\n===== Purchase an Item =====");
        System.out.print("\nEnter the exact name of the "
        + "item you want to buy: ");
        targetName = keyboard.nextLine();
        selectedSlot = slotFinder(targetName);
        if (selectedSlot == null) {
            System.out.println("That item does not exist in this machine.");
        } else if (selectedSlot.getCurrentStock() == 0) {
            System.out.println("Sorry, that item is completely out of stock.");
        } else {
            itemPrice = selectedSlot.getPrice();
            totalInserted = checkCash();
            if (totalInserted < itemPrice) {
                System.out.printf("Insufficient funds. "
                + "Item costs P%.2f but you only inserted P%.2f.%n",
                itemPrice, totalInserted);
            } else {
                changeOwed = totalInserted - itemPrice;
                changeOwed = Math.round(changeOwed * 100.0) / 100.0; 
                if (changeOwed > 0 && !checkChangeAvailability(changeOwed)) {
                    System.out.println("\nTransaction Cancelled: "
                    + "Machine cannot provide exact change.");
                    returnCash();
                } else {
                    purchasedItem = dispenseItem(targetName);
                    if (purchasedItem != null) {
                        System.out.println("Calorie Content: " +
                        purchasedItem.getCalories() + " calories");
                        transferCashToStorage(); 
                        if (changeOwed > 0) {
                            giveChange(changeOwed);
                        } else {
                            System.out.println("Exact amount paid. "
                            + "No change required.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays items on the slots on the vending machine
     */
    public void displayItems() {
        int i;
        System.out.println("Displaying available items:");
        for (i = 0; i < slotList.size(); i++) {
            if (slotList.get(i).getItem() == null) {
                System.out.println((i + 1) + ": Empty Slot");
            } else {
                System.out.printf("%d. Item: %-14s Calorie Count: %-6.2f"
                + " Price: P%-6.2f Stock: %d%n", (i + 1), 
                slotList.get(i).getItem().getName(), 
                slotList.get(i).getItem().getCalories(), 
                slotList.get(i).getPrice(), slotList.get(i).getCurrentStock());
            }
        }
    }

    /**
     * Recieves a denomination from the user.
     * @param cash the denomination inserted by the user
     * @see Denomination class for denomination attributes
     */
    public void recieveCash(Denomination cash) {
        int i, newQuantity;
        boolean check = false;
        Denomination temp;
        if (cash.getValue() > 0 && cash.getQuantity() > 0) {
            check = false;
            for (i = 0; i < cashInserted.getCashList().size()
                && !check; i++) {
                temp = cashInserted.getCashList().get(i);
                if (temp.getValue() == cash.getValue()) {
                    newQuantity = temp.getQuantity() + cash.getQuantity();
                    cashInserted.getCashList().set(i,
                        new Denomination(temp.getValue(), newQuantity));
                    check = true;
                }
            }
        } else {
            System.out.println("Invalid cash denomination or quantity.");
        }
    }

    /**
     * Checks the total value of the denominations inserted
     * @return total ammount of cash inserted 
     */
    public double checkCash() {
        double totalCash = 0;
        for (Denomination d : cashInserted.getCashList()) {
            totalCash += d.getValue() * d.getQuantity();
        }
        return totalCash;
    }

    /**
     * Returns the cash inserted by the user back
     */
    public void returnCash() {
        if(cashInserted.getCashList().isEmpty()){
            System.out.print("Returning cash...\n");
            for (Denomination d : cashInserted.getCashList()) {
                if (d.getQuantity() > 0) {
                    System.out.printf("Returning %d of P%.2f",
                    d.getQuantity(), d.getValue());
                }
            }
        }
        cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    /**
     * transfers the cash recieved after a successful transaction to 
     * the vending machine's cash storage
     */
    public void transferCashToStorage() {
        for (Denomination d : cashInserted.getCashList()) {
            if (d.getQuantity() > 0) {
                cashStorage.addCash(d);
            }
        }
        cashInserted = new CashStorage(new ArrayList<Denomination>());
    }

    /**
     * checks for change availability by simulating the transaction first
     * @param changeAmount the change needed to be provided by the vending machine
     * @return true if change is available, else false
     */
    public boolean checkChangeAvailability(double changeAmount) {
        int i;
        CashStorage tempCashList;
        Denomination temp;
        tempCashList = new CashStorage(new ArrayList<Denomination>());
        for (Denomination d : cashStorage.getCashList()) {
            tempCashList.addCash(new Denomination(d.getValue(),
            d.getQuantity()));
        }
        for (Denomination d : cashInserted.getCashList()) {
            tempCashList.addCash(new Denomination(d.getValue(),
            d.getQuantity()));
        }
        for (i = tempCashList.getCashList().size() - 1; i >= 0; i--) {
            temp = tempCashList.getCashList().get(i);
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

    /**
     * gives the change to the user after a successful transaction
     * @param changeAmount the change needed to be provided by the vending machine
     */
    public void giveChange(double changeAmount) {
        int i;
        Denomination temp;
        CashStorage changeDispensed;
        System.out.println("Calculating change for P" + changeAmount);
        changeDispensed = new CashStorage(new ArrayList<Denomination>());
        for (i = cashStorage.getCashList().size() - 1; i >= 0; i--) {
            temp = cashStorage.getCashList().get(i);
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
                System.out.println("Returning " + d.getQuantity() 
                + " of P" + d.getValue());
            }
        }
    }

    /**
     * dispenses the item chosen to be purchased by the user
     * @param name the name of the Item that is being purchased
     * @return Item to be dispensed if item exists and has stock, else null
     */
    public Item dispenseItem(String name) {
        boolean itemFound = false;
        Item item = null;
        int i;
        Slots selectedItemSlot;
        for (i = 0; i < slotList.size() && !itemFound; i++) {
            selectedItemSlot = slotList.get(i);
            if (selectedItemSlot.getItem() != null
            && selectedItemSlot.getItem().getName().equalsIgnoreCase(name)) {
                itemFound = true;
                if (!selectedItemSlot.getItemList().isEmpty()) {
                    item = selectedItemSlot.getItemList().remove(0);
                    selectedItemSlot.incrementSold();
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

    /**
     * A helper method used to find a slot that contains a specific item
     * @param name the name of the item used to search for its slot
     * @return Slot that contains the item if it exists, else null
     */
    public Slots slotFinder(String name) {
        Slots selectedSlot = null;
        for (Slots slot : slotList) {
            if (slot.getItem() != null
            && slot.getItem().getName().equalsIgnoreCase(name)) {
                selectedSlot = slot;
            }
        }
        return selectedSlot;
    }

    /**
     * Checks if the denomination that is being inserted is to be accepted by the machine
     * @param value the value of the denomination that the user is trying to insert
     * @return true if valid, else false
     */
    public boolean validDenomination(double value) {
        boolean isValid = false;
        for (Denomination d : cashStorage.getCashList()) {
            if (d.getValue() == value) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * Cancels the transaction and returns inserted cash
     */
    public void cancelTransaction() {
        System.out.println("Transaction cancelled.");
        returnCash();
    }

    /**
     * Displays and allows user to choose which slot to manage
     * @param keyboard the scanner used to get input
     */
    public void selectSlot(Scanner keyboard) {
        int slotChoice = -1;
        System.out.println("\n===== Configure & Restock Slots =====");
        do {
            displayItems();
            System.out.println("Select a slot number to manage (1-8)" 
            + " or 0 to return:");
            System.out.print("Enter your choice: ");
            slotChoice = keyboard.nextInt();
            keyboard.nextLine();
            if (slotChoice == 0) {
                System.out.println("Returning to previous menu.");
            } else if (slotChoice < 1 || slotChoice > 8) {
                System.out.println("Invalid slot number. Please try again.");
            } else {
                manageSlot(slotChoice - 1, keyboard);
            }
        } while (slotChoice != 0);
    }

    /**
     * Displays and allows the management of the selected slot
     * @param slotIndex the index of the slot to be managed
     * @param keyboard the scanner used to get input
     */
    public void manageSlot(int slotIndex, Scanner keyboard) {
        int choice = -1;
        int quantity, currentStock;
        int i, lastIndex;
        double calories, price;
        String itemName;
        Slots slot = slotList.get(slotIndex);
        do{
        System.out.println("\n===== Managing Slot =====");
        System.out.println("Slot with item: " 
        + (slot.getItem() != null ? slot.getItem().getName() : "N/A"));
        System.out.println("1. Assign Item ");
        System.out.println("2. Add Item (Restock)");
        System.out.println("3. Remove Items (Remove Stock)");
        System.out.println("4. Remove Assigned Item");
        System.out.println("5. Change Item Price");
        System.out.println("6. Return to previous menu");
        System.out.print("Enter your choice: ");
        choice = keyboard.nextInt();
        keyboard.nextLine();
        switch (choice) {
            case 1:
                if(slot.getItem() == null){
                    quantity = 0;
                    System.out.print("Enter item name to assign: ");
                    itemName = keyboard.nextLine();
                    System.out.print("Enter item calorie count: ");
                    calories = keyboard.nextDouble();
                    keyboard.nextLine();
                    System.out.print("Enter item price: ");
                    price = keyboard.nextDouble();
                    keyboard.nextLine();
                    do {
                        System.out.print("Enter quantity of items to add "
                        + "(Maximum 10): ");
                        quantity = keyboard.nextInt();
                        keyboard.nextLine();
                    } while (quantity < 1 || quantity > 10);
                    slot.setItem(new Item(itemName, calories));
                    slot.setPrice(price);
                    for (i = 0; i < quantity; i++) {
                        slot.addItem(new Item(itemName, calories));
                    }
                    System.out.println("Item assigned successfully.");
                } else {
                    System.out.println("Slot already has an item assigned");
                }
                break;
            case 2:
                if (slot.getItem() == null) {
                    System.out.println("\nNo item assigned to this slot."
                    + " Please assign an item first.");
                } 

                else if(slot.getCurrentStock() >= 10) {
                    System.out.println("\nSlot is already full. "
                    + "Cannot add more items.");
                }

                else {
                    quantity = 0;
                    currentStock = slot.getItemList().size();
                    do {
                    System.out.println("Enter quantity of items to add:"
                    + " (Total stock should not exceed 10)");
                    System.out.println("Current stock for '"
                     + slot.getItem().getName() + "': " + currentStock);
                    System.out.print("Enter quantity: ");
                        quantity = keyboard.nextInt();
                        keyboard.nextLine();
                    } while (quantity + currentStock > 10 || quantity < 0);
                    for (i = 0; i < quantity; i++) {
                        slot.addItem(new Item(slot.getItem().getName(),
                        slot.getItem().getCalories()));
                    }
                    System.out.println("Items added successfully.");
                    printSpecificSummary(slotIndex, keyboard);
                }
                break;
            case 3:
                if (slot.getItem() == null) {
                    System.out.println("\nNo item assigned to this slot."
                    + " Please assign an item first.");
                }
                
                else if(slot.getCurrentStock() == 0) {
                    System.out.println("\nSlot is already empty. "
                    + "Cannot remove more items.");
                }

                else {
                    quantity = 0;
                    currentStock = slot.getItemList().size();
                    do {
                    System.out.println("Enter quantity of items to remove:"
                    + " (Total stock should not be less than 0)");
                    System.out.println("Current stock for '"
                     + slot.getItem().getName() + "': " + currentStock);
                    System.out.print("Enter your choice: ");
                        quantity = keyboard.nextInt();
                        keyboard.nextLine();
                    } while (currentStock - quantity < 0 || quantity < 0);
                    printSpecificSummary(slotIndex, keyboard);

                    for (i = 0; i < quantity; i++) {
                        lastIndex = slot.getItemList().size() - 1;
                        slot.getItemList().remove(lastIndex);
                    }
                    System.out.println("Items removed successfully.");
                }
                break;
            case 4:
                if (slot.getItem() == null) {
                    System.out.println("\nNo item assigned to this slot.");
                } else {
                    printSpecificSummary(slotIndex, keyboard);
                    slot.setItem(null);
                    slot.getItemList().clear();
                    slot.setPrice(0.0);
                    slot.setBeforeStock(0);
                    slot.resetSold();
                    System.out.println("\nAssigned item removed successfully.");
                }
                break;
            case 5:
                if (slot.getItem() == null) {
                    System.out.println("No item assigned to this slot."
                    + " Please assign an item first.");
                } else {
                    System.out.println("\n Current " + slot.getItem().getName()
                     + " " + slot.getPrice());
                    System.out.print("Enter new price for the item: ");
                    price = keyboard.nextDouble();
                    keyboard.nextLine();
                    slot.setPrice(price);
                    System.out.println("Item price updated successfully.");
                }
                break;
            case 6:
                System.out.println("\nReturning to previous menu.");
                break;
            default:
                System.out.println("\nInvalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    /**
     * Displays and allows user to manage the cash vault and its contents
     * @param keyboard the scanner used to get input
     */
    public void manageCashStorage(Scanner keyboard) {
        int i, choice = -1;
        int addQuantity, removeQuantity, index = -1;
        boolean validInput;
        double addValue, removeValue;
        do{
            System.out.println("Managing cash storage...");
            System.out.println("1. Add Cash");
            System.out.println("2. Remove Cash");
            System.out.println("3. Add New Denomination");
            System.out.println("4. Remove a Denomination");
            System.out.println("5. Return to previous menu");
            System.out.print("Enter your choice: ");
            choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice) {
                case 1:
                    validInput = false;
                    addValue = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        addValue = keyboard.nextDouble();
                        keyboard.nextLine();
                        if (validDenomination(addValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid denomination. "
                            + "Please try again.");
                        }
                    } while (!validInput);
                    addQuantity = 0;
                    validInput = false;
                    do{
                        System.out.println("Enter quantity to add:");
                        addQuantity = keyboard.nextInt();
                        keyboard.nextLine();
                        if (addQuantity > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid quantity."
                            + " Please enter a positive number.");
                            validInput = false;
                        }
                    } while (!validInput);
                    cashStorage.addCash(new Denomination(addValue,
                        addQuantity));
                    System.out.println("Cash added successfully.");
                    break;
                case 2:
                    validInput = false;
                    removeValue = 0;
                    removeQuantity = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        removeValue = keyboard.nextDouble();
                        keyboard.nextLine();
                        if (validDenomination(removeValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid denomination."
                            + " Please try again.");
                        }
                    } while (!validInput);
                    validInput = false;
                    do{
                        System.out.println("Enter quantity to remove:");
                        removeQuantity = keyboard.nextInt();
                        keyboard.nextLine();
                        if (removeQuantity > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid quantity."
                            + " Please enter a positive number.");
                            validInput = false;
                        }
                    } while (!validInput);
                    if(cashStorage.removeCash(new Denomination(removeValue,
                        removeQuantity))){
                        System.out.println("Cash removed successfully.");
                    }
                    break;
                case 3:
                    validInput = false;
                    addValue = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        addValue = keyboard.nextDouble();
                        keyboard.nextLine();
                        if (!validDenomination(addValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Denomination already exists. "
                            + "Please try again.");
                        }
                    } while (!validInput);
                    addQuantity = 0;
                    validInput = false;
                    do{
                        System.out.println("Enter quantity to add:");
                        addQuantity = keyboard.nextInt();
                        keyboard.nextLine();
                        if (addQuantity > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid quantity."
                            + " Please enter a positive number or 0.");
                            validInput = false;
                        }
                    } while (!validInput);
                    cashStorage.addCash(new Denomination(addValue,
                        addQuantity));
                    cashInserted.addCash(new Denomination(addValue, 0));
                    System.out.println("Cash added successfully.");
                    break;
                case 4:
                    validInput = false;
                    removeValue = 0;
                    do{
                        System.out.println("Enter denomination value:");
                        removeValue = keyboard.nextDouble();
                        keyboard.nextLine();
                        if (validDenomination(removeValue)) {
                            validInput = true;
                        } else {
                            System.out.println("Denomination does not exist. "
                            + "Please try again.");
                        }
                    } while (!validInput);
                    for(i = 0; i < cashStorage.getCashList().size() && index == -1; i++){
                        if(cashStorage.getCashList().get(i).getValue() == removeValue){
                            index = i;
                        }
                    }
                    System.out.printf("Dispensing remaining amount of P%.2f%n", removeValue);
                    System.out.print("Dispensed " + cashStorage.getCashList().get(index).getQuantity());
                    System.out.printf(" of P%.2f%n", removeValue);
                    cashStorage.removeDenomination(removeValue);
                    cashInserted.removeDenomination(removeValue);
                    System.out.println("Denomination removed successfully.");
                    break;
                case 5:
                    System.out.println("Returning to previous menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                }
        } while(choice != 5); 
    }

    /**
     * Prints a transaction summary with data from the last audit
     * @param keyboard the scanner used to get input
     */
    public void printTransactionSummary(Scanner keyboard) {
        double totalSalesRevenue = 0;
        int i;
        Slots s;
        double itemRevenue, totalVaultBalance, cashTotal;
        System.out.println("Transaction Summary:");
        System.out.println("Items Sold:");
        System.out.printf(" %-4s | %-15s | %-7s | %-12s |"
        + " %-10s | %-6s | %-9s%n","Slot", "Item Name", "Price",
         "Starting Stock", "Ending Stock", "Sold", "Revenue");
        System.out.println("------------------------------------------------"
        + "---------------------------------");
        
        for (i = 0; i < slotList.size(); i++) {
            s = slotList.get(i);
            if (s.getItem() != null) {
                itemRevenue = s.getSold() * s.getPrice();
                totalSalesRevenue += itemRevenue;
                
                System.out.printf(" [%d]  | %-15s | P%-6.2f | %-14d | %-12d |"
                + " %-6d | P%.2f%n", (i + 1), s.getItem().getName(),
                s.getPrice(), s.getBeforeStock(), s.getCurrentStock(),
                s.getSold(), itemRevenue);
                s.resetSold();
                s.setBeforeStock(s.getCurrentStock());
            } else {
                System.out.printf(" [%d]  | %-15s | %-7s | %-14s |"
                + " %-12s | %-6s | %-9s%n", (i + 1), "N/A",
                "-", "-", "-", "-", "-");
            }
        }
        System.out.printf("Revenue Collected: P%.2f%n", totalSalesRevenue);
        System.out.println("Current Cash Audit:");
        totalVaultBalance = 0;
        for (Denomination d : cashStorage.getCashList()) {
            cashTotal = d.getValue() * d.getQuantity();
            totalVaultBalance += cashTotal;
            if (d.getQuantity() > 0) {
                System.out.printf("  - Denomination: P%-7.2f |"
                + " Count: %-4d | Subtotal: P%.2f%n", 
                    d.getValue(), d.getQuantity(), cashTotal);
            }
        }
        System.out.printf("Total Cash In Machine: P%.2f%n%n",
        totalVaultBalance);
        System.out.println("Press enter to continue...");
        keyboard.nextLine();
    }

    /**
     * prints a transaction summary with data from the last restocking/audit of the selected slot
     * @param index the index of the selected slot
     * @param keyboard the scanner used to get input
     */
    public void printSpecificSummary(int index, Scanner keyboard){
        double totalSalesRevenue = 0;
        Slots s;
        double itemRevenue;
        System.out.println("Transaction Summary:");
        System.out.println("Items Sold:");
        System.out.printf(" %-15s | %-7s | %-12s |"
        + " %-10s | %-6s | %-9s%n", "Item Name", "Price",
         "Starting Stock", "Ending Stock", "Sold", "Revenue");
        System.out.println("------------------------------------------------"
        + "---------------------------------");
        
        s = slotList.get(index);
        if (s.getItem() != null) {
            itemRevenue = s.getSold() * s.getPrice();
            totalSalesRevenue += itemRevenue;
            
            System.out.printf(" %-15s | P%-6.2f | %-14d | %-12d |"
            + " %-6d | P%.2f%n", s.getItem().getName(),
            s.getPrice(), s.getBeforeStock(), s.getCurrentStock(),
            s.getSold(), itemRevenue);
        } else {
            System.out.printf(" %-15s | %-6s | %-14s |"
            + " %-12s | %-4s | %-9s%n", "N/A",
            "-", "-", "-", "-", "-");
        }
        System.out.printf("Revenue Collected: P%.2f%n", totalSalesRevenue);
        System.out.println("Press enter to continue...");
        keyboard.nextLine();
    }
}
