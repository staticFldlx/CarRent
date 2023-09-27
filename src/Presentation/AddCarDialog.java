package Presentation;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import Business.Car;

/**
 * 
 * @author IwanB
 *
 * AddCarDialog - used to add a new car
 * 
 */
public class AddCarDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 173323780409671768L;
	
	/**
	 * detailPanel: reuse CarDetailPanel to add cars
	 */
	private CarDetailPanel detailPanel = new CarDetailPanel(false);

	public AddCarDialog()
	{
		setTitle(StringResources.getAppTitle());
		detailPanel.initCarDetails(getBlankCar());
		add(detailPanel);
		updateLayout();
		setSize(400, 400);
	}

	private void updateLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		layout.addLayoutComponent(detailPanel, BorderLayout.CENTER);
	}

	private Car getBlankCar() {
		Car car = new Car();
		return car;
	}
}
