import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUp implements Initializable {
    @FXML
    private Button btnSignup;

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstname;

    @FXML
    private TextField txtLastname;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    Connection con1 = null;

    @FXML
    void SigningUp(MouseEvent event) {
        if (event.getSource() == btnSignup) {
            //neu an dang nhap
            if (TrySignUp().equals("Success")) {
                System.out.println("fsafdsfsa");
            }
        }
    }

    private String TrySignUp() {
        String status = "Success";
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            status = "Error";
        } else {
            //query
            String sql = "INSERT INTO userLogin(username,email,password) VALUES (?,?,?)";
            try {
                PreparedStatement preparedStatement = con1.prepareStatement(sql);
                preparedStatement.setString(2, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.execute();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con1 = SignIn.connection();
        if (con1 == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }
}
