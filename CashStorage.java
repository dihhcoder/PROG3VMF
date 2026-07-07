import java.util.ArrayList;
/**
 * A Constructor class for Cash Storages/Vaults on the Vending Machine
 * @author Jasper Isiah Geronimo
 * @author John Kendrick Constantino
 */
public class CashStorage
{
    private ArrayList<Denomination> cashList;

    /**
     * Creates a CashStorage with regards to the current PHP Denominations
     * <p>
     * Stores them in increasing order
     * @param cashList The ArrayList which stores the denominations
     */
    public CashStorage(ArrayList<Denomination> cashList)
    {
        this.cashList = cashList;
        this.addCash(new Denomination(0.01, 0));
        this.addCash(new Denomination(0.05, 0));
        this.addCash(new Denomination(0.10, 0));
        this.addCash(new Denomination(0.25, 0));
        this.addCash(new Denomination(1.00, 0));
        this.addCash(new Denomination(5.00, 0));
        this.addCash(new Denomination(10.00, 0));
        this.addCash(new Denomination(20.00, 0));
        this.addCash(new Denomination(50.00, 0));
        this.addCash(new Denomination(100.00, 0));
        this.addCash(new Denomination(200.00, 0));
        this.addCash(new Denomination(500.00, 0));
        this.addCash(new Denomination(1000.00, 0));
    }

    /**
     * Adds cash to the Cash Vault
     * <p>
     * If denomination value already exists, only the quantity is updated
     * Else, A new denomination will be created and will be added in increasing order
     * @param cash The Denomination to be added
     */
    public void addCash(Denomination cash)
    {
        int i, newQuantity;
        Denomination temp;
        boolean found, added;
        if (cash.getValue() > 0 && cash.getQuantity() >= 0)
        {
            found = false;
            for (i = 0; i < cashList.size() && !found; i++)
            {
                temp = cashList.get(i);
                if (temp.getValue() == cash.getValue())
                {
                    newQuantity = temp.getQuantity() + cash.getQuantity();
                    cashList.get(i).setQuantity(newQuantity);
                    found = true;
                }
            }
            if (!found)
            {
                added = false;
                for (i = 0; i < cashList.size() && !added; i++)
                {
                    if (cashList.get(i).getValue() > cash.getValue())
                    {
                        cashList.add(i, new Denomination(cash.getValue(),
                        cash.getQuantity()));
                        added = true;
                    }
                }
                if (!added)
                {
                    cashList.add(new Denomination(cash.getValue(),
                    cash.getQuantity()));
                }
            }
        }
        else
        {
            System.out.println("Invalid cash denomination or quantity.");
        }
    }

    /**
     * Removes a certain amount of a denomination
     * @param cash The denomination to be removed
     * @return true if operation was successful, else false
     */
    public boolean removeCash(Denomination cash)
    {
        boolean success = false;
        int i, newQuantity;
        boolean found;
        Denomination temp;
        if(cash.getValue() > 0 && cash.getQuantity() > 0)
        {
            found = false;
            for (i = 0; i < cashList.size() && !found; i++)
            {
                temp = cashList.get(i);
                if (temp.getValue() == cash.getValue())
                {
                    found = true;
                    newQuantity = temp.getQuantity() - cash.getQuantity();
                    
                    if (newQuantity >= 0)
                    {
                        cashList.set(i, new Denomination(temp.getValue(),
                        newQuantity));
                        success = true;
                    }
                    else
                    {
                        System.out.println("Not enough cash of this"
                        + " denomination to remove.");
                        success = false;
                    }
                }
            }
            if (!found)
            {
                System.out.println("Cash denomination not found in storage.");
                success = false;
            }
        }
        else
        {
            System.out.println("Invalid cash denomination or quantity.");
        }
        return success;
    }

    /**
     * Gets the arraylist holidng all denominations
     * @return the cashvault arraylist
     */
    public ArrayList<Denomination> getCashList()
    {
        return cashList;
    }

    /**
     * Gets the number of denominations in the list
     * @return The size of the cash vault
     */
    public int getSize(){
        return cashList.size();
    }

    /**
     * Removes a denomination from the cash vault
     * @param value the value of the denomination to be removed
     */
    public void removeDenomination(double value){
        int i;
        boolean removed = false;
        for(i = 0; i < cashList.size() && !removed; i++){
            if(cashList.get(i).getValue() == value){
                cashList.remove(i);
                removed = true;
            }
        }
    }
}