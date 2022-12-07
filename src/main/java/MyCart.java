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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyCart implements Initializable {

    @FXML
    private Button checkout;

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
                    private final Button removeButton = new Button("remove");

                    {
                        removeButton.setOnAction(event -> {
                            Products product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                PreparedStatement ps = null;
                                try {
                                    ps = con1.prepareStatement("delete from cart where productid = ?");
                                    ps.setInt(1, product.getId());
                                    ps.executeUpdate();
                                    alert.setTitle("Succeed");
                                    alert.setHeaderText("Successfully");
                                    alert.show();
                                    refreshtable();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(removeButton);
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
            PreparedStatement ps=con1.prepareStatement("select distinct products.productid,products.name,products.categoryid,cart.quantity,products.price from cart \n" +
                    "inner join products on products.productid=cart.productid inner join users on cart.userid=?");
            ps.setInt(1,SignIn.id);
            resultSet1 = ps.executeQuery();
            while (resultSet1.next()) {
                int id = resultSet1.getInt(1);
                String name = resultSet1.getString(2);
                String category = resultSet1.getString(3);
                int quantity = resultSet1.getInt(4);
                double price = resultSet1.getDouble(5);
                cartlist.add(new Products(id, name, category, quantity, price));
                System.out.println(1);
            }
            table.setItems(cartlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void checkout(ActionEvent event) {
        try {
            int orderid = 0;
            PreparedStatement ps = con1.prepareStatement("select * from orders where orderid = (select max(orderid) from orders)");
            updateId();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                orderid = rs.getInt("orderid") + 1;
            }
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            PreparedStatement ps1 = con1.prepareStatement("insert into orders values (?, ?, ?)");
            ps1.setInt(1, orderid);
            ps1.setInt(2, SignIn.id);
            ps1.setString(3, date.format(Calendar.getInstance().getTime()));

            ps1.executeUpdate();

            for (int i = 0; i < table.getItems().size(); i++) {
                Products product = table.getItems().get(i);
                ps = con1.prepareStatement("select quantity from products WHERE userid=? and name = ?");
                ps.setInt(1, SignIn.id);
                ps.setString(2, product.getName());
                resultSet1 = ps.executeQuery();
                if (resultSet1.next()) {
                    ps = con1.prepareStatement("UPDATE products set quantity=? WHERE userid=? and name = ?");
                    ps.setInt(1, product.getQuantity() + 1);
                    ps.setInt(2, SignIn.id);
                    ps.setString(3, product.getName());
                    ps.executeUpdate();
                } else {
                    PreparedStatement statement = con1.prepareStatement
                            ("insert into products(name,categoryid,quantity,price,userid) values (?,?,?,?,?)");
                    statement.setString(1, product.getName());
                    statement.setString(2, product.getCategory());
                    statement.setInt(3, product.getQuantity());
                    statement.setDouble(4, product.getPrice());
                    statement.setInt(5, SignIn.id);
                    System.out.println(SignIn.id);
                    statement.execute();
                }
            }
            cartlist.clear();
            table.setItems(cartlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateId() {
        try {
            PreparedStatement pss = con1.prepareStatement("set foreign_key_checks = 0");
            pss.executeUpdate();

            int orderid = 0;
            PreparedStatement ps1 = con1.prepareStatement("select * from orders where orderid = (select max(orderid) from orders)");
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                orderid = rs.getInt("orderid") + 1;
            }

            PreparedStatement ps = con1.prepareStatement("update login.cart set orderid = " + orderid + " where userid = " + SignIn.id + "");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/customer/customer.fxml")));
        scene.getStylesheets().add(getClass().getResource("css/Mycss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
