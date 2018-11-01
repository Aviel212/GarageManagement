import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayList {
	ArrayList<Car> cars;
	DateFormat dateFormat;
	Date date;
	
	DayList(){
		cars = new ArrayList<Car>();
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date=new Date();
	}
}
