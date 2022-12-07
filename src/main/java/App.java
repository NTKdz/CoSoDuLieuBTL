import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static final seller SELLER = new seller();

    public static seller getSeller() {
        return SELLER;
    }

    private static final SignIn user = new SignIn();

    public static SignIn getUser() {
        return user;
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/seller/seller.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (seller.checkpress) {
                    seller.checkpress = false;
                    getSeller().refreshtable();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}