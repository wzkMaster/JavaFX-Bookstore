package JavaFX;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//根据需要生成一个可以给用户两种选择的提示窗口
public class ConfirmBox {
    static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //保证用户必须处理完这个窗口才能做别的事情

        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label(message);

        Button yesButton = new Button("确认");
        Button noButton = new Button("取消");

        yesButton.setOnAction(e ->{
            answer = true;
            window.close();
        } );
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(yesButton,noButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300,200);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
