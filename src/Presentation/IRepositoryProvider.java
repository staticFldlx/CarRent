package Presentation;

import java.util.Vector;

import Business.Car;

/**
 * Encapsulates create/read/update/delete operations to database
 * @author IwanB
 *
 */
public interface IRepositoryProvider {
	/**
	 * Check employee credentials
	 * @param userName: the userName of employee credentials
	 * @param password: the password of employee credentials
	 */
	public String checkEmployeeCredentials(String userName, String password);
	
	/**
	 * Find associated cars given an employee
	 * @param userName: the userName
	 * @return
	 */
	public Vector<Car> findCarsByEmployee(String userName);
	
	/**
	 * Find the associated cars based on the searchString provided as the parameter
	 * @param searchString: see assignment description search specification
	 * @return
	 */
	public Vector<Car> findCarsByCriteria(String searchString);	
	
	/**
	 * Add a new car into the database
	 * @param car: the new car to add
	 */
	public void addCar(Car car);

	/**
	 * Update the details for a given car
	 * @param car: the car for which to update details
	 */
	public void updateCar(Car car);
}
