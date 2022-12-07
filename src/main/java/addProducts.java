import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    Connection con1 = SignIn.connection();
    ResultSet resultSet1 = null;

    void add() {
        try {
            PreparedStatement ps = con1.prepareStatement("select quantity from products WHERE userid=? and name = ?");
            ps.setInt(1, SignIn.id);
            ps.setString(2, productname.getText());
            resultSet1 = ps.executeQuery();
            if (resultSet1.next()) {
                int quantity = resultSet1.getInt(1) + 1;
                ps = con1.prepareStatement("UPDATE products set quantity=? WHERE userid=? and name = ?");
                ps.setInt(1, quantity);
                ps.setInt(2, SignIn.id);
                ps.setString(3, productname.getText());
                ps.executeUpdate();
            } else {
                Connection con = SignIn.connection();
                PreparedStatement statement = con.prepareStatement
                        ("insert into products(name,categoryid,quantity,price,userid) values (?,?,?,?,?)");
                statement.setString(1, productname.getText());
                statement.setString(2, Newcategory.getText());
                statement.setInt(3, Integer.parseInt(Newquantity.getText()));
                statement.setDouble(4, Double.parseDouble(Newprice.getText()));
                statement.setInt(5, SignIn.id);
                System.out.println(SignIn.id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onAddClicked(ActionEvent event) throws IOException {
        add();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/seller/seller.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onCancelClicked(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/seller/seller.fxml")));
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
