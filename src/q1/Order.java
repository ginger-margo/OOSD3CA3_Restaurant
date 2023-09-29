package q1;

// class Order that stores the description of the order 
public class Order {
	
	private String description;
	
	// constructor
	public Order(String descr) {
		this.description = descr;
	}
	
	// toString method for string output 
	@Override
	public String toString() {
		return description;
	}
}
