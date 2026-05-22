package JavaFXUI;

import Model.User;
import DAO.ReportDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

public class DashboardView{
    public static void showDashboard(Stage stage, User user){
        Label welcome = new Label("CyberSentinel Dashboard");
        Label  role_title = new Label("Role");
        Label user_title = new Label("Signed in as");
        Label UserName = new Label(user.getUsername());
        Label rol = new Label(user.getRole());
        Button AddIncidents = new Button("Add Incident");
        Button View_Incidents = new Button("View Incidents");
        Button Update_Status = new Button("Update Status");
        Button Search_Incidents = new Button("Search Incidents");
        Button packetscanning= new Button("Packet Scanning");
        Button portscanner = new Button("Port Scanner");
        Button Reports = new Button("Reports");
        Button Export_Data = new Button("Export Data");
        Button logout = new Button("Logout");
        AppTheme.primary(AddIncidents);
        AppTheme.secondary(packetscanning);
        AppTheme.secondary(portscanner);
        AppTheme.danger(logout);
        AddIncidents.setOnAction(e -> {
            try {
                AddIncidentView.AddIncidentview(stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open Add Incident", ex.getMessage());
            }
        });
        packetscanning.setOnAction(e -> {
            try {
                List<PcapNetworkInterface> devices = Pcaps.findAllDevs();
                PacketScanningView.showDeviceSelectionDialog(devices,stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open Packet Scanning", ex.getMessage());
            }
        });
        portscanner.setOnAction(e -> {
            PortScannerView.showPortScanner(stage, user);
        });
        View_Incidents.setOnAction(e -> {
            try {
                ViewIncidentView.viewIncidents(stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open incidents", ex.getMessage());
            }
        });
        Update_Status.setOnAction(e -> {
            try {
                UpdateIncidentView.viewIncident(stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open update screen", ex.getMessage());
            }
        });
        Search_Incidents.setOnAction(e -> {
            try {
                SearchIncidentView.SearchIncidentview(stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open search", ex.getMessage());
            }
        });
        Reports.setOnAction(e -> {
            try {
                ExportView.Export(stage,user);
            } catch (Exception ex) {
                showAlert("Unable to open reports", ex.getMessage());
            }
        });
        Export_Data.setOnAction(e -> {
            try {
                boolean success = ReportDAO.file_generation();
                if(success){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Successful");
                    alert.setHeaderText("Export Successful");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                showAlert("Export failed", ex.getMessage());
            }
        });
        logout.setOnAction(e -> {
            stage.close();
        });

        VBox root = new VBox(18);
        AppTheme.page(root, welcome);
        root.setAlignment(Pos.CENTER);

        HBox profile = new HBox(12);
        profile.setAlignment(Pos.CENTER);
        role_title.getStyleClass().add("subtle-text");
        user_title.getStyleClass().add("subtle-text");
        rol.getStyleClass().add("status-pill");
        UserName.getStyleClass().add("status-pill");
        profile.getChildren().addAll(user_title, UserName, role_title, rol);

        GridPane actionGrid = new GridPane();
        actionGrid.setHgap(12);
        actionGrid.setVgap(12);
        actionGrid.setAlignment(Pos.CENTER);
        actionGrid.setPadding(new Insets(22));
        AppTheme.card(actionGrid);
        Button[] actions = {AddIncidents, View_Incidents, Update_Status, Search_Incidents, packetscanning, portscanner, Reports, Export_Data, logout};
        for (Button action : actions) {
            action.setMaxWidth(Double.MAX_VALUE);
            action.setMinHeight(42);
            GridPane.setHgrow(action, Priority.ALWAYS);
        }
        actionGrid.add(AddIncidents, 0, 0);
        actionGrid.add(View_Incidents, 1, 0);
        actionGrid.add(Update_Status, 2, 0);
        actionGrid.add(Search_Incidents, 0, 1);
        actionGrid.add(packetscanning, 1, 1);
        actionGrid.add(portscanner, 2, 1);
        actionGrid.add(Reports, 0, 2);
        actionGrid.add(Export_Data, 1, 2);
        actionGrid.add(logout, 2, 2);

        root.getChildren().addAll(welcome, profile, actionGrid);
        Scene dashboardScene = new Scene(root,900,600);
        AppTheme.apply(dashboardScene);
        stage.setTitle("CyberSentinel Dashboard");
        stage.setScene(dashboardScene);
    }

    private static void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CyberSentinel");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
