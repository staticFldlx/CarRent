package Business;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Car {

	private int carId = 0;
	private String makeModel;
	private String status;
	private String typeWheel;
	private String purchaseDate;
	private String employee;
	private String description;


	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getMakeModel() {
		return makeModel;
	}
	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}
	public String getMake() {
		return (makeModel.split(" ", 2).length > 0) ? makeModel.split(" ", 2)[0] : null;
	}
	public String getModel() {
		return (makeModel.split(" ", 2).length > 1) ? makeModel.split(" ", 2)[1] : null;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getTypeWheel() {
		return typeWheel;
	}
	public void setTypeWheel(String typeWheel) {
		this.typeWheel = typeWheel;
	}
	public String getType() {
		return (typeWheel.split(" - ", 2).length > 0) ? typeWheel.split(" - ", 2)[0] : null;
	}
	public String getWheel() {
		return (typeWheel.split(" - ", 2).length > 1) ? typeWheel.split(" - ", 2)[1] : null;
	}

	public String getPurchaseDateString() {
		return purchaseDate;
	}
	public void setPurchaseDateString(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Date getPurchaseDate() {
		java.util.Date date = null;
		try {
			//这里原来是dd/MM/yyyy
			date = new SimpleDateFormat("yyyy-MM-dd").parse(purchaseDate);
		} catch (ParseException e) {
			System.out.println("Incorrect date format. It should be dd/MM/yyyy.\n" + e.getMessage());
		}
		return new Date(date.getTime());
	}

	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//Define the fields used to display search results under Find button
	public String toString()
	{
		return getMakeModel();
	}
}
