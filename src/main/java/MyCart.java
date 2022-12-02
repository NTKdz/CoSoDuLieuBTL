import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyCart implements Initializable {
    @FXML
    private Button back;

    @FXML
    private TableColumn<Products, String> cartcategory;

    @FXML
    private TableColumn<Products, Integer> cartid;

    @FXML
    private TableColumn<Products, String> cartname;

    @FXML
    private TableColumn<Products, Double> cartprice;

    @FXML
    private TableColumn<Products, Integer> cartquantity;

    @FXML
    private TableView<Products> table;

    Connection con1 = SignIn.connection();

    ObservableList<Products> cartlist = FXCollections.observableArrayList();

    ResultSet resultSet1 = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cartid.setCellValueFactory(new PropertyValueFactory<>("id"));
        cartname.setCellValueFactory(new PropertyValueFactory<>("name"));
        cartquantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn tableColumn = new TableColumn<>("Actions");
        Callback<TableColumn<Products, Void>, TableCell<Products, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Products, Void> call(TableColumn<Products, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Add to cart");

                    {
                        addButton.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
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
        table.getColumns().add(tableColumn);
        refreshtable();
    }
    public void refreshtable() {
        cartlist.clear();
        try {
            resultSet1 = con1.createStatement().executeQuery("select *from products");
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(2);
                String category = resultSet1.getString(3);
                int quantity = resultSet1.getInt(4);
                double price = resultSet1.getDouble(5);
                cartlist.add(new Products(id, name, category, quantity, price));
            }
            table.setItems(cartlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
