package pages.filterProducts;

public class ProductInfo {
    private  final double price;
    private final  double rate;
    public ProductInfo(double price, double rate) {
        this.price = price;
        this.rate = rate;
    }
    public double getPrice() {
        return price;
    }

    public double getRate() {
        return rate;
    }



}
