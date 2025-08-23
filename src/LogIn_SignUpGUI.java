import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.w3c.dom.Text;

import java.net.PasswordAuthentication;

//testing recomit
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

        Button signUpBtn = new Button("sign up");
        signUpBtn.setOnAction(ActionEvent->{
            if(confirmPass.getText().equals(passwordInput.getText()) || confirmPass.getText().equals(passwordText.getText())){
                //allow login & save account
                signUpObj.setUserEmail(emailInput.getText());
                if(passwordInput.getText()!=null)
                    signUpObj.setPassword(passwordInput.getText());
                else signUpObj.setPassword(passwordText.getText());

                if(!signUpObj.emailExist())
                    signUpObj.saveAccount();
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Email Validation");
                    alert.setHeaderText("YOUR EMAIL IS ALREADY IN USE");
                    alert.setContentText("You can't use the same email twice");
                    alert.showAndWait();

                }

            }else{
                //alert the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Confirm Password");
                alert.setHeaderText("YOUR PASSWORDS DONT MATCH");
                alert.setContentText("Please retry to confirm your password");
                alert.showAndWait();

                confirmPass.setStyle("-fx-border-color: red;");
            }
        });
        VBox signUpBox = new VBox(10);
        signUpBox.getChildren().addAll(grid,signUpBtn);

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
                eyeIcon.setIconCode(FontAwesome.EYE);

            }else{
                passwordField.setText(passwordText.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);

                passwordText.setManaged(false);
                passwordText.setVisible(false);

                //show.setGraphic(new FontIcon("fa-eye-slash"));
                eyeIcon.setIconCode(FontAwesome.EYE_SLASH);
            }

        });
       return show;
    }
}
