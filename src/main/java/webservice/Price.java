package webservice;

/**
 * Created by oliver on 2017/4/15.
 */
public class Price {
    private String price_type="";//这里就是seat_type
    private String price="";

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
