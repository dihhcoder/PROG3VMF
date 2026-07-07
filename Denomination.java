/**
 * Constructor class for denomination object
 * @author John Kendrick Constantino
 */
public class Denomination{
    double value;
    int quantity;

    /**
     * Creates a denomination object(bills and/or coins) with a value and quantity.
     * @param value
     * @param quantity
     */
    public Denomination(double value, int quantity){
        this.value = value;
        this.quantity = quantity;
    }
    /**
     * Gets the value of the denomination
     * @return The denomination value in PHP
     */
    public double getValue(){
        return this.value;
    }

    /**
     * Gets the quantity of the denomination
     * @return The amount of bills/coins available
     */
    public int getQuantity(){
        return this.quantity;
    }

    /**
     * Updates the quantity of the denomination
     * @param quantity The amount to assign to this denomination value
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}