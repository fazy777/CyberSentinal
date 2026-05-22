package JavaFXUI;

import Model.Incident;
import Model.User;
import DAO.IncidentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchIncidentView {
    public static void SearchIncidentview(Stage stage, User user) throws SQLException, ClassNotFoundException  {
        Label title = new  Label("Search Incidents");
        Button id_incident = new Button("Search By ID");
        Button title_incident = new Button("Search By Title");
        Button category_incident = new Button("Search By category");
        Button severity_incident = new Button("Search By severity");
        Button status_incident = new Button("Search By status");
        Button sourceip_incident = new Button("Search By sourceip");
        Button reported_by_incident = new Button("Search By reported_by");
        Button back = new Button("Back");
        AppTheme.secondary(id_incident);
        AppTheme.secondary(title_incident);
        AppTheme.secondary(category_incident);
        AppTheme.secondary(severity_incident);
        AppTheme.secondary(status_incident);
        AppTheme.secondary(sourceip_incident);
        AppTheme.secondary(reported_by_incident);
        id_incident.setOnAction(event->{
            try {
                searchbyid(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        title_incident.setOnAction(event->{
            try{
            searchbytitle(stage,user);
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Search Failed", e.getMessage());
        }
        });
        category_incident.setOnAction(event->{
            try{
            searchbycategory(stage,user);
    } catch (SQLException | ClassNotFoundException e) {
        showAlert("Search Failed", e.getMessage());
    }
        });
        severity_incident.setOnAction(event->{
            try{
            searchbyseverity(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
        showAlert("Search Failed", e.getMessage());
            }
        });
        status_incident.setOnAction(event->{
            try{
            searchbystatus(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
        showAlert("Search Failed", e.getMessage());
            }

        });
        sourceip_incident.setOnAction(event->{
            try{
            searchbysourceip(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
        showAlert("Search Failed", e.getMessage());
            }

        });
        reported_by_incident.setOnAction(event->{
            try{
            searchbyreportedby(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
        showAlert("Search Failed", e.getMessage());
            }

        });
        back.setOnAction(event->{
            DashboardView.showDashboard(stage,user);
        });
        HBox hBox1 = new HBox(5);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(id_incident,title_incident,category_incident);
        HBox hBox2 = new HBox(5);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(severity_incident,status_incident,sourceip_incident);
        HBox hBox3 = new HBox(5);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getChildren().addAll(reported_by_incident);
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, title);
        vbox.getChildren().addAll(title,hBox1,hBox2,hBox3,back);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();

    }
    static void searchbyid(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbyid = new TextField();searchbyid.setPromptText("Search By ID");
        searchbyid.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            Integer id = parseInteger(searchbyid.getText(), "Incident ID");
            if (id == null) {
                return;
            }
            try {
                ArrayList<Incident> list = IncidentDAO.searchById(id);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbyid,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbytitle(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbytitle = new TextField();searchbytitle.setPromptText("Search By title");
        searchbytitle.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            String title = searchbytitle.getText();
            try {
                ArrayList<Incident> list = IncidentDAO.searchByTitle(title);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbytitle,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbycategory(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbycategory = new TextField();searchbycategory.setPromptText("Search By category");
        searchbycategory.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            String catagory = searchbycategory.getText();
            try {
                ArrayList<Incident> list = IncidentDAO.searchByCategory(catagory);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbycategory,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbyseverity(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbyseverity = new TextField();searchbyseverity.setPromptText("Search By severity");
        searchbyseverity.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            String severity = searchbyseverity.getText();
            try {
                ArrayList<Incident> list = IncidentDAO.searchBySeverity(severity);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbyseverity,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbystatus(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        Button search = new Button("Search");
        TextField searchbystatus = new TextField();searchbystatus.setPromptText("Search By status");
        searchbystatus.setMaxWidth(400);
        search.setOnAction(event->{
            String status = searchbystatus.getText();
            try {
                ArrayList<Incident> list = IncidentDAO.searchByStatus(status);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbystatus,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbysourceip(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbysourceip = new TextField();searchbysourceip.setPromptText("Search By sourceip");
        searchbysourceip.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            String sourceip = searchbysourceip.getText();
            try {
                ArrayList<Incident> list = IncidentDAO.searchBySourceIp(sourceip);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbysourceip,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    static void searchbyreportedby(Stage stage,User user) throws SQLException, ClassNotFoundException {
        Button back = new Button("Search By Else");
        TextField searchbyreportedby = new TextField();searchbyreportedby.setPromptText("Search By reportedby");
        searchbyreportedby.setMaxWidth(400);
        Button search = new Button("Search");
        search.setOnAction(event->{
            Integer reportedby = parseInteger(searchbyreportedby.getText(), "Reporter ID");
            if (reportedby == null) {
                return;
            }
            try {
                ArrayList<Incident> list = IncidentDAO.searchByReported_BY(reportedby);
                show(stage,list,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException  | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(5);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, null);
        AppTheme.actionBar(hbox);
        AppTheme.primary(search);
        hbox.getChildren().addAll(search,back);
        vbox.getChildren().addAll(searchbyreportedby,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Search");
        stage.setScene(scene);
        stage.show();
    }
    public static void show(Stage stage,ArrayList<Incident> incidents,User user) throws SQLException, ClassNotFoundException {
        Label Title  = new Label("Incidents");
        Button Refresh = new Button("Refresh");
        Button back = new Button("Back");
        AppTheme.primary(Refresh);
        TableView<Incident> tableView = new TableView<>();
        TableColumn<Incident,String> incidentIDColumn = new TableColumn<>("IncidentId");incidentIDColumn.setCellValueFactory(new PropertyValueFactory<>("incidentId"));
        TableColumn<Incident, String> titleColumn = new TableColumn<>("Title");titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Incident, String> categoryColumn = new TableColumn<>("Category");categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Incident, String> severityColumn = new TableColumn<>("Severity");severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        TableColumn<Incident, String> statusColumn = new TableColumn<>("Status");statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Incident, String> sourceIpColumn = new TableColumn<>("Source Ip");sourceIpColumn.setCellValueFactory(new PropertyValueFactory<>("sourceIp"));
        TableColumn<Incident, String> descriptionColumn = new TableColumn<>("Description");descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Incident, String> reporterColumn = new TableColumn<>("Reported By");reporterColumn.setCellValueFactory(new PropertyValueFactory<>("reportedBy"));
        TableColumn<Incident, Timestamp> timeColumn = new TableColumn<>("Created At");timeColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        TableColumn<Incident, String> sourcePortColumn = new TableColumn<>("Source Port");sourcePortColumn.setCellValueFactory(new PropertyValueFactory<>("srcPort"));
        TableColumn<Incident, String> destinationIpColumn = new TableColumn<>("Destination Ip");destinationIpColumn.setCellValueFactory(new PropertyValueFactory<>("dest_ip"));
        TableColumn<Incident, String> destinationPortColumn = new TableColumn<>("Destination Port");destinationPortColumn.setCellValueFactory(new PropertyValueFactory<>("dst_Port"));
        TableColumn<Incident, Timestamp> dest_serviceColumn = new TableColumn<>("Service Provider");dest_serviceColumn.setCellValueFactory(new PropertyValueFactory<>("destinationService"));
        TableColumn<Incident, Integer> source_macColumn = new TableColumn<>("Source Mac");source_macColumn.setCellValueFactory(new PropertyValueFactory<>("source_mac"));
        TableColumn<Incident, String> protocolColumn = new TableColumn<>("Protocol");protocolColumn.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        ObservableList<Incident> data = FXCollections.observableArrayList(incidents);
        tableView.getColumns().addAll(Arrays.asList(incidentIDColumn,titleColumn, categoryColumn, severityColumn, statusColumn, sourceIpColumn,reporterColumn,timeColumn,sourcePortColumn,destinationIpColumn,destinationPortColumn,dest_serviceColumn,source_macColumn,protocolColumn,descriptionColumn));
        tableView.setItems(data);
        AppTheme.table(tableView);
        Refresh.setOnAction(event->{
            try {
                show(stage,incidents,user);
            } catch (SQLException |ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        back.setOnAction(event->{
            try {
                SearchIncidentview(stage,user);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert("Search Failed", e.getMessage());
            }
        });
        VBox vbox = new VBox(10);
        HBox hbox = new HBox(10);
        vbox.setAlignment(Pos.TOP_CENTER);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, Title);
        AppTheme.actionBar(hbox);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        hbox.getChildren().addAll(Refresh,back);
        vbox.getChildren().addAll(Title,tableView,hbox);
        Scene scene = new Scene(vbox,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Search Incidents");
        stage.setScene(scene);
        stage.show();
    }

    private static Integer parseInteger(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showAlert("Validation Error", fieldName + " is required.");
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            showAlert("Invalid Number", fieldName + " must be a whole number.");
            return null;
        }
    }

    private static void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incident Search");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
