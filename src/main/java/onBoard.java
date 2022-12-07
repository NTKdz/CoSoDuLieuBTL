import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class onBoard {
    @FXML
    private Label myStore;

    @FXML
    private Label profile;

    @FXML
    private Label shop;

    @FXML
    void ProfileClicked(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/profile.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void myStoreClicked(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/seller/seller.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void shopClicked(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/customer/customer.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
