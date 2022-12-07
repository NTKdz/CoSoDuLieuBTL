import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class seller implements Initializable {
    @FXML
    private Button back;

    @FXML
    public Button addproducts;
    @FXML
    private TableColumn<Products, String> category;

    @FXML
    private TableColumn<Products, Integer> id;

    @FXML
    private TableColumn<Products, String> name;

    @FXML
    private TableColumn<Products, Double> price;

    @FXML
    private Button products;

    @FXML
    private TextField searchfield;

    @FXML
    private Button search;

    @FXML
    private Button orderDetail;

    @FXML
    private TableColumn<Products, Integer> quantity;

    @FXML
    private TableColumn<Products, Double> sales;

    @FXML
    private TableView<Products> tableView;

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
        Callback<TableColumn<Products, Void>, TableCell<Products, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Products, Void> call(TableColumn<Products, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("remove");

                    {
                        addButton.setOnAction(event -> {
                            Products product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                PreparedStatement ps = null;
                                try {
                                    ps = con1.prepareStatement("delete from products where productid = ?");
                                    ps.setInt(1, product.getId());
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

        //set editable to tableview
        TableColumn editCol = new TableColumn<>("Edits");
        Callback<TableColumn<Products, Void>, TableCell<Products, Void>> edit = new Callback<>() {
            @Override
            public TableCell<Products, Void> call(TableColumn<Products, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("edit");

                    {
                        editButton.setOnAction(event -> {
                            Products product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                PreparedStatement ps = null;
                                try {
                                    System.out.println(product.getName());
                                    ps = con1.prepareStatement("UPDATE products set name=? , price=? , quantity=? WHERE productid = ?;");
                                    ps.setString(1, product.getName());
                                    ps.setDouble(2,product.getPrice());
                                    ps.setInt(3,product.getQuantity());
                                    ps.setInt(4, product.getId());
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
                        buttonsPane.getChildren().add(editButton);
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
        editCol.setCellFactory(edit);
        tableView.getColumns().add(editCol);

        tableView.setEditable(true);

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, String> productsStringCellEditEvent) {
                Products product = productsStringCellEditEvent.getRowValue();
                product.setName(productsStringCellEditEvent.getNewValue());
            }
        });

        quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, Integer> productsStringCellEditEvent) {
                Products product = productsStringCellEditEvent.getRowValue();
                product.setQuantity(productsStringCellEditEvent.getNewValue());
            }
        });

        price.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        price.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, Double> productsStringCellEditEvent) {
                Products product = productsStringCellEditEvent.getRowValue();
                product.setPrice(productsStringCellEditEvent.getNewValue());
            }
        });
        con1 = SignIn.connection();
        refreshtable();
    }

    public void refreshtable() {
        productlist.clear();
        try {
            PreparedStatement statement = con1.prepareStatement("select distinct *from products where userid=?");
            statement.setInt(1, SignIn.id);
            resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(3);
                String category = resultSet1.getString(2);
                int quantity = resultSet1.getInt(5);
                double price = resultSet1.getDouble(4);
                productlist.add(new Products(id, name, category, quantity, price));
            }
            tableView.setItems(productlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addProductsPress(ActionEvent actionEvent) {
        try {
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/seller/NewProducts.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void search(ActionEvent event) {
        System.out.println(1);
        productlist.clear();
        if (searchfield.getText().trim().isEmpty()) refreshtable();
        else {
            try {
                PreparedStatement p1 = con1.prepareStatement("SELECT * FROM products WHERE " +
                        "name LIKE ? or categoryid LIKE ?");
                p1.setString(1, "%" + searchfield.getText() + "%");
                p1.setString(2, "%" + searchfield.getText() + "%");
                resultSet1 = p1.executeQuery();
                while (resultSet1.next()) {
                    int id = resultSet1.getInt(1);
                    String name = resultSet1.getString(3);
                    String category = resultSet1.getString(2);
                    int quantity = resultSet1.getInt(5);
                    double price = resultSet1.getDouble(4);
                    productlist.add(new Products(id, name, category, quantity, price));
                }
                tableView.setItems(productlist);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void detailClicked(ActionEvent event) {

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
