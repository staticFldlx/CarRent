package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 
 * @author IwanB
 * AddEntitiesPanel is shown at the top of the car tracker window
 * - this is where all buttons used to add entities like Event/Instruction/User/Car should be added
 * 
 */
public class AddEntitiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2256207501462485047L;
	

	private JButton addCarButton = new JButton(StringResources.getCarAddButtonLabel());


	public AddEntitiesPanel()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(addCarButton);
		updateLayout();
		addCarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddCarDialog dialog = new AddCarDialog();
				dialog.setVisible(true);
			}
		});	
	}


	private void updateLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		layout.addLayoutComponent(addCarButton, BorderLayout.CENTER);
	}
}
