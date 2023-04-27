import java.lang.reflect.Array;
import java.util.ArrayList;
public class CartList {

    private ArrayList<MovieInCart> list;

    public CartList() {
        this.list = new ArrayList<MovieInCart>();
    }
    public void addToCart(MovieInCart movie){
        for (MovieInCart m : this.list){
            if (m.getName().equals(movie.getName())){
                m.addCount();
                return;
            }
        }
        list.add(movie);
    }

    public void addToCartString(String name){
        for (MovieInCart m : this.list){
            if (m.getName().equals(name)){
                m.addCount();
                return;
            }
        }
    }

    public void removeFromCart(String name){
        for (MovieInCart m : this.list){
            if (m.getName().equals(name)){
                m.subtractCount();
                if (m.getCount()==0){
                    this.deleteFromCart(name);
                }
                return;
            }
        }
    }

    public void deleteFromCart(String name){
        for (MovieInCart m : this.list){
            if (m.getName().equals(name)){
                this.list.remove(m);
                return;
            }
        }
    }

    public ArrayList<MovieInCart> getCart(){
        return list;
    }
}
