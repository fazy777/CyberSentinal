package JavaFXUI;

import Model.Incident;
import Model.User;
import DAO.IncidentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddIncidentView {
    public static void AddIncidentview(Stage stage, User user) throws Exception {
        ObservableList<String> categories = FXCollections.observableArrayList("Phishing", "Malware", "Ransomware", "DDoS", "Data Breach", "Insider Threat", "Zero-Day Exploit", "Other");
        ObservableList<String> severityLevels = FXCollections.observableArrayList("Low", "Medium", "High", "Critical");
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Open", "In Progress", "Resolved", "Closed");
        ObservableList<String> protocolOptions = FXCollections.observableArrayList("TCP", "HTTPS", "SFTP", "UDP");
        Label title_page = new Label("Add Incident");
        Label heading = new Label("Record the incident context, network details, and response status.");
        AppTheme.subtle(heading);
        Label status = new Label();
        TextField title_incident = new TextField();
        title_incident.setPromptText("Title");
        ComboBox<String> category_incident = new ComboBox<>(categories);
        category_incident.setPromptText("category");
        ComboBox<String> severity_incident = new ComboBox<>(severityLevels);
        severity_incident.setPromptText("severity");
        ComboBox<String> status_incident = new ComboBox<>(statusOptions);
        status_incident.setPromptText("status");
        TextField source_ip_incident = new TextField();
        source_ip_incident.setPromptText("source_ip");
        TextArea description_incident = new TextArea();
        description_incident.setPromptText("description");
        ComboBox<String> protocol_incident = new ComboBox<>(protocolOptions);
        protocol_incident.setPromptText("Protocol");
        TextField destinationService_incident = new TextField();
        destinationService_incident.setPromptText("Service-Provider");
        TextField srcPort_incident = new TextField();
        srcPort_incident.setPromptText("Source Port");
        TextField dest_ip_incident = new TextField();
        dest_ip_incident.setPromptText("Destination IP");
        TextField dst_Port_incident = new TextField();
        dst_Port_incident.setPromptText("Destination Port");
        TextField source_mac_incident = new TextField();
        source_mac_incident.setPromptText("Source MAC");
        Button submit_button = new Button("Save");
        Button cancel_button = new Button("Cancel");
        Button clear = new Button("Clear");
        AppTheme.primary(submit_button);
        AppTheme.danger(cancel_button);
        cancel_button.setOnAction(event -> {
            DashboardView.showDashboard(stage, user);
        });
        clear.setOnAction(event -> {
            clear(title_incident, category_incident, severity_incident, status_incident, source_ip_incident, description_incident, protocol_incident, destinationService_incident, srcPort_incident, dest_ip_incident, dst_Port_incident, source_mac_incident);
            status.setText("Cleared!!");
        });
        submit_button.setOnAction(event -> {
            String title = title_incident.getText();
            String categoryIncidentText = category_incident.getValue();
            String severityIncidentText = severity_incident.getValue();
            String statusIncidentText = status_incident.getValue();
            String sourceIp = source_ip_incident.getText();
            String description = description_incident.getText();
            String protocol = protocol_incident.getValue();
            String destinationService = destinationService_incident.getText();
            if (title_incident.getText().isEmpty()
                    || source_ip_incident.getText().isEmpty()
                    || category_incident.getValue() == null
                    || severity_incident.getValue() == null
                    || status_incident.getValue() == null
                    || protocol_incident.getValue() == null
                    || srcPort_incident.getText().isEmpty()
                    || dst_Port_incident.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Validation Error");
                alert.setContentText("Please fill in title, category, severity, status, protocol, source IP, and both ports before saving.");
                alert.show();
            } else {
                try {
                    int srcPort = Integer.parseInt(srcPort_incident.getText());
                    int dst_Port = Integer.parseInt(dst_Port_incident.getText());
                    String dest_ip = dest_ip_incident.getText();
                    String source_mac = source_mac_incident.getText();
                    Incident incident = IncidentDAO.addIncident(title, categoryIncidentText, severityIncidentText, statusIncidentText, sourceIp, description, user.getUserId(), protocol, destinationService, srcPort, dst_Port, dest_ip, source_mac);
                    if (incident != null) {
                        status.setText("Successfully Added!!");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    status.setText("Error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    status.setText("Invalid number format for ports");
                }
                status.setText("Submitted!");
                clear(title_incident, category_incident, severity_incident, status_incident, source_ip_incident, description_incident, protocol_incident, destinationService_incident, srcPort_incident, dest_ip_incident, dst_Port_incident, source_mac_incident);
            }
        });
        title_incident.setMaxWidth(Double.MAX_VALUE);
        category_incident.setMaxWidth(Double.MAX_VALUE);
        severity_incident.setMaxWidth(Double.MAX_VALUE);
        status_incident.setMaxWidth(Double.MAX_VALUE);
        source_ip_incident.setMaxWidth(Double.MAX_VALUE);
        description_incident.setMaxWidth(Double.MAX_VALUE);
        description_incident.setPrefRowCount(4);
        protocol_incident.setMaxWidth(Double.MAX_VALUE);
        destinationService_incident.setMaxWidth(Double.MAX_VALUE);
        srcPort_incident.setMaxWidth(Double.MAX_VALUE);
        dest_ip_incident.setMaxWidth(Double.MAX_VALUE);
        dst_Port_incident.setMaxWidth(Double.MAX_VALUE);
        source_mac_incident.setMaxWidth(Double.MAX_VALUE);
        status.setMaxWidth(400);
        status.getStyleClass().add("status-pill");

        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(12);
        form.setPadding(new Insets(22));
        AppTheme.card(form);
        addField(form, 0, "Title", title_incident);
        addField(form, 1, "Category", category_incident);
        addField(form, 2, "Severity", severity_incident);
        addField(form, 3, "Source IP", source_ip_incident);
        addField(form, 4, "Status", status_incident);
        addField(form, 5, "Protocol", protocol_incident);
        addField(form, 6, "Service Provider", destinationService_incident);
        addField(form, 7, "Source Port", srcPort_incident);
        addField(form, 8, "Destination IP", dest_ip_incident);
        addField(form, 9, "Destination Port", dst_Port_incident);
        addField(form, 10, "Source MAC", source_mac_incident);
        addField(form, 11, "Description", description_incident);

        VBox root = new VBox(14);
        HBox hbox = new HBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        AppTheme.page(root, title_page);
        hbox.setAlignment(Pos.CENTER);
        AppTheme.actionBar(hbox);
        hbox.getChildren().addAll(submit_button, cancel_button, clear);
        root.getChildren().addAll(
                title_page,
                heading,
                form,
                hbox,
                status
        );
        Scene scene = new Scene(root, 900, 600);
        AppTheme.apply(scene);
        stage.setTitle("Add Incident");
        stage.setScene(scene);
        stage.show();
    }

    private static void addField(GridPane form, int row, String labelText, Control control) {
        Label label = new Label(labelText);
        label.getStyleClass().add("subtle-text");
        int gridRow = row / 2;
        int gridColumn = (row % 2) * 2;
        form.add(label, gridColumn, gridRow);
        form.add(control, gridColumn + 1, gridRow);
        GridPane.setFillWidth(control, true);
        control.setPrefWidth(260);
    }

    private static void clear(TextField titleIncident, ComboBox<String> categoryIncident, ComboBox<String> severityIncident, ComboBox<String> statusIncident, TextField sourceIpIncident, TextArea descriptionIncident, ComboBox<String> protocolIncident, TextField destinationServiceIncident, TextField srcPortIncident, TextField destIpIncident, TextField dstPortIncident, TextField sourceMacIncident) {
        titleIncident.clear();
        categoryIncident.setValue(null);
        severityIncident.setValue(null);
        statusIncident.setValue(null);
        sourceIpIncident.clear();
        descriptionIncident.clear();
        protocolIncident.setValue(null);
        destinationServiceIncident.clear();
        srcPortIncident.clear();
        destIpIncident.clear();
        dstPortIncident.clear();
        sourceMacIncident.clear();
    }
}
