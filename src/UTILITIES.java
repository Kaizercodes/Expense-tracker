import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
        UTILITIES CLASS

        1.Format currency
        2.date
        3.Category Validation
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

    // method for ID generation
    public static int IDGen(){
        int newId = IDGenerate.generateID();
        return newId;
    }

    // formating money to be in form of 2000.00
    public static  Double roundToTwoDecimal(double amount){
        String amountFormatted = String.format("%.2f",amount);
        double amounts = Double.parseDouble(amountFormatted);
        return amounts;
    }

    // amount validation
    public static boolean isValidAmount(String amount){
        try{
            Double.parseDouble(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // category validation
    public static boolean isValidCategory(String category){
        boolean isValid = false;
        String [] categories = {"Food & Groceries","Housing & Utilities",
                "Transportation","Education","Health & Medical",
                "Personal & Family","Entertainment & recreation",
                "Savings and Investment", "Debt repayment","Other"};

        for(int index = 0;index < categories.length; index++){
            if(categories[index].equalsIgnoreCase(category.trim())){
                isValid = true;
            }
        }
        return isValid;
    }

    // total amount calculations
    public static double totalAmount(List<Double> amounts){
        double total = 0.00;
        for(double amount : amounts){
            total += amount;
        }
        total = roundToTwoDecimal(total);
        return total;
    }

    // average amount for reports
    public static double averageAmount(List<Double> amounts){
        double total = 0.00;
        double length = amounts.size();
        for(double amount : amounts){
            total += amount;
        }
        double average = total/length;
        average = roundToTwoDecimal(average);
        return average;
    }

    public static void main(String[] args){

    }
}
