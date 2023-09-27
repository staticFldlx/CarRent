package Presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Business.BusinessComponentFactory;
import Business.Car;

public class CarTrackerFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5532618722097754725L;
	
	private AddEntitiesPanel addEntitiesPanel = null;
	private CarDetailPanel detailPanel = null;
	private CarSidePanel sidePanel = null;


	static String loggedInUsername = null;

	/**
	 * Main car tracker frame
	 * Logs on the user
	 * Initialize side panel + add entities panel + containing event list + detail panel
	 */
	public CarTrackerFrame()
	{
		setTitle(StringResources.getAppTitle());
	    setLocationRelativeTo(null);
	    
	    logOnUser();
	    initialise();
	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);  
	}
	
	/**
	 *  !!! 
	 *  Only used to simulate logon
	 *  This should really be implemented using proper salted hashing
	 *	and compare hash to that in DB
	 *  really should display an error message for bad login as well
	 *	!!!
	 */
	private void logOnUser() {
		boolean OK = false;
		while (!OK) {		
				String userName = (String)JOptionPane.showInputDialog(
									this,
									null,
									StringResources.getEnterUserNameString(),
									JOptionPane.QUESTION_MESSAGE);
				
				JPasswordField jpf = new JPasswordField();
				int okCancel = JOptionPane.showConfirmDialog(
									null,
									jpf,
									StringResources.getEnterPasswordString(),
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
				
				String password = null;
				if (okCancel == JOptionPane.OK_OPTION) {
					password = new String(jpf.getPassword());
				}

				if (userName == null || password == null)
					System.exit(0);
				else
					if (!userName.isEmpty() && !password.isEmpty()) {
						loggedInUsername = checkAdmCredentials(userName, password);
						if (loggedInUsername != null) {
							OK = true;
						}
						else {
							// Display an error dialog when the username or password is incorrect
							System.out.println("Password incorrect!");
							JOptionPane.showMessageDialog(this,
								"Incorrect username or password.",
								"Login Failed",
								JOptionPane.ERROR_MESSAGE);
						}
					}
		}
	}

	private void initialise()
	{
		addEntitiesPanel = new AddEntitiesPanel();	
	    detailPanel = new CarDetailPanel(true);	    
	    sidePanel = getSidePanel(new CarListPanel(getAllCars()));
	    
	    BorderLayout borderLayout = new BorderLayout();
	    borderLayout.addLayoutComponent(addEntitiesPanel, BorderLayout.NORTH);
	    borderLayout.addLayoutComponent(sidePanel, BorderLayout.WEST);
	    borderLayout.addLayoutComponent(detailPanel, BorderLayout.CENTER);
	    
	    JButton  refreshButton = new JButton(StringResources.getRefreshButtonLabel());
	    final CarTrackerFrame frame = this;
	    refreshButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.refresh(frame.getAllCars());
			}
		});
	    
	    borderLayout.addLayoutComponent(refreshButton, BorderLayout.SOUTH);
	    
	    this.setLayout(borderLayout);
	    this.add(addEntitiesPanel);
	    this.add(refreshButton);
	    this.add(sidePanel);
	    this.add(detailPanel);
	    this.setSize(600, 300);
	}
	
	private CarSidePanel getSidePanel(CarListPanel listPanel)
	{
		final CarTrackerFrame frame = this;
		listPanel.registerCarSelectionNotifiableObject(detailPanel);
		return new CarSidePanel(new ISearchCarListener() {
			@Override
			public void searchClicked(String searchString) {
				frame.refresh(frame.findCarsByCriteria(searchString));
			}
		},listPanel);
	}
	
	private String checkAdmCredentials(String userName, String password)
	{
		return BusinessComponentFactory.getInstance().getCarProvider().checkEmployeeCredentials(userName, password);
	}
	
	private Vector<Car> getAllCars()
	{
		return BusinessComponentFactory.getInstance().getCarProvider().findCarsByEmployee(loggedInUsername);
	}
	
	private Vector<Car> findCarsByCriteria(String pSearchString)
	{
		pSearchString = pSearchString.trim();
		if (!pSearchString.equals(""))
			return BusinessComponentFactory.getInstance().getCarProvider().findCarsByCriteria(pSearchString);
		else
			return getAllCars();
	}
	
	private  void refresh(Vector<Car> cars)
	{
		if(sidePanel != null && detailPanel!= null)
		{
			sidePanel.refresh(cars);
			detailPanel.refresh();
			sidePanel.registerCarSelectionNotifiableObject(detailPanel);
		}
	}
	
	
	public static void main(String[] args)
	{
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	CarTrackerFrame ex = new CarTrackerFrame();
                ex.setVisible(true);
            }
        });		
	}
}
