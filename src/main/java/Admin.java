import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    @FXML
    private TableColumn<user, String> email;

    @FXML
    private TableColumn<user, Integer> id;

    @FXML
    private TableColumn<user, String> name;

    @FXML
    private TableColumn<user, String> password;

    @FXML
    private TableView<user> tableView;
    ResultSet resultSet1 = null;
    Connection con1 = SignIn.connection();


    ObservableList<user> memberlist = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        password.setCellValueFactory(new PropertyValueFactory<>("category"));
        email.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(140);
        Callback<TableColumn<user, Void>, TableCell<user, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<user, Void> call(TableColumn<user, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Ban");

                    {
                        addButton.setOnAction(event -> {
                            user user1 = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                PreparedStatement ps = null;
                                try {
                                    ps = con1.prepareStatement("delete from users where userid = ?");
                                    ps.setInt(1, user1.getId());
                                    ps.executeUpdate();
                                    alert.setTitle("Succeed");
                                    alert.setHeaderText("Successfully");
                                    alert.show();
                                    refreshtable();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                alert.setTitle("Succeed");
                                alert.setHeaderText("Successfully");
                                alert.show();
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(addButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };
            }
        };
        tableColumn.setCellFactory(cellFactory);
        tableView.getColumns().add(tableColumn);
        
        refreshtable();
    }

    public void refreshtable() {
        memberlist.clear();
        try {
            resultSet1 = con1.createStatement().executeQuery("select distinct *from users");
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(2);
                String email = resultSet1.getString(4);
                String password = resultSet1.getString(3);
                memberlist.add(new user(id, name, password, email));
            }
            tableView.setItems(memberlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
