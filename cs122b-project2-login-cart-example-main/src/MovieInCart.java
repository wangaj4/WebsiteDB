
public class MovieInCart {

    private String name;
    private int price;
    private int count;
    private String id;

    public MovieInCart(String name, int price, String id) {
        this.name = name;
        this.price = price;
        this.count = 1;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId(){
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count +=1;
    }

    public void subtractCount() {
        this.count -=1;
    }
}
