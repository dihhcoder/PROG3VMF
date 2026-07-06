import java.util.ArrayList;

public class CashStorage
{
    private ArrayList<Denomination> cashList;

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
                    cashList.set(i, new Denomination(temp.getValue(),
                    newQuantity));
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

    public ArrayList<Denomination> getCashList()
    {
        return cashList;
    }

    public void printCashStorage()
    {
        System.out.println("Cash Storage:");
        for (Denomination cash : cashList)
        {
            System.out.printf("Denomination: $%.2f, Quantity: %d%n",
            cash.getValue(), cash.getQuantity());
        }
    }

    public int getQuantity(double value)
    {
        for (Denomination cash : cashList)
        {
            if (cash.getValue() == value)
            {
                return cash.getQuantity();
            }
        }
        return 0;
    }
}