
import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class customer implements Initializable {

    @FXML
    private TableColumn<Products, Integer> quantity;
    @FXML
    private Button cartbut;
    @FXML
    private TableColumn<Products, String> category;
    @FXML
    private TableColumn<Products, Integer> id;
    @FXML
    private Button search;

    @FXML
    private TableColumn<Products, String> name;
    @FXML
    private TableColumn<Products, Double> price;
    @FXML
    private TextField searchFiled;
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
        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(140);
        Callback<TableColumn<Products, Void>, TableCell<Products, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Products, Void> call(TableColumn<Products, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Add to cart");

                    {
                        addButton.setOnAction(event -> {
                            Products product = getTableView().getItems().get(getIndex());
                            try {
                                if(product.getQuantity()>0) {
                                    int id = product.getId();
                                    int userid = SignIn.id;
                                    System.out.println(userid);
                                    PreparedStatement ps = con1.prepareStatement("select quantity from cart WHERE userid=? and productid = ?");
                                    ps.setInt(1, SignIn.id);
                                    ps.setInt(2, product.getId());
                                    resultSet1 = ps.executeQuery();
                                    if (resultSet1.next()) {
                                        int quantity = resultSet1.getInt(1) + 1;
                                        ps = con1.prepareStatement("UPDATE cart set quantity=? WHERE userid=? and productid = ?");
                                        ps.setInt(1, quantity);
                                        ps.setInt(2, SignIn.id);
                                        ps.setInt(3, product.getId());
                                        ps.executeUpdate();
                                    } else {
                                        PreparedStatement statement = con1.prepareStatement("insert into cart(productid,quantity,userid) values (?,?,?)");
                                        statement.setInt(1, id);
                                        statement.setInt(2, 1);
                                        statement.setInt(3, userid);
                                        statement.execute();
                                    }
                                    ps = con1.prepareStatement("UPDATE products set quantity=? WHERE productid = ?");
                                    ps.setInt(1, product.getQuantity() - 1);
                                    ps.setInt(2, product.getId());
                                    ps.executeUpdate();
                                    refreshtable();
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
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
            resultSet1 = con1.createStatement().executeQuery("select distinct *from products");
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(3);
                String category = resultSet1.getString(2);
                int quantity = resultSet1.getInt(5);
                double price = resultSet1.getDouble(4);
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
    void seeCart(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/customer/MyCart.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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

    @FXML
    void search(ActionEvent event) {
        System.out.println(1);
        productlist.clear();
        if (searchFiled.getText().trim().isEmpty()) refreshtable();
        else {
            try {
                PreparedStatement p1 = con1.prepareStatement("SELECT * FROM products WHERE " +
                        "name LIKE ? or categoryid LIKE ?");
                p1.setString(1, "%" + searchFiled.getText() + "%");
                p1.setString(2, "%" + searchFiled.getText() + "%");
                resultSet1 = p1.executeQuery();
                while (resultSet1.next()) {
                    int id = resultSet1.getInt(1);
                    String name = resultSet1.getString(3);
                    String category = resultSet1.getString(2);
                    int quantity = resultSet1.getInt(5);
                    double price = resultSet1.getDouble(4);
                    productlist.add(new Products(id, name, category, quantity, price));
                }
                table.setItems(productlist);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
