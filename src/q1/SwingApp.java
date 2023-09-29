package q1;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;


public class SwingApp {
	
	private static int countChildren = 1;
	private static List<JComboBox<Order>> availableComboboxes = new ArrayList();
	private static List<JLabel> successLabels = new ArrayList();
	private static List<Order> unpreparedOrders = new ArrayList();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Restaurant");
		
		renderMainMenu(frame);

	}
	
	private static void renderMainMenu(JFrame frame){
		JPanel panel = new JPanel(); // creates UI wrapper
		panel.setLayout(new MigLayout()); // setting layout
		
		JPanel orderComposePanel = new JPanel(); // additional layer with list of orders
		orderComposePanel.setLayout(new MigLayout());
		
		JLabel label = new JLabel("Please choose the dishes");
		orderComposePanel.add(label, "wrap");
		
		// typical order line of 3 elements: comboBox + delete button + indicator of readiness
		JComboBox<Order> cb = new JComboBox<>(generateOrders()); 
		availableComboboxes.add(cb);
		JButton btnDeleteItem = new JButton("Delete");
		
		btnDeleteItem.addActionListener((e) -> {
			removeRow(orderComposePanel, btnDeleteItem);
		});
		
		JLabel lblNotification = new JLabel("Dish is ready");
		successLabels.add(lblNotification);
		lblNotification.setVisible(false);
		orderComposePanel.add(cb);
		orderComposePanel.add(btnDeleteItem);
		orderComposePanel.add(lblNotification, "wrap");
		JButton btnAddItem = new JButton("Add new item to the order"); //adds new string with order
	
		
		btnAddItem.addActionListener((e) -> {
			// similar string to the one created during the first rendering 
			JComboBox<Order> cb1 = new JComboBox<>(generateOrders());
			availableComboboxes.add(cb1);
			JButton btnDeleteItem1 = new JButton("Delete");
			btnDeleteItem1.addActionListener((e1) -> {
				removeRow(orderComposePanel, btnDeleteItem1);
			});
			
			JLabel lblNotification1 = new JLabel("Dish is ready");
			successLabels.add(lblNotification1);
			lblNotification1.setVisible(false);
			orderComposePanel.add(cb1, 3*countChildren + 1);
			orderComposePanel.add(btnDeleteItem1, 3*countChildren + 2); 
			orderComposePanel.add(lblNotification1, "wrap", 3*countChildren + 3);
			
			countChildren++;
			orderComposePanel.revalidate(); 
		});
		
		orderComposePanel.add(btnAddItem);
		
		panel.add(orderComposePanel, "wrap");
		
		JButton btnStart = new JButton("Start cooking");
		panel.add(btnStart);
		
		btnStart.addActionListener((e) -> {
			unpreparedOrders.clear();
			for (JComboBox<Order> combx: availableComboboxes) {
				unpreparedOrders.add((Order) combx.getSelectedItem());
			}
			int unpreparedOrdersSize = unpreparedOrders.size();
			// Auxiliary thread: 
			Thread thread = new Thread(()-> {
				for (int i = 0; i < unpreparedOrdersSize; i++) {
					synchronized (unpreparedOrders) {
						try {
							unpreparedOrders.wait(); // waits for notification from waiter that another order is ready
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JLabel lblToShow = successLabels.get(i); // find success label to show
						SwingUtilities.invokeLater(() -> lblToShow.setVisible(true) ); //updates the state of the UI element in the main thread
					}
				}
			});
			
			thread.start();
			Restaurant theRestaurant = new Restaurant();
			theRestaurant.setOrders(unpreparedOrders);
			
		});
		
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 400);
		frame.setVisible(true);
	}

	/**
	 * @param orderComposePanel
	 * @param btnDeleteItem
	 * Finds the row that the delete button belongs to
	 * Finds all the other elements in this row
	 * Removes all of them
	 */
	private static void removeRow(JPanel orderComposePanel, JButton btnDeleteItem) {
		int index = orderComposePanel.getComponentZOrder(btnDeleteItem); //get the index of the bnt
		JLabel lblToDelete = (JLabel) orderComposePanel.getComponents()[index + 1];
		successLabels.remove(lblToDelete);
		orderComposePanel.remove(index + 1);
		orderComposePanel.remove(index);
		JComboBox<Order> currentCb = (JComboBox<Order>) orderComposePanel.getComponents()[index - 1];
		orderComposePanel.remove(index - 1);
		orderComposePanel.revalidate(); // refreshes the page for the added items to be visible
		countChildren--;
		availableComboboxes.remove(currentCb);
	}
	
	private static Order[] generateOrders() {
		return new Order[] {
				new Order("Pelmeni"),
				new Order("Borsht"),
				new Order("Olivie"),
				new Order("Syrniki")
		};
	}
	
	

}
