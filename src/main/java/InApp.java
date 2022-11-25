import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class InApp implements Initializable {

    @FXML
    private TableColumn<?, ?> Id;

    @FXML
    private TableColumn<?, ?> Name;

    @FXML
    private Button cartbut;

    @FXML
    private Button productBut;

    @FXML
    private Button profileBut;

    @FXML
    private TextField searchFiled;

    @FXML
    private TableView<?> tableView;

    @FXML
    void searchBar(ActionEvent event) {

    }

    @FXML
    void seeCart(ActionEvent event) {

    }

    @FXML
    void seeProducts(ActionEvent event) {

    }

    @FXML
    void seeProfile(ActionEvent event) {

    }

    @FXML
    void textView(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
