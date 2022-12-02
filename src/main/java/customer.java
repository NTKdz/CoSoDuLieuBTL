
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class customer implements Initializable {
    @FXML
    public Button addproducts;
    @FXML
    private TableColumn<Products, Integer> quantity;
    @FXML
    private Button cartbut;
    @FXML
    private TableColumn<Products, String> category;
    @FXML
    private TableColumn<Products, Integer> id;
    @FXML
    private Text label1;
    @FXML
    private TableColumn<Products, String> name;
    @FXML
    private TableColumn<Products, Double> price;
    @FXML
    private Button productBut;
    @FXML
    private TextField searchFiled;
    @FXML
    private TableView<Products> table;

    public static boolean checkpress = false;

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
        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(140);
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
        con1 = SignIn.connection();
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

    public void addProductsPress(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/NewProducts.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
