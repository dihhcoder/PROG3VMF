import java.util.ArrayList;

public class CashStorage
{
    private ArrayList<Denomination> cashList;

    public CashStorage(ArrayList<Denomination> cashList)
    {
        this.cashList = cashList;
    }

    public void addCash(Denomination cash)
    {
        this.cashList.add(cash);
    }

    public void removeCash(Denomination cash)
    {
        this.cashList.remove(cash);
    }

}