import java.awt.Color;

public class Car {
	String company, model, color;
	String yearCreated;
	String licenseNumber;
	int index=0;
	
	Car(String company, String model, String yearCreated, 
			String color, String licenseNumber){
		this.company=company;
		this.model=model;
		this.yearCreated=yearCreated;
		this.licenseNumber=licenseNumber;
		this.color=color;
		
		index++;
		
	}
}