public class Item
{
    private String name;
    private int calories;

    public Item(String name, int calories) 
    {
        this.name = name;
        this.calories = calories;
    }

    public String getName() 
    {
        return this.name;
    }

    public int getCalories() 
    {
        return this.calories;
    }
}