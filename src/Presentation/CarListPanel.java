package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Business.Car;

/**
 * Panel encapsulating car list
 * @author IwanB
 *
 */
public class CarListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1013855025757989473L;
	
	private List<ICarSelectionNotifiable> notifiables = new ArrayList<ICarSelectionNotifiable>();
	private Vector<Car> cars;
	
	/**
	 * 
	 * @param cars - vector of cars to display in the car list panel
	 */
	public CarListPanel(Vector<Car> cars)
	{
		this.cars = cars;
		this.setBorder(BorderFactory.createLineBorder(Color.black));	
		initList(this.cars);
	}

	private void initList(Vector<Car> cars) {
		
		final JList<Car> list = new JList<Car>(cars);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);
		this.add(listScroller);
		
		BorderLayout listLayout = new BorderLayout();
		listLayout.addLayoutComponent(listScroller, BorderLayout.CENTER);
		this.setLayout(listLayout);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				for(ICarSelectionNotifiable notifiable : notifiables)
				{
					Car selectedCar = list.getSelectedValue();
					if(selectedCar != null)
					{
						notifiable.carSelected(selectedCar);
					}
				}
			}		
		});
	}
	
	/**
	 * Refresh car list to display vector of car objects
	 * @param cars - vector of car objects to display
	 */
	public void refresh(Vector<Car> cars)
	{
		this.removeAll();
		this.initList(cars);
		this.updateUI();
		this.notifiables.clear();
	}
	
	/**
	 * Register an object to be notified of a car selection change
	 * @param notifiable object to invoke when a new car is selected
	 */
	public void registerCarSelectionNotifiableObject(ICarSelectionNotifiable notifiable)
	{
		notifiables.add(notifiable);
	}
}
