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

public class MyProducts implements Initializable {
    @FXML
    private TableColumn<Products, Integer> quantity;
    @FXML
    private TableColumn<Products, String> category;
    @FXML
    private TableColumn<Products, Integer> id;
    @FXML
    private TableColumn<Products, String> name;
    @FXML
    private TableColumn<Products, Double> price;
    @FXML
    private TableView<Products> table;

    ResultSet resultSet1 = null;
    Connection con1 = SignIn.connection();

    ObservableList<Products> productlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn tableColumn = new TableColumn<>("check");
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
        productlist.clear();
        try {
            resultSet1 = con1.createStatement().executeQuery("select *from products");
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(2);
                String category = resultSet1.getString(3);
                int quantity = resultSet1.getInt(4);
                double price = resultSet1.getDouble(5);
                productlist.add(new Products(id, name, category, quantity, price));
            }
            table.setItems(productlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
