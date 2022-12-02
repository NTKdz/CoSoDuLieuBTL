import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addProducts implements Initializable {
    @FXML
    private Button Newadd;

    @FXML
    private Button Newcancel;

    @FXML
    private TextField Newcategory;

    @FXML
    private TextField Newprice;

    @FXML
    private TextField Newquantity;

    @FXML
    private TextField productname;

    @FXML
    private Text text;

    void add() {
        try {
            Connection con = SignIn.connection();
            PreparedStatement statement = con.prepareStatement
                    ("insert into products(name,category,quantity,price) values (?,?,?,?)");
            statement.setString(1, productname.getText());
            statement.setString(2, Newcategory.getText());
            statement.setInt(3, Integer.parseInt(Newquantity.getText()));
            statement.setDouble(4, Double.parseDouble(Newprice.getText()));
            statement.execute();
            customer.checkpress = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onAddClicked(ActionEvent event) {
        Stage stage = (Stage) Newcancel.getScene().getWindow();
        stage.close();
        add();
    }

    @FXML
    void onCancelClicked(ActionEvent event) {
        Stage stage = (Stage) Newcancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
