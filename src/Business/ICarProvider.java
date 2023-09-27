package Business;

import java.util.Vector;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 * @author IwanB
 *
 */
public interface ICarProvider {

	/**
	 * Check employee credentials
	 * @param userName : the userName of employee credentials
	 * @param password : the password of employee credentials
	 */
	public String checkEmployeeCredentials(String userName, String password);
	
	/**
	 * Find cars associated in some way with a userName
	 * Cars which have the parameter below should be included in the result
	 * @param userName
	 * @return
	 */
	public Vector<Car> findCarsByEmployee(String userName);
	
	/**
	 * Given an expression searchString like 'word' or 'this phrase', this method should return 
	 * any cars that contains this phrase.
	 * @param searchString : the searchString to use for finding cars in the database
	 * @return
	 */
	public Vector<Car> findCarsByCriteria(String searchString);	
	
	/**
	 * Add the details for a new car to the database
	 * @param car : the new car to add
	 */
	public void addCar(Car car);

	/**
	 * Update the details for a given car
	 * @param car : the car for which to update details
	 */
	public void updateCar(Car car);
}
