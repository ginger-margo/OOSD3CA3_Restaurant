package q1;

import java.util.ArrayList;
import java.util.List;


// Q1 related only 
public class App {
	
	List<Order> orders = new ArrayList<Order>();

	public static void main(String[] args) {
		Restaurant theRestaurant = new Restaurant();
		theRestaurant.setOrders(generateOrders());
	}
	
	
	private static List<Order> generateOrders() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("Pelmeni"));
		orders.add(new Order("Borsht"));
		orders.add(new Order("Olivie"));
		orders.add(new Order("Syrniki"));
		return orders;
	}
}
