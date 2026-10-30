package id.indigym;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{
    @Override 
    public void start(Stage primaryStage) throws IOException{
        //Memanggil login.fxml
        URL loginView = MainApp.class.getResource("/fxml/login.fxml");
        if (loginView==null){
            throw new IllegalStateException(
                "File login.fxml tidak ditemukan!"
            );
        }
        //Memuat tampilan FXMLnya
        FXMLLoader loader = new FXMLLoader(loginView);
        Parent root = loader.load();
        //Scene Aplikasi
        Scene scene = new Scene(root, 1100, 720);
        //File CSS
        URL css = MainApp.class.getResource("/css/application.css");
        if (css==null){
            throw new IllegalStateException(
                "File css tidak ditemukan!"
            );
        }
        //Penerapan CSS
        scene.getStylesheets().add(css.toExternalForm());
        //Pengaturan Jendela Aplikasi
        primaryStage.setTitle("IndiGym - Gym Management");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}