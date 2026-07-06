public class Item
{
    private String name;
    private double calories;

    public Item(String name, double calories) 
    {
        this.name = name;
        this.calories = calories;
    }

    public String getName() 
    {
        return this.name;
    }

    public double getCalories() 
    {
        return this.calories;
    }
}