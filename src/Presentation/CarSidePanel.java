package Presentation;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;

import Business.Car;

/**
 * 
 * Represents instruction list panel of instruction tracker that includes
 * both the search box/button and text field; AND the instruction list
 * @author IwanB
 *
 */
public class CarSidePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2693528613703066603L;

	private CarListPanel mListPanel;
	
	/**
	 * Represents the left panel of car tracker that includes
	 * both the search box/button and text field; AND the car list
	 * 
	 * @param searchEventListener : used to retrieve user search query in search box
	 * @param listPanel : car list panel
	 */
	public CarSidePanel(ISearchCarListener searchEventListener, CarListPanel listPanel)
	{
		mListPanel = listPanel;
		CarSearchComponents searchComponents = new CarSearchComponents(searchEventListener);
	
		add(searchComponents);
		add(listPanel);
		
		BorderLayout layout = new BorderLayout();
		layout.addLayoutComponent(searchComponents, BorderLayout.NORTH);
		layout.addLayoutComponent(listPanel, BorderLayout.CENTER);
		setLayout(layout);
	}
	
	public void refresh(Vector<Car> cars)
	{
		mListPanel.refresh(cars);
	}
	
	public void registerCarSelectionNotifiableObject(ICarSelectionNotifiable notifiable)
	{
		mListPanel.registerCarSelectionNotifiableObject(notifiable);
	}
}
