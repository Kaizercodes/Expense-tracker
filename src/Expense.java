import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Expense extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane box = new BorderPane();
        Button btn = new Button("OK");
        RadioButton btn1 = new RadioButton();

        VBox pane = new VBox(10);
        pane.getChildren().addAll(btn,btn1);
        //box.getChildren().add(pane);
        box.setBottom(pane);

        Scene scene = new Scene(box,200,300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
