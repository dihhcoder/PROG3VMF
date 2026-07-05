public class Denomination{
    double value;
    int quantity;

    public Denomination(double value, int quantity){
        this.value = value;
        this.quantity = quantity;
    }

    public double getValue(){
        return this.value;
    }

    public int getQuantity(){
        return this.quantity;
    }
}