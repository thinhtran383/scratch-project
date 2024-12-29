package online.thinhtran.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        scene = new Scene(loadFXML("LoginView"), 600, 328);

        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        primaryStage.show();
    }


    public static void setRoot(String fxmlPath) throws IOException{
        Parent root = loadFXML(fxmlPath);
        scene.setRoot(root);

        stage.setWidth(root.prefWidth(-1));
        stage.setHeight(root.prefHeight(-1));
    }

    public static void setRootPopup(String fxml, String title, double width, double height) throws IOException {

        Stage popup = new Stage();

        Scene popupScene = new Scene(loadFXML(fxml), width, height);

        popup.setResizable(false);
        popup.setScene(popupScene);
        popup.setTitle(title);

        popup.showAndWait();


    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlPath + ".fxml"));

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
