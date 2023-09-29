package q1;

public class Chef extends Thread {

	private Order order;
	private boolean lastOrder = false;

	/**
	 * Chef waits for order from waiter
	 * Spends time cooking
	 * Notifies waiter that the order is ready
	 * Waits for another order
	 * If the order is last, finishes work after preparation 
	 */
	@Override
	public void run() {
		while (true) {
			if (order != null) {
				System.out.println("Chef started cooking " + order);
				synchronized (order) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Chef finished cooking " + order);
					order.notify(); //notify the next waiting thread
				}
				if (lastOrder) { // check if it was the last order
					break; // lunch break for the chef - he is done :)
				} else {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				order = null;
			}
		}
		System.out.println("Chef is done");
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public void setLastOrder() {
		this.lastOrder = true;
	}
}
