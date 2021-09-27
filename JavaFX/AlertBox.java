package JavaFX;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//根据需要生成一个提示信息的弹窗
public class AlertBox {
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //保证用户必须处理完这个窗口才能做别的事情

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button button = new Button("OK");
        button.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}
