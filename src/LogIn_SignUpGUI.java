import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

//testing recomitt
public class LogIn_SignUpGUI extends Application{

    SignUp signUpObj = new SignUp();
    @Override
    public void start(Stage primaryStage){
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();

        Scene myScene = new Scene(signUp(),500,300);
        primaryStage.setScene(myScene);
        primaryStage.show();

    }

    public static void main(String[] args){
        Application.launch(args);
    }

    public VBox signUp(){
        VBox signUpBox= new VBox(10);
        VBox container1 = new VBox(10),container2 = new VBox(10);
        /************ fields for container1, which holds the first fields **********/
        GridPane grid = new GridPane();
        Label userName = new Label("UserName");
        TextField userNameInput = new TextField();

        Label email = new Label("Email");
        TextField emailInput= new TextField();
        Label password = new Label("Password");

        PasswordField passwordInput = new PasswordField();
        TextField passwordText = new TextField();
        passwordText.setVisible(false);
        passwordText.setManaged(false);

        Label confrim = new Label("Confirm Password");
        PasswordField confirmPass = new PasswordField();
        TextField confrimPassText = new TextField();
        confrimPassText.setVisible(false);
        confrimPassText.setManaged(false);

        Button showPassword1 = showPassword(passwordInput,passwordText);
        Button showPassword2 = showPassword(confirmPass,confrimPassText);

        grid.addRow(0,userName,userNameInput);
        grid.addRow(1,email,emailInput);
        grid.addRow(2,password,passwordInput);grid.add(passwordText,1,2);//added passwordText in the same column
        grid.addRow(3,confrim,confirmPass);grid.add(confrimPassText,1,3);//quite some rough code

        grid.add(showPassword1,3,2);//adding show password button to row 3
        grid.add(showPassword2,3,3);//adding show password button to row 4

        grid.setHgap(5);
        grid.setVgap(10);

        Button nextBtn = new Button("Next");
        container1.getChildren().addAll(grid,nextBtn);

        //disabling button if the fields are not filled
        nextBtn.disableProperty().bind(
                userNameInput.textProperty().isEmpty()
                        .or(emailInput.textProperty().isEmpty())
                        .or(passwordInput.textProperty().isEmpty().and(passwordText.textProperty().isEmpty()))
                                .or(confirmPass.textProperty().isEmpty().and(confrimPassText.textProperty().isEmpty()))
        );
        nextBtn.setOnAction(ActionEvent->{
            if(!signUpObj.EmailExists(emailInput.getText())){
                boolean confirmation1 = (confirmPass.getText().equals(passwordInput.getText()))||(confirmPass.getText().equals(passwordText.getText()));
                boolean confirmation2 = (confrimPassText.getText().equals(passwordInput.getText()))||(confrimPassText.getText().equals(passwordText.getText()));
                if(confirmation1||confirmation2){
                    container1.setVisible(false);
                    container1.setManaged(false);
                    container2.setVisible(true);
                    container2.setManaged(true);
                }
                else{
                //alert the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Confirm Password");
                alert.setHeaderText("YOUR PASSWORDS DONT MATCH");
                alert.setContentText("Please retry to confirm your password");
                alert.showAndWait();
                confirmPass.setStyle("-fx-border-color: red;");
                }
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Email Validation");
                alert.setHeaderText("YOUR EMAIL IS ALREADY IN USE");
                alert.setContentText("You can't use the same email twice");
                alert.showAndWait();
                emailInput.setStyle("-fx-border-color: red");
            }
        });
/****** container2...holds security Questions************/
        Label que1 = new Label(signUpObj.getVerificationQuestion(1));
        TextField ans1 = new TextField();
        Label que2 = new Label(signUpObj.getVerificationQuestion(2));
        TextField ans2 = new TextField();
        Label que3 = new Label(signUpObj.getVerificationQuestion(3));
        TextField ans3 = new TextField();
        Separator line = new Separator();
        Separator line1 = new Separator();
        container2.getChildren().addAll(que1,ans1,line,que2,ans2,line1,que3,ans3);
        container2.setVisible(false);
        container2.setManaged(false);
        ans1.setPrefColumnCount(15);ans2.setPrefColumnCount(15);ans3.setPrefColumnCount(15);
        Button signUpBtn = new Button("SIGN UP");
        container2.getChildren().add(signUpBtn);
        signUpBtn.setOnAction(ActionEvent->{
            String emailHold = emailInput.getText();
            String userNameHold = userNameInput.getText();
            String passwordHold ;
            String verifAnswer=ans1.getText()+ans2.getText()+ans3.getText();
            if(passwordInput.isVisible())
                passwordHold=passwordInput.getText();
            else passwordHold=passwordText.getText();
            signupButon(userNameHold,emailHold,passwordHold,verifAnswer);
        });

        signUpBtn.disableProperty().bind( //disables button when text fields are not filled
                ans1.textProperty().isEmpty()
                        .or(ans2.textProperty().isEmpty())
                        .or(ans3.textProperty().isEmpty())
        );
        //Aligning & Binding with properties
        container1.setPadding(new Insets(10));
        container1.prefWidthProperty().bind(signUpBox.widthProperty());
        container2.setPadding(new Insets(10));
        container2.prefWidthProperty().bind(signUpBox.widthProperty());
        container2.setAlignment(Pos.CENTER);
        container2.setFillWidth(false);
        signUpBox.getChildren().addAll(container1,container2);
        return signUpBox;
    }
    private Button showPassword(PasswordField passwordField, TextField passwordText){
        FontIcon eyeIcon = new FontIcon("fa-eye-slash");
        eyeIcon.setIconSize(20);
        Button show = new Button();
        show.setStyle("-fx-background-color: transparent");
        show.setGraphic(eyeIcon);
        show.setOnAction(ActionEvent->{
            if(passwordField.isVisible()){
                passwordText.setText(passwordField.getText());
                passwordText.setManaged(true);
                passwordText.setVisible(true);

                passwordField.setManaged(false);
                passwordField.setVisible(false);

                //show.setGraphic(new FontIcon("fa-eye"));
                eyeIcon.setIconLiteral("fa-eye");

            }else{
                passwordField.setText(passwordText.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);

                passwordText.setManaged(false);
                passwordText.setVisible(false);

                //show.setGraphic(new FontIcon("fa-eye-slash"));
                eyeIcon.setIconLiteral("fa-eye-slash");
            }

        });
       return show;
    }
    private void signupButon(String userName,String email, String password, String verificaitionReply){
        signUpObj.setUserEmail(email);
        signUpObj.setUserName(userName);
        signUpObj.setPassword(password);
        signUpObj.setVerificationReply(verificaitionReply);
        signUpObj.saveAccount();
    }
}
