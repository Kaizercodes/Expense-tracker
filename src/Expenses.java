//GUI classes
import javafx.application.Application;
import javafx.stage.Stage;

//data handling classes
import java.io.*;
import java.util.ArrayList;


public class Expenses extends Application {
    //data fields
    private static String email, userName;
    private double plannedCatExp, plannedTotal, actualTotal, actualCatExp;

    //methods
    public void Expenses(String email){ //constructor
        this.email= email;
    }

    public void planExpenses(ArrayList<String> planned){
        try{
            File ExpensePlan = new File(email + "/ExpensePlans");
            ExpensePlan.createNewFile();// creating file
            //saving plans
           PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("ExpensePlans.txt")));
            for(int i = 0; i<planned.size(); i++){
                writer.write(planned.get(i));
                writer.println();
            }
           writer.close();
        }catch(IOException e){
            //!!! dont forget to catch
        } catch (Exception e){
            //!!! don't forget to catch
        }

    }
    public static void readData() {
        try{//reading expense plans
            String fileName = email + "ExpensePlans.txt";
            BufferedReader readPlans = new BufferedReader(new FileReader(fileName));

            readPlans.close();
        } catch(IOException e){
            System.out.println("error reading plans ; " + e.getMessage());
        }
        catch (Exception e){
            String error = ("error: " + e.getMessage());
        }
    }


    @Override
    public void start(Stage primaryStage){

    }
    public static void main(String[] args){
        Application.launch(args);
    }

}
