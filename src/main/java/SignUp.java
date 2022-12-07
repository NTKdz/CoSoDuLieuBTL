import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
            }
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
    private String TrySignUp() {
        String status = "Success";
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        if ( !isValid(email)||email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            status = "Error";
        } else {
            try {
            PreparedStatement ps = con1.prepareStatement("select * from users WHERE username=? or email=? ");
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet resultSet1 = ps.executeQuery();
            if (resultSet1.next()) {
                lblErrors.setText("profile is used");
                return status = "profile is used";
            }
            //query
            String sql = "INSERT INTO users(username,email,password,roles) VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = con1.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setInt(4,0);
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
