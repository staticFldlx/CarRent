package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Business.BusinessComponentFactory;
import Business.Car;


/**
 * 
 * @author IwanB
 * Panel used for creating and updating cars
 */
public class CarDetailPanel extends JPanel implements ICarSelectionNotifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2031054367491790942L;

	private Car currentCar = null;
	private boolean isUpdatePanel = true;

	private JTextField carIdField;
	private JTextField makeModelField;
	private JTextField makeField;
	private JTextField modelField;
	private JTextField statusField;
	private JTextField typeWheelField;
	private JTextField typeField;
	private JTextField wheelField;
	private JTextField purchaseDateField;
	private JTextField employeeField;
	private JTextArea descriptionArea;
	
	/**
	 * Panel used for creating and updating cars
	 * @param isUpdatePanel : describes whether panel will be used to either create or update car
	 */
	public CarDetailPanel(boolean isUpdatePanel)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.isUpdatePanel = isUpdatePanel;
	}

	/**
	 * Re-populates panel details with given car object
	 * @param car new car object to populate panel details with
	 */
	public void initCarDetails(Car car) {
		removeAll();	
		if(car != null)
		{
			currentCar = car;
			addAll();
		}
	}

	private void addAll() {
		JPanel lTextFieldPanel = new JPanel();
		BoxLayout lTextFieldLayout = new BoxLayout(lTextFieldPanel, BoxLayout.Y_AXIS);
		lTextFieldPanel.setLayout(lTextFieldLayout);

		BorderLayout lPanelLayout = new BorderLayout();	
		lPanelLayout.addLayoutComponent(lTextFieldPanel, BorderLayout.NORTH);

		//create car text fields
		//application convention is to map null to empty string (if db has null this will be shown as empty string)
		if(currentCar.getCarId() > 0) {
			carIdField = createLabelTextFieldPair(StringResources.getCarIdLabel(), ""+currentCar.getCarId(), lTextFieldPanel);
			carIdField.setEditable(false);
		}

		if(isUpdatePanel) {
			makeModelField = createLabelTextFieldPair(StringResources.getMakeModelLabel(), currentCar.getMakeModel() == null ? "" : ""+ currentCar.getMakeModel(), lTextFieldPanel);
			statusField = createLabelTextFieldPair(StringResources.getStatusLabel(), currentCar.getStatus() == null ? "" : ""+currentCar.getStatus(), lTextFieldPanel);
			typeWheelField = createLabelTextFieldPair(StringResources.getTypeWheelLabel(), currentCar.getTypeWheel() == null ? "" : ""+ currentCar.getTypeWheel(), lTextFieldPanel);
			purchaseDateField = createLabelTextFieldPair(StringResources.getPurchaseDateLabel(), currentCar.getPurchaseDate() == null ? "" : ""+ currentCar.getPurchaseDateString(), lTextFieldPanel);
			employeeField = createLabelTextFieldPair(StringResources.getEmployeeLabel(), currentCar.getEmployee() == null ? "" : ""+currentCar.getEmployee(), lTextFieldPanel);
		} else {
			makeField = createLabelTextFieldPair(StringResources.getMakeLabel(), "", lTextFieldPanel);
			modelField = createLabelTextFieldPair(StringResources.getModelLabel(), "", lTextFieldPanel);
			typeField = createLabelTextFieldPair(StringResources.getTypeLabel(), "", lTextFieldPanel);
			wheelField = createLabelTextFieldPair(StringResources.getWheelLabel(), "", lTextFieldPanel);
			purchaseDateField = createLabelTextFieldPair(StringResources.getPurchaseDateLabel(), "", lTextFieldPanel);
		}
		//employeeField = createLabelTextFieldPair(StringResources.getEmployeeLabel(), currentCar.getEmployee() == null ? CarTrackerFrame.loggedInUsername : ""+currentCar.getEmployee(), lTextFieldPanel);
		add(lTextFieldPanel);

		//create description text area
		descriptionArea = new JTextArea(currentCar.getDescription() == null ? "" : currentCar.getDescription());
		descriptionArea.setAutoscrolls(true);
		descriptionArea.setLineWrap(true);
		lPanelLayout.addLayoutComponent(descriptionArea, BorderLayout.CENTER);
		add(descriptionArea);

		//Create car save (create or update button)
		JButton saveButton = createCarSaveButton();
		lPanelLayout.addLayoutComponent(saveButton, BorderLayout.SOUTH);
		this.add(saveButton);

		setLayout(lPanelLayout);
		updateUI();
	}

	private JButton createCarSaveButton() {
		JButton saveButton = new JButton(isUpdatePanel ? StringResources.getCarUpdateButtonLabel() : 
			StringResources.getCarAddButtonLabel());
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//application convention is to map empty string to null (i.e. if app has empty string - this will be null in db)
				if(isUpdatePanel) {
					currentCar.setMakeModel(makeModelField.getText().equals("") ? null : makeModelField.getText());
					currentCar.setStatus(statusField.getText().equals("") ? null : statusField.getText());
					currentCar.setTypeWheel(typeWheelField.getText().equals("") ? null : typeWheelField.getText());
					currentCar.setEmployee(employeeField.getText().equals("") ? null : employeeField.getText());
				} else {
					String make = makeField.getText().equals("") ? "" : makeField.getText();
					String model = modelField.getText().equals("") ? "" : " "+modelField.getText();
					currentCar.setMakeModel(make + model);

					String type = typeField.getText().equals("") ? "" : typeField.getText();
					String wheel = wheelField.getText().equals("") ? "" : " - "+wheelField.getText();
					currentCar.setTypeWheel(type + wheel);
				}
				currentCar.setPurchaseDateString(purchaseDateField.getText().equals("") ? null : purchaseDateField.getText());
				currentCar.setDescription(descriptionArea.getText().equals("") ? null : descriptionArea.getText());

				if(isUpdatePanel) {
					BusinessComponentFactory.getInstance().getCarProvider().updateCar(currentCar);
				}
				else {
					BusinessComponentFactory.getInstance().getCarProvider().addCar(currentCar);
				}
			}
		});
		
		return saveButton;
	}

	private JTextField createLabelTextFieldPair(String label, String value, JPanel container) {
		
		JPanel pairPanel = new JPanel();
		JLabel jlabel = new JLabel(label);
		JTextField field = new JTextField(value);
		pairPanel.add(jlabel);
		pairPanel.add(field);

		container.add(pairPanel);

		BorderLayout lPairLayout = new BorderLayout();
		lPairLayout.addLayoutComponent(jlabel, BorderLayout.WEST);
		lPairLayout.addLayoutComponent(field, BorderLayout.CENTER);
		pairPanel.setLayout(lPairLayout);	
		
		return field;
	}

	/**
	 * Implementation of ICarSelectionNotifiable::carSelected used to switch car
	 * displayed on CarDisplayPanel
	 */
	@Override
	public void carSelected(Car car) {
		initCarDetails(car);
	}
	
	/**
	 * Clear car details panel
	 */
	public void refresh()
	{
		initCarDetails(null);
	}
}
