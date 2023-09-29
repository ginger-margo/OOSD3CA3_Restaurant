package q1;

import java.util.List;

// class Restaurant contains chef and waiter
public class Restaurant {

	private List<Order> orders;
	private Chef chef;
	private Waiter waiter;
	
	
	public Restaurant() {
		this.chef = new Chef();
		this.waiter = new Waiter(chef);
	}
	
	// starts all the activities
	public void setOrders(List<Order> orders) {
		this.orders = orders;
		waiter.setOrders(orders);
		waiter.start();
	}

}
