import java.util.ArrayList;

public class Slots
{

    private ArrayList<Item> ItemList;
    private int capacity;
    private double price;

    public Slots(ArrayList<Item> ItemList, int capacity, double price)
    {
        this.ItemList = ItemList;
        this.capacity = capacity;
        this.price = price;
    }

    public ArrayList<Item> getItemList()
    {
        return ItemList;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public double getPrice()
    {
        return price;
    }

    public Item getItem()
    {
        if (!ItemList.isEmpty())
        {
            return ItemList.get(0);
        }
        else
        {
            System.out.println("Slot is empty. No items to retrieve.");
            return null;
        }
    }    

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void addItem(Item newItem)
    {
        if (this.ItemList.size() < this.capacity)
        {
            this.ItemList.add(newItem);
        }
        else
        {
            System.out.println("Slot is full. Cannot add more Items.");
        }
    }

    public void removeItem(Item itemToRemove)
    {
        this.ItemList.remove(itemToRemove);
    }


}