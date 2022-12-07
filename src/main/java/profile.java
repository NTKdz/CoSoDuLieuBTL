import com.mysql.cj.protocol.Resultset;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class profile implements Initializable {
    @FXML
    private Button back;
    @FXML
    private Button confirm;

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Label password;

    @FXML
    void click(ActionEvent event) throws SQLException {
        PreparedStatement ps;
        Connection con1 = SignIn.connection();
        String ema = email.getText();
        String username = name.getText();
        String pass = password.getText();
        if (!isValid(ema)) {
            ps = con1.prepareStatement("UPDATE users set email=? WHERE userid=?");
            ps.setString(1, ema);
            ps.setInt(2, SignIn.id);
            ps.executeUpdate();
        }
        if(!username.isEmpty()){
            ps = con1.prepareStatement("UPDATE users set username=? WHERE userid=?");
            ps.setString(1, username);
            ps.setInt(2, SignIn.id);
            ps.executeUpdate();
            System.out.println(1);
        }
        if(!pass.isEmpty()){
            ps = con1.prepareStatement("UPDATE users set password=? WHERE userid=?");
            ps.setString(1, pass);
            ps.setInt(2, SignIn.id);
            ps.executeUpdate();
        }
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection con=SignIn.connection();
        try {
            PreparedStatement ps = con.prepareStatement("select *from users where userid=?");
            ps.setInt(1,SignIn.id);
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                name.setText(resultSet.getString(2));
                email.setText(resultSet.getString(4));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/OnBoard.fxml")));
        stage.setScene(scene);
        stage.show();
    }


}