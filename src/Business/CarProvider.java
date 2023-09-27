package Business;

import java.util.Vector;

import Data.RepositoryProviderFactory;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 * @author IwanB
 *
 */
public class CarProvider implements ICarProvider {

	/**
	 * Check employee credentials
	 * @param userName : the userName of employee credentials
	 * @param password : the password of employee credentials
	 */
	@Override
	public String checkEmployeeCredentials(String userName, String password) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().checkEmployeeCredentials(userName, password);
	}

	/**
	 * Find cars associated in some way with a userName
	 * Cars which have the parameter below should be included in the result
	 * @param userName
	 * @return
	 */
	@Override
	public Vector<Car> findCarsByEmployee(String userName) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().findCarsByEmployee(userName);
	}

	/**
	 * Update the details for a given car
	 * @param car : the car for which to update details
	 */
	@Override
	public void updateCar(Car car) {
		RepositoryProviderFactory.getInstance().getRepositoryProvider().updateCar(car);
	}

	/**
	 * Add the details for a new car to the database
	 * @param car : the new car to add
	 */
	@Override
	public void addCar(Car car) {
		RepositoryProviderFactory.getInstance().getRepositoryProvider().addCar(car);
	}

	/**
	 * Given an expression searchString like 'word' or 'this phrase', this method should return 
	 * any cars that contains this phrase.
	 * @param searchString: the searchString to use for finding cars in the database
	 * @return
	 */
	@Override
	public Vector<Car> findCarsByCriteria(String searchString) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().findCarsByCriteria(searchString);
	}
}
