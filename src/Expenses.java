//GUI classes
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

//data handling classes
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class Expenses extends Application {
    //data fields
    private static String email = "resources", userName;
    private double totalAmountUsed, monthlyIncome = 7000;
    private  ObservableList<String> categories = FXCollections.observableArrayList();
    private ArrayList<String> transactionList = new ArrayList<>();
    FilteredList<String> filteredCategories = new FilteredList<>(categories, s->true);
    private double[] plans ;
    Stage primaryStage;//used when refreshing
    //methods

    public boolean methodCaller(String email,double monthlyIncome){
        try{
            this.email=email;
            this.monthlyIncome=monthlyIncome;
            start(primaryStage);
            Expenses.main(new String[]{});
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage=primaryStage;
        readData();
        if(plans == null)
            plans=new double[categories.size()];
        boolean hasInternet = hasInternet();

        /** all nodes**/
        HBox navBar = navBar();
        VBox sideBar = sideBar();
        HBox centerLayout = new HBox(10);
        VBox leftPaneBox = new VBox(10);
        ScrollPane leftPane = new ScrollPane();
        leftPane.setContent(leftPaneBox);
        PieChart pieChart=pieChart();
        VBox summaryBox = new VBox(10);
        VBox rightPane = new VBox(10);


        //Adding the Category Bar with some search bar Filtering
        filteredCategories.addListener((ListChangeListener.Change<? extends String> change) -> {
            leftPaneBox.getChildren().clear();
            for (String name : filteredCategories) {
                VBox categoryBar = categoryBar(name, hasInternet);
                categoryBar.setId("categoryBar");
                leftPaneBox.getChildren().add(categoryBar);
                categoryBar.setAlignment(Pos.CENTER);
            }
        });
         for(String name : filteredCategories){
            VBox categoryBar = categoryBar(name,hasInternet);
            categoryBar.setId("categoryBar");
            leftPaneBox.getChildren().add(categoryBar);
            categoryBar.setAlignment(Pos.CENTER);
        }

         /** summary box**/
         GridPane grid = new GridPane();
         grid.addRow(0,new Label("MONTHLY INCOME: "),new Label(String.format("K%.2f",monthlyIncome)));
        grid.addRow(1,new Label("AMOUNT USED: "),new Label(String.format("K%.2f",totalAmountUsed)));
        grid.addRow(2,new Label("BALANCE: "),new Label(String.format("K%.2f",monthlyIncome-totalAmountUsed)));
        double perc =totalAmountUsed/monthlyIncome*100;
        Label percent = new Label(String.format("%.2f",perc)+"%");
        grid.addRow(3,new Label("PERCENTAGE USED: "),percent);
        summaryBox.getChildren().add(grid);
        if(perc>90) percent.setStyle("-fx-text-fill: red");
        else percent.setStyle("-fx-text-fill: green");
        if(totalAmountUsed>monthlyIncome){
             summaryBox.getChildren().add(new Label("Your negative balance shows that you have exceed your monthly Income"));
         }


        /** Adding em nodes**/

        leftPane.setContent(leftPaneBox);
        rightPane.getChildren().addAll(pieChart,summaryBox);
        centerLayout.getChildren().addAll(leftPane,rightPane);

        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setLeft(sideBar);
        root.setCenter(centerLayout);

        /**InJava Styling**/
        grid.setHgap(10);
        grid.setVgap(10);
        pieChart.setMinSize(400,400);
        sideBar.setPrefWidth(200);
        sideBar.setPadding(new Insets(10));
        navBar.setPadding(new Insets(10));
        summaryBox.setPadding(new Insets(10));
        leftPaneBox.setFillWidth(false);
        leftPaneBox.setPadding(new Insets(10));
        leftPaneBox.setAlignment(Pos.TOP_CENTER);
        leftPaneBox.prefWidthProperty().bind(leftPane.widthProperty());
        leftPane.prefWidthProperty().bind(centerLayout.widthProperty().multiply(0.45));
        //leftPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightPane.prefWidthProperty().bind(centerLayout.widthProperty().multiply(0.55));

        if(!hasInternet){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("NO INTERNET");
            alert.setHeaderText("SLOW OR NO CONNECTION");
            alert.setContentText("Some features may be unavailable, please check your connection");
            alert.showAndWait();
        }
        primaryStage.setTitle("Expense Tracker");
        Scene myScene = new Scene(root, 1000, 650);
        myScene.getStylesheets().add(getClass().getResource("eStyle.css").toExternalForm());
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        Application.launch(args);
    }
    private PieChart pieChart(){
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (int i =0; i<categories.size();i++) {
            pieData.add(new PieChart.Data(categories.get(i), plans[i]));
        }
        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Expenses Breakdown");
        pieChart.setLegendVisible(true);
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);

        return  pieChart;
    }
    private VBox categoryBar(String name,boolean hasInternet){

        Label categoryName = new Label(name);
        Image iconObj ;

        if(hasInternet){
            iconObj = new Image(getIconName(name));

        }else{
            iconObj = new Image(getClass().getResourceAsStream("/resources/no-wifi.png"));
        }
        //
        ImageView icon = new ImageView(iconObj);
        Image TransiconObj = new Image(getClass().getResourceAsStream("/resources/list.png"));
        ImageView transicon = new ImageView(TransiconObj);
        Button transButton = new Button();
        transButton.setGraphic(transicon);
        HBox displayprogress = new HBox(5,icon,progressBar(name),transButton);
        //displayprogress.setId("displayProgress_HBox");

        VBox displayEachCat = new VBox(10);displayEachCat.setId("displayEachCat_VBox");
        VBox.setVgrow(displayEachCat,Priority.NEVER);
        displayEachCat.getChildren().addAll(categoryName,displayprogress);


        //listeners
        transButton.setOnMouseEntered(MouseEvent ->{
            ColorAdjust fadeEffect = new ColorAdjust();
            fadeEffect.setBrightness(-0.2);
            transButton.setEffect(fadeEffect);
            //transButton.setGraphic(transicon);
            transButton.setStyle("-fx-cursor: hand;");
            transButton.setTooltip(new Tooltip("Transactions"));
            //transButton.setText(null);
        });transButton.setOnMouseExited(MouseEvent->transButton.setEffect(null));
        transButton.setOnAction(actionEvent->recents(name));

        //Inside styling & sizing
        categoryName.setStyle("-fx-font-family: comic Sans MS; -fx-font-weight: Bold;");
        transicon.setFitHeight(18);
        transicon.setFitWidth(13);
        transicon.setPreserveRatio(true);
        icon.setFitWidth(32);
        icon.setFitHeight(28);
        icon.setPreserveRatio(true);
        displayEachCat.setPadding(new Insets(5));

        return displayEachCat;
    }
    private StackPane progressBar(String categoryName){
        StackPane pane = new StackPane();
        double progress = setProgress(categoryName)/monthlyIncome;
        ProgressBar progressBar = new ProgressBar(progress);
        Label label = new Label(String.format("%.2f",progress*100)+"%");
        pane.getChildren().addAll(progressBar,label);

        progressBar.setPrefWidth(200);

        return pane;
    }
    private HBox navBar(){
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle(" -fx-border-color: lightgray;");//-fx-background-color: #f5f5f5;

        // App Title
        Label appTitle = new Label("MyExpenses");
        appTitle.setId("appTitle");

        // Search Bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Categories...");
        // Search bar filtering
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = newValue.toLowerCase();
            filteredCategories.setPredicate(item -> {
                if (filter == null || filter.isEmpty()) {
                    return true; // show all
                }
                return item.toLowerCase().contains(filter); // match category name
            });
        });

        searchBar.setPrefWidth(200);

        // reload button
        Button reloadBtn = new Button("reload");
        reloadBtn.setId("reload");
        reloadBtn.setOnAction(ActionEvent->buttonClicks(reloadBtn.getText()));
        /*
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Profile Icon
        Button profileBtn = new Button("👤");
        profileBtn.setStyle("-fx-background-color: transparent;-fx-font-size: 20; -fx-text-fill: black;");*/

        navBar.getChildren().addAll(appTitle, searchBar,reloadBtn);


        return navBar;
    }
    private VBox sideBar(){
        VBox sideBar = new VBox(10);
        String[] labels = {"Dashboard","Transactions","Expenses",
                "Add Category","Delete Category","Plan My Expenses","My Expense plans","Settings"};

        for(int i = 0;i< labels.length;i++){ //adds the buttons from the array
            String label =(labels[i]);
            Button sideButtons = new Button(label);
            Separator line = new Separator();
            if(i==2 || i==6)
                sideBar.getChildren().addAll(sideButtons,line);
            else sideBar.getChildren().add(sideButtons);
            sideButtons.setId("sideButtons");
            sideButtons.setStyle("-fx-background-color: transparent; -fx-text-fill: blue;-fx-cursor: hand");//-fx-font-size: 14;
            sideButtons.setOnAction(actionEvent -> buttonClicks(sideButtons.getText()));

        }
        return sideBar;
    }
    public static String getIconName(String CategoryName){
        String baseLink = "https://raw.githubusercontent.com/Spencer-MK/Expense_icons/main/";
        if(CategoryName.toLowerCase().contains("food"))
            return baseLink+"food.png";
        else if (CategoryName.toLowerCase().contains("housing"))
            return baseLink+"housing.png";
        else if (CategoryName.toLowerCase().contains("transport"))
            return baseLink+"transport.png";
        else if (CategoryName.toLowerCase().contains("education"))
            return baseLink+"education.png";
        else if (CategoryName.toLowerCase().contains("health"))
            return baseLink+"health.png";
        else if (CategoryName.toLowerCase().contains("personal"))
            return baseLink+"personal.png";
        else if (CategoryName.toLowerCase().contains("entertainment"))
            return baseLink+"entertain.png";
        else if (CategoryName.toLowerCase().contains("savings"))
            return baseLink+"saving.png";
        else if (CategoryName.toLowerCase().contains("debt"))
            return baseLink+"debt.png";

        return baseLink+"other.png";


    }
    public double setProgress(String CategoryName){
        String line; double sum=0;
        for (int i=0;i<transactionList.size();i++){
            String[] divArray = transactionList.get(i).split(",");
            if(divArray[2].equals(CategoryName)){
                sum +=Double.parseDouble(divArray[1]);
            }
        }
        return sum;
    }
    public void saveData(){
        try{//saving plans
            File folder = new File (email);
            folder.mkdir();
            File file = new File(email+"/plans.txt");
            file.createNewFile();
            //saving plans
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(email+"/plans.txt")));
            for(int i = 0; i<plans.length; i++){
                writer.printf("%.2f",plans[i]);
                writer.println();
            }
            writer.close();
            PrintWriter writeToCategories = new PrintWriter(new BufferedWriter(new FileWriter(email+"/categories.txt")));
            for(int i = 0; i<plans.length; i++){
                writeToCategories.write(categories.get(i));
                writeToCategories.println();
            }
            writeToCategories.close();
        }catch(IOException e){
            System.out.println("Error saving data: "+ e.getMessage());
        } catch (Exception e){
            System.out.println("Something went wrong : " + e.getMessage());
        }

    }
    private void buttonClicks(String prompt){
        if(prompt.equals("My Expense plans")){
            Dialog<Void> dialogBox = new Dialog<>();
            GridPane grid = new GridPane();
            Label h1 = new Label("CATEGORY"), h2=new Label("BUDGETED AMOUNT"),h3 = new Label("AMOUNT USED"),h4 = new Label("PERCENTAGE USED");
            grid.add(h1,0,0);grid.add(h2,1,0);grid.add(h3,2,0);grid.add(h4,3,0);
            for(int i = 0; i<plans.length;i++){
                int row = i+1;
                double amountUsd= setProgress(categories.get(i));
                double percentage = (amountUsd/plans[i])*100;
                grid.add(new Label(categories.get(i)),0,row);
                grid.add(new Label(String.format("%.2f",plans[i])),1,row);
                grid.add(new Label(String.format("%.2f",amountUsd)),2,row);
                grid.add(new Label(String.format("%.2f",percentage)+"%"),3,row);
            }
            grid.setHgap(20);
            grid.setVgap(10);
            dialogBox.setTitle(prompt);
            dialogBox.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialogBox.getDialogPane().setContent(grid);
            h1.setFont(Font.font("Times New Roman", FontWeight.BOLD,14));
            h2.setFont(Font.font("Times New Roman", FontWeight.BOLD,14));
            h3.setFont(Font.font("Times New Roman", FontWeight.BOLD,14));
            h4.setFont(Font.font("Times New Roman", FontWeight.BOLD,14));

            dialogBox.showAndWait();

        } else if (prompt.equals("Plan My Expenses")){
            TextField[] inputFields = new TextField[plans.length];
            Dialog<Void> dialogBox = new Dialog<>();
            Label budget = new Label("Draft a mini Budget of your Expenditures This month");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            Label ok = new Label("press ok to save");ok.setStyle("-fx-text-fill: green");
            VBox dialog = new VBox(10,budget,grid,ok);
            for(int i=0; i<categories.size();i++){
                Label name = new Label(categories.get(i));
                TextField inputText = new TextField();
                inputText.setPromptText("0.00");
                grid.add(name,0,i);
                grid.add(inputText,1,i);
                inputFields[i]=inputText;

                inputText.addEventFilter(KeyEvent.KEY_TYPED, event->{
                    if((!event.getCharacter().matches("\\d")) || event.getCharacter().isEmpty()){
                        event.consume();
                        ok.setText("ONLY NUMERIC INPUT ALLOWED");
                        ok.setStyle("-fx-text-fill: red;");
                    }
                    else{
                        ok.setText("press okay to save");
                        ok.setStyle("-fx-text-fill: green");
                    }

                });
            }
            dialogBox.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.OK);
            Button okButton = (Button)dialogBox.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setOnAction(actionEvent -> {
                for(int i = 0; i<inputFields.length ;i++){
                    String input = inputFields[i].getText();
                    if(input!=null)
                        plans[i] = Double.parseDouble(input);
                    else
                        plans[i]=0.00;
                } saveData();
            });
            dialogBox.getDialogPane().setContent(dialog);
            dialogBox.setTitle(prompt);
            dialogBox.showAndWait();
        
        } else if (prompt.equals("Add Category")) {
            Dialog<Void> dialogBox =new Dialog<>();
            GridPane content = new GridPane();
            Label categoryName = new Label("Category Name");
            TextField textInput = new TextField();
            content.addRow(0,categoryName,textInput);
            content.setHgap(10);

            dialogBox.getDialogPane().getButtonTypes().add(ButtonType.OK);
            Button okButton = (Button)dialogBox.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setOnAction(actionEvent -> {
                categories.add(textInput.getText()); //adds the entered category to the list
                saveData();//saves the updated list
                start(primaryStage);//recalls the start method
            });

            dialogBox.setTitle(prompt);
            dialogBox.getDialogPane().setContent(content);
            dialogBox.showAndWait();
        } else if (prompt.equals("Delete Category")) {
            Dialog<Void> dialogBox = new Dialog<>();
            dialogBox.setTitle("Delete Categories");
            VBox container = new VBox(5);
            ObservableList<String> selected = FXCollections.observableArrayList();

            for (int i = 0; i < categories.size(); i++) {
                CheckBox select = new CheckBox(categories.get(i));
                select.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                    if (isSelected) {
                        selected.add(select.getText());
                    } else {
                        selected.remove(select.getText());
                    }
                });
                container.getChildren().add(select);
            }

            // Add ButtonTypes first
            ButtonType deleteType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialogBox.getDialogPane().getButtonTypes().addAll(deleteType, cancelType);

            //  lookingUp Button
            Button deleteButton = (Button) dialogBox.getDialogPane().lookupButton(deleteType);

            // Optional: disable delete if no selection
            deleteButton.disableProperty().bind(Bindings.isEmpty(selected));

            deleteButton.setOnAction(event -> {

                for (String item : selected) {
                    categories.remove(item); //remove the actual item, not "selected"
                    ArrayList<Double> temp = new ArrayList<>();
                    for(int i = 0;i<plans.length;i++)
                        if(i!=categories.indexOf(item)){
                            temp.add(plans[i]);
                        }
                    plans = new double[temp.size()];
                    for(int i = 0;i<plans.length;i++)
                        plans[i]=temp.get(i);
                }
                saveData();
                start(primaryStage); //recalls start for a reload
                //Main.main(new String[]{}); //reloads app
            });

            dialogBox.getDialogPane().setContent(container);

            dialogBox.showAndWait();
        } else if (prompt.equals("reload")) {
            start(primaryStage);
        } else if (prompt.equals("Dashboard")) {

        } else if (prompt.equals("Transaction")) {

        } else if (prompt.equals("Settings")) {

        } else if (prompt.equals("Expenses")) {
            start(primaryStage);
        }

    }
    private void readData() {
        try {//reading categories
            String fileName = email + "/categories.txt";
            BufferedReader readCategories = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            String line;
            int i = 0;
            while ((line = readCategories.readLine()) != null) {
                if (i < categories.size())
                    categories.set(i, line);
                else categories.add(line);
                i++;
            }
            readCategories.close();
        } catch (IOException e) {
            System.out.println("error reading Categories ; " + e.getMessage());
        } catch (Exception e) {
            String error = ("I/O error: " + e.getMessage());
        }

        try {//reading transactions
            String fileName = email + "/transactions.txt";
            BufferedReader readTransactions = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            String line;
            int i = 0;
            while ((line = readTransactions.readLine()) != null) {
                String[] div = line.split(",");
                if(dateChecker(div[0]))
                    continue;
                if (i < transactionList.size())
                    transactionList.set(i, line);
                else transactionList.add(line);
                i++;
            }
            readTransactions.close();
        } catch (IOException e) {
                System.out.println("IO error reading transactions: " + e.getMessage());
        } catch (Exception e) {
                System.out.println("something went wrong: " + e.getMessage());
        }
        try {//reading plans
            String fileName = email + "/plans.txt";
            BufferedReader readPlans = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            plans = new double[categories.size()];
            String line;
            int i = 0;
            while ((line = readPlans.readLine()) != null) {
                plans[i] = Double.parseDouble(line);
                i++;
            }
            readPlans.close();
        } catch (IOException e) {
            System.out.println("Error reading plans : "+e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong: "+e.getMessage());
        }
        //calculating total amount
        double sum = 0;
        for (int i = 0; i < transactionList.size(); i++) {
            String line = transactionList.get(i);
            String[] array = line.split(",");
            sum += Double.parseDouble(array[1]);
        }
        totalAmountUsed = sum;

    }
    private static boolean hasInternet() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD"); //sends messege to google head server
            conn.setConnectTimeout(3000); // 3 seconds of waiting
            conn.setReadTimeout(3000);
            int responseCode = conn.getResponseCode();
            return (200 <= responseCode && responseCode <= 399); //true if gets responce in time
        } catch (IOException e) {
            return false;
        }/**Problem: method runs once for 3secs, on upgrade need of a constant refreshing one**/
    }
    private static boolean dateChecker(String dateString) {
            try {
                // conversioniasis
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = formatter.parse(dateString);
                //calendar for inputus
                Calendar inputCalendar = Calendar.getInstance();
                inputCalendar.setTime(parsedDate);
                // date us
                Calendar currentCalendar = Calendar.getInstance();
                // Le compar
                return inputCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                        inputCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR);

            } catch (Exception e) {
                System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
                return false;
            }
    }
    private void recents(String name){
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("RECENTS");
        dialog.setHeaderText("Recent Transactions In this Category");

        // Set button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE, ButtonType.OK);

        // Create table
        TableView<eTransaction> table = new TableView<>();
        // Date column
        TableColumn<eTransaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(100);
        // Category column
        TableColumn<eTransaction, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(120);
        // Description column
        TableColumn<eTransaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(200);
        // Amount column
        TableColumn<eTransaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setPrefWidth(100);
        amountColumn.setCellFactory(column -> new TableCell<eTransaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {//formating amount data
                super.updateItem(amount, empty);
                if (empty || amount == null) { // prevent va null_P. exception
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("K%.2f", amount));
                    //setStyle(amount < 0 ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
                }
            }
        });
        table.getColumns().addAll(categoryColumn, descriptionColumn, amountColumn,dateColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Adding data
        ObservableList<eTransaction> data = FXCollections.observableArrayList();

        int count = 0; //limits #of recents
        for(int i =0; i<transactionList.size() && count <11; i++){
            String[] div = transactionList.get(i).split(",");
            if(!name.equals(div[2]))
                continue;
            double amount = Double.parseDouble(div[1]);
            eTransaction transaction  = new eTransaction(div[0],div[2],div[3],amount);
            data.add(transaction);
        }
        table.setItems(data);

        // Dialog Box layout
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().add(table);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefSize(600, 400);

        // Show dialog
        dialog.showAndWait();
    }
    public static class eTransaction{
        private final String date;
        private final String category;
        private final String description;
        private final double amount;

        public eTransaction(String date, String category, String description, double amount) {
            this.date = date;
            this.category = category;
            this.description = description;
            this.amount = amount;
        }

        public String getDate() { return date; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
    }

}

/** AND IN THE END THEY RESTED... **/