package Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Vector;
import java.sql.Date;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;

import Business.Car;
import Presentation.IRepositoryProvider;

/**
 * Encapsulates create/read/update/delete operations to PostgreSQL database
 * @author IwanB
 */
public class PostgresRepositoryProvider implements IRepositoryProvider {
	//DB connection parameters - ENTER YOUR LOGIN AND PASSWORD HERE

    // private final String userid = "y23s2c9120_xzha0908";
    // private final String passwd = "123456";
    // private final String myHost = "soit-db-pro-2.ucc.usyd.edu.au";

	private final String userid = "postgres";
    private final String passwd = "123456";
    private final String myHost = "localhost";

	private Connection openConnection() throws SQLException
	{
		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerName(myHost);
		source.setDatabaseName(userid);
		source.setUser(userid);
		source.setPassword(passwd);
		Connection conn = source.getConnection();
	    System.out.println("Sql connected successfully!");
	    return conn;
	}

	/**
	 * Validate login request
	 * @param userName: the employee's userName trying to login
	 * @param password: the employee's password used to login
	 * @return
	 */
	@Override
	// public String checkEmployeeCredentials(String userName, String password) {



	// 	return "";
	// }
	
	public String checkEmployeeCredentials(String userName, String password) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
	
		try {
			// Open database connection
			conn = openConnection();
	
			// SQL query to check credentials
			String query = "SELECT userName FROM Employee WHERE userName = ? AND password = ?";
			
			// Prepared statement to avoid SQL Injection
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			
			// Execute query
			resultSet = preparedStatement.executeQuery();
	
			// If result set is not empty, credentials are correct
			if (resultSet.next()) {
				System.out.println("Password Correct!\n" + resultSet.getString("userName") + " has logged in!");
				return resultSet.getString("userName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		} finally {
			// Close resources
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return null;
	}
	
	/**
	 * Find all cars associated with an employee
	 * @param userName: the userName
	 * @return
	 */
	@Override
	// public Vector<Car> findCarsByEmployee(String userName) {

	// 	return new Vector<Car>();
	// }
	public Vector<Car> findCarsByEmployee(String userName) {
        Vector<Car> cars = new Vector<Car>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = openConnection();
            // SQL query to find cars by employee
			String query = "SELECT c.carID, c.make || ' ' || c.model as makeModel, s.statusName as status, " +
						"ct.carTypeName || ' - ' || cw.carWheelName as typeWheel, " +
						"c.purchaseDate, c.managedBy, c.description " +
						"FROM Car c " +
						"JOIN Status s ON c.statusID = s.statusID " +
						"JOIN CarType ct ON c.carTypeID = ct.carTypeID " +
						"LEFT JOIN CarWheel cw ON c.carWheelID = cw.carWheelID " +
						"WHERE c.managedBy = ? " +
						"ORDER BY c.description ASC, s.statusName DESC, c.purchaseDate DESC";
			
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userName);
			
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Car car = new Car();
				car.setCarId(resultSet.getInt("carID"));
				car.setMakeModel(resultSet.getString("makeModel"));
				car.setStatus(resultSet.getString("status"));
				car.setTypeWheel(resultSet.getString("typeWheel"));
				car.setPurchaseDateString(resultSet.getDate("purchaseDate").toString());
				car.setEmployee(resultSet.getString("managedBy"));
				car.setDescription(resultSet.getString("description"));
				cars.add(car);
			}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cars;
    }

	/**
	 * Find a list of cars based on the searchString provided as parameter
	 * @param searchString: see assignment description for search specification
	 * @return
	 */
	@Override
	// public Vector<Car> findCarsByCriteria(String searchString) {

