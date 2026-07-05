import java.util.ArrayList;

public class Slots
{
    private Item item;
    private ArrayList<Item> ItemList;
    private int capacity;
    private double price;
    private int sold;
    private int beforeStock;

    public Slots(Item item, int capacity, double price)
    {
        this.ItemList = new ArrayList<Item>();
        this.item = item;
        this.capacity = capacity;
        this.price = price;
        this.sold = 0;
        this.beforeStock = 0;
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
        return item;
    }
    
    public int getSold()
    {
        return sold;
    }

    public int getBeforeStock()
    {
        return beforeStock;
    }

    public int getCurrentStock()
    {
        return ItemList.size();
    }

    public void setItem(Item item)
    {
        this.item = item;
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

    public void incrementSold()
    {
        this.sold++;
    }
}