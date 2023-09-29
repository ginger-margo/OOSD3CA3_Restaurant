package q1;

//class waiter - a thread

import java.util.List;

public class Waiter extends Thread {

	private List<Order> orders;
	private Chef chef;

	// constructor
	public Waiter(Chef chef) {
		this.chef = chef;

	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * Gives orders to chef one by one
	 * Notifies auxiliary thread that one more order is ready
	 * Notify chef about last order 
	 * Finishes work
	 */
	public void run() {
		chef.start();
		while (!orders.isEmpty()) { // while there are orders in the list of orders
			synchronized (orders) {
				Order currentOrder = orders.get(0); // set the first order as current order
				System.out.println("Bringing order to chef " + currentOrder);
				synchronized (currentOrder) {
					if (orders.size() == 1) { // if this is the last order in the list
						chef.setLastOrder(); // notify the chef that this is the last order
					}
					chef.setOrder(currentOrder); // the chef picks up the order
					try {
						currentOrder.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Bringing order to customer " + currentOrder);
				orders.remove(0); // remove order from orders
				orders.notify();
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Waiter is done");
	}
}
