import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
        UTILITIES CLASS

        1.Format currency
        2.date
        3.Category Validation
        4.Amount Validation depending on balance
        5.Total Calculation
        6.Expense ID
        8. Filtering expenses and sorting
        9. time
 */
//class for generating an ID
class IDGenerate{
    private static int currentId = 0;

    public static int generateID() {
        return ++currentId;
    }
}
public class UTILITIES{
    //method for date
    public static String Date(){
        String date;

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy"); // formating date like 05 Aug 2025
        date = today.format(formatter);

      return  date;
    }
    //method for time
    public static String Time(){
        String time;

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");    // formating time like 4:30 pm
        time = now.format(formatter);
        return time;
    }
    public static int IDGen(){
        int newId = IDGenerator.generateID();
        return newId;
    }

}