	// 	return new Vector<Car>();
	// }
	public Vector<Car> findCarsByCriteria(String searchString) {
        Vector<Car> cars = new Vector<Car>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = openConnection();
            // SQL query with criteria based on keyword
			String query = "SELECT c.*, ct.carTypeName, s.statusName, cw.carWheelName, e.firstName || ' ' || e.lastName AS fullName " +
			"FROM Car c " +
			"LEFT JOIN CarType ct ON c.carTypeID = ct.carTypeID " +
			"LEFT JOIN Status s ON c.statusID = s.statusID " +
			"LEFT JOIN CarWheel cw ON c.carWheelID = cw.carWheelID " +
			"LEFT JOIN Employee e ON c.managedBy = e.userName " +
			"WHERE (AGE(current_date, c.purchaseDate) <= interval '15 years') AND " +
			"(c.make ILIKE ? OR c.model ILIKE ? OR s.statusName ILIKE ? OR ct.carTypeName ILIKE ? OR cw.carWheelName ILIKE ? OR e.firstName ILIKE ? OR e.lastName ILIKE ? OR c.description ILIKE ?) " +
			"ORDER BY CASE WHEN c.managedBy IS NULL THEN 1 ELSE 2 END, c.purchaseDate ASC";

			preparedStatement = conn.prepareStatement(query);
			for (int i = 1; i <= 8; i++) {
				preparedStatement.setString(i, "%" + searchString + "%");
			}

			// Execute query
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
			Car car = new Car();
			car.setCarId(resultSet.getInt("carID"));
			car.setMakeModel(resultSet.getString("make") + " " + resultSet.getString("model"));
			car.setStatus(resultSet.getString("statusName"));
			car.setTypeWheel(resultSet.getString("carTypeName") + " - " + resultSet.getString("carWheelName"));
			car.setPurchaseDateString(resultSet.getDate("purchaseDate").toString());
			car.setEmployee(resultSet.getString("fullName"));
			car.setDescription(resultSet.getString("description"));
			cars.add(car);
			}
			System.out.println("Searching the Car(s) about '" + searchString + "''.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cars;
    }

	private int getIdFromTableNameAndName(String tableName, String columnName, String name, Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + " = ?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
	
		if (rs.next()) {
			return rs.getInt(1); 
		} else {
			throw new SQLException("Name not found in table");
		}
	}

	/**
	 * Add a new car into the Database
	 * @param car: the new car to add
	 */
	@Override
	// public void addCar(Car car) {

	// }
	public void addCar(Car car) {
		Connection conn = null;
        PreparedStatement preparedStatement = null;
		try {
			// Open database connection
			conn = openConnection();

			// Convert wheel and type string to respective IDs
			int wheelId = getIdFromTableNameAndName("CarWheel", "carWheelName", car.getWheel(), conn);
			int typeId = getIdFromTableNameAndName("CarType", "carTypeName", car.getType(), conn);

			// Prepare SQL query
			String query = "INSERT INTO Car(make, model, statusID, carTypeID, carWheelID, purchaseDate, description) VALUES(?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = conn.prepareStatement(query);

			preparedStatement.setString(1, car.getMake());
			preparedStatement.setString(2, car.getModel());
			preparedStatement.setInt(3,1);
			preparedStatement.setInt(4, typeId);
			preparedStatement.setInt(5, wheelId);
			preparedStatement.setDate(6, java.sql.Date.valueOf(car.getPurchaseDateString()));
			preparedStatement.setString(7, car.getDescription());

			// Execute the query
			preparedStatement.executeUpdate();
			System.out.println(car.getMakeModel() + " has been added successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close resources
			try {
				if (preparedStatement != null) preparedStatement.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

	/**
	 * Update the details of a given car
	 * @param car: the car for which to update details
	 */
	@Override
	// public void updateCar(Car car) {
		
	// }
	public void updateCar(Car car) {
		Connection conn = null;  // This should be your database connection object
		PreparedStatement preparedStatement = null;
	
		try {
			// Make sure to get your connection from your specific DataSource or Connection pool
			conn = openConnection();
			
			// Convert wheel and type string to respective IDs
			int wheelId = getIdFromTableNameAndName("CarWheel", "carWheelName", car.getWheel(), conn);
			int typeId = getIdFromTableNameAndName("CarType", "carTypeName", car.getType(), conn);
			int statusId = getIdFromTableNameAndName("Status", "statusName", car.getStatus(), conn);

			String query = "UPDATE Car SET make = ?, model = ?, statusID = ?, carTypeID = ?, carWheelID = ?, purchaseDate = ?, managedBy = ?, description = ? WHERE carID = ?";
	
			preparedStatement = conn.prepareStatement(query);
	
			// Assuming the Car class has getter methods for all these properties
			preparedStatement.setString(1, car.getMake());
			preparedStatement.setString(2, car.getModel());
			preparedStatement.setInt(3, statusId); 
			preparedStatement.setInt(4, typeId); 
			preparedStatement.setInt(5, wheelId); 
			preparedStatement.setDate(6, java.sql.Date.valueOf(car.getPurchaseDateString()));
			preparedStatement.setString(7, car.getEmployee());
			preparedStatement.setString(8, car.getDescription());
			preparedStatement.setInt(9, car.getCarId());
	
			preparedStatement.executeUpdate();
			
			System.out.println("The information about No." + car.getCarId() + " has been updated successfully!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
