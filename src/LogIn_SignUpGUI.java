import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.w3c.dom.Text;

import java.net.PasswordAuthentication;

//testing recomitt
public class LogIn_SignUpGUI extends Application{

    private boolean showButtonHandler = true;
    LogIn user = new LogIn();
    SignUp signUpObj = new SignUp();
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Expense tracker");

        Button signUpButton = new Button("sign up");
        primaryStage.setScene(logIn(signUpButton));
        signUpButton.setOnAction(_->{
            Scene myScene = new Scene(signUp(),500,300);
            primaryStage.setScene(myScene);
        });
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
                String email_in= emailInput.getText();
                if(!SignUp.EmailExists(email_in))
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
    public Scene logIn(Button signUpButton){
        // all pane containers used
        VBox mainPane = new VBox();
        GridPane activityPane = new GridPane();
        StackPane forgotPasswordButtonPane = new StackPane();
        GridPane showAndLogInButtonContainer = new GridPane();
        FlowPane toSignUpPane = new FlowPane();

        //pane containers styling
        forgotPasswordButtonPane.setStyle("-fx-background-color: indigo");
        mainPane.setStyle("-fx-background-color: indigo");
        toSignUpPane.setPadding(new Insets(0,50,50,50));
        activityPane.setStyle("-fx-background-color: indigo");
        activityPane.setVgap(5);
        activityPane.setPadding(new Insets(50,50,50,50));

        //a header label on page and its styling with its container
        StackPane subScenePane = new StackPane();
        subScenePane.setStyle("-fx-background-color: green");
        Label projectLabel = new Label("EXPENSE-TRACKER");
        projectLabel.setStyle("-fx-text-fill: white; -fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 18");
        subScenePane.getChildren().add(projectLabel);
        SubScene topLabelContainer = new SubScene(subScenePane,360,50);

        //buttons and labels
        Label emailLabel =  new Label("Email account");
        Label passwordLabel = new Label("password");
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button logInButton = new Button(" LOG IN ");
        Button forgotPasswordButton = new Button("Forgot password");
        Button showPasswordButton = new Button(" SHOW ");
        Label toSignUp = new Label("do not have account?");


        showAndLogInButtonContainer.setHgap(100);
        showAndLogInButtonContainer.addRow(1,showPasswordButton,logInButton);

        //labels and button styling
        emailLabel.setTextFill(Color.WHITE);
        passwordLabel.setTextFill(Color.WHITE);
        forgotPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: grey");
        logInButton.setStyle("-fx-background-color: green; -fx-text-fill: white");
        showPasswordButton.setStyle("-fx-background-color: green; -fx-text-fill: white");
        signUpButton.setStyle("-fx-background-color: transparent; -fx-text-fill: grey");
        toSignUp.setStyle("-fx-text-fill: white; -fx-font-style: italic;");

        //layout management
        activityPane.add(emailLabel,1,1);
        activityPane.add(emailField,1,3);
        activityPane.add(passwordLabel,1,4);
        activityPane.add(passwordField,1,6);
        activityPane.add(showAndLogInButtonContainer,1,7);
        forgotPasswordButtonPane.getChildren().add(forgotPasswordButton);
        toSignUpPane.getChildren().addAll(toSignUp,signUpButton);
        mainPane.getChildren().addAll(topLabelContainer,activityPane,forgotPasswordButtonPane,toSignUpPane);

        Scene scene = new Scene(mainPane,360,400);

        logInButtonController(scene,signUpButton,logInButton,showPasswordButton,forgotPasswordButton,emailField,passwordField,activityPane,mainPane,forgotPasswordButtonPane);

        return scene;
    }
    public void logInButtonController(Scene scene,Button signUpButton,Button logIn, Button show,Button forgotPassword,TextField email,PasswordField password, GridPane activityPane, VBox mainPane, StackPane forgotPasswordButtonPane){
        SignUp userIn = new SignUp();

        logIn.setOnAction(_->{
            String userEmail = email.getText();
            String userPassword = password.getText();
            if(SignUp.EmailExists(userEmail)){

                user.setEmail(userEmail);
                try {
                    if(user.verifiedPassword(userPassword)) {
                        /*
                                put the next scene here!!!
                         */


                        return;
                    }
                    else{
                        Label warning2 = new Label("you entered a wrong password!");
                        warning2.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                        activityPane.add(warning2,1,5);
                    }
                } catch (Exception e) {
                    Label warning4 = new Label("something went wrong!");
                    warning4.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                    activityPane.add(warning4,1,5);
                }
            }else{
                Label warning1 = new Label("the email you entered does not exist");
                warning1.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                activityPane.add(warning1,1,2);
            }
        });

        forgotPassword.setOnAction(_->{
            mainPane.getChildren().remove(forgotPasswordButtonPane);
            String userEmail = email.getText();
            if(SignUp.EmailExists(userEmail)) {
                user.setEmail(userEmail);
                activityPane.getChildren().removeAll(activityPane.getChildren());
                forgotPasswordButtonPane.getChildren().removeAll();

                Label question1 = new Label(userIn.getVerificationQuestion(1));
                TextField question1reply = new TextField();
                Label question2 = new Label(userIn.getVerificationQuestion(2));
                TextField question2reply = new TextField();
                Label question3 = new Label(userIn.getVerificationQuestion(3));
                TextField question3reply = new TextField();
                Button logIn2 = new Button(" LOG IN ");

                question1.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
                question2.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
                question3.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
                logIn2.setStyle("-fx-background-color: green; -fx-text-fill: white");

                activityPane.addColumn(1, question1, question1reply, question2, question2reply, question3, question3reply);
                activityPane.add(logIn2, 2, 7);

                logIn2.setOnAction(_ -> {
                    String replies = question1reply.getText() + question2reply.getText() + question3reply.getText();
                    try {
                        if (user.checkVerificationAnswer(replies)){
                            /*

                                put the next scene here!!!

                            */
                            return;
                        }
                        else {
                            Label warning3 = new Label("you gave a wrong answer somewhere!");
                            warning3.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                            activityPane.add(warning3, 1, 9);
                        }
                    } catch (Exception e) {
                        Label warning4 = new Label("something went wrong!");
                        warning4.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                        activityPane.add(warning4,1,9);
                    }
                });
            }else{
                Label warning1 = new Label("the email you entered does not exist");
                warning1.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
                activityPane.add(warning1,1,2);
            }
        });

        TextField passwordTextField = new TextField();
        show.setOnAction(_->{
            if(showButtonHandler){
                showButtonHandler = false;
                String passwordText = password.getText();
                passwordTextField.setText(passwordText);
                activityPane.add(passwordTextField,1,6);
                show.setText(" HIDE ");
            }else{
                showButtonHandler = true;
                activityPane.getChildren().remove(passwordTextField);
                show.setText("SHOW");
            }

        });
    }
}
