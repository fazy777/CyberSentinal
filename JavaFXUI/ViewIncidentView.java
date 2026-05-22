package JavaFXUI;

import Model.Incident;
import Model.User;
import DAO.IncidentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;


public class ViewIncidentView {
    public static void viewIncidents(Stage stage, User user) throws SQLException, ClassNotFoundException {
        Label title_page = new Label("Incident View");
        Button refresh = new Button("Refresh");
        Button backToDashboard = new Button("Back to Dashboard");
        AppTheme.primary(refresh);
        TableView<Incident> tableView = new TableView<Incident>();
        TableColumn<Incident,String> incidentIDColumn = new TableColumn<>("IncidentId");
        TableColumn<Incident, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Incident, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Incident, String> severityColumn = new TableColumn<>("Severity");
        TableColumn<Incident, String> statusColumn = new TableColumn<>("Status");
        TableColumn<Incident, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Incident, Timestamp> timeColumn = new TableColumn<>("Created At");
        TableColumn<Incident, String> sourceIpColumn = new TableColumn<>("Source Ip");
        TableColumn<Incident, String> sourcePortColumn = new TableColumn<>("Source Port");
        TableColumn<Incident, String> destinationIpColumn = new TableColumn<>("Destination Ip");
        TableColumn<Incident, String> destinationPortColumn = new TableColumn<>("Destination Port");
        TableColumn<Incident, Timestamp> dest_serviceColumn = new TableColumn<>("Service Provider");
        TableColumn<Incident, Integer> source_macColumn = new TableColumn<>("Source Mac");
        TableColumn<Incident, String> protocolColumn = new TableColumn<>("Protocol");
        TableColumn<Incident, String> reporterColumn = new TableColumn<>("Reported By");
        incidentIDColumn.setCellValueFactory(new PropertyValueFactory<>("incidentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        sourceIpColumn.setCellValueFactory(new PropertyValueFactory<>("sourceIp"));
        reporterColumn.setCellValueFactory(new PropertyValueFactory<>("reportedBy"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sourcePortColumn.setCellValueFactory(new PropertyValueFactory<>("srcPort"));
        destinationIpColumn.setCellValueFactory(new PropertyValueFactory<>("dest_ip"));
        destinationPortColumn.setCellValueFactory(new PropertyValueFactory<>("dst_Port"));
        source_macColumn.setCellValueFactory(new PropertyValueFactory<>("source_mac"));
        protocolColumn.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        dest_serviceColumn.setCellValueFactory(new PropertyValueFactory<>("destinationService"));

        ObservableList<Incident> data = FXCollections.observableArrayList(IncidentDAO.getAllIncidents());
        tableView.setItems(data);
        tableView.getColumns().addAll(Arrays.asList(incidentIDColumn,titleColumn, categoryColumn, severityColumn, statusColumn, sourceIpColumn,reporterColumn,timeColumn,sourcePortColumn,destinationIpColumn,destinationPortColumn,dest_serviceColumn,source_macColumn,protocolColumn,descriptionColumn));
        AppTheme.table(tableView);

        backToDashboard.setOnAction(e -> {
            DashboardView.showDashboard(stage,user);
        });
        refresh.setOnAction(e -> {
            try {
                viewIncidents(stage,user);
            } catch (SQLException | ClassNotFoundException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("View Incidents");
                alert.setHeaderText("Refresh failed");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        VBox root = new VBox(10);
        VBox root1 = new VBox(10);
        HBox hbox = new HBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root1.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        AppTheme.page(root, title_page);
        AppTheme.actionBar(hbox);
        VBox.setVgrow(root1, Priority.ALWAYS);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        hbox.getChildren().addAll(refresh,backToDashboard);
        root1.getChildren().addAll(tableView, hbox);
        root.getChildren().addAll(title_page,root1);
        Scene scene = new Scene(root,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("View Incidents");
        stage.setScene(scene);
        stage.show();
    }
}
