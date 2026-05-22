package JavaFXUI;

import Model.Ports;
import Model.User;
import Npcap.PortScanning;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.ArrayList;
import java.util.List;

public class PortScannerView {
    private static ObservableList<Ports> scanResults = FXCollections.observableArrayList();
    private static TextField targetIpField;
    private static TextField startPortField;
    private static TextField endPortField;
    private static ProgressBar progressBar;
    private static Label statusLabel;
    private static TableView<Ports> tableView;

    @SuppressWarnings("")
    public static void showPortScanner(Stage stage, User user) {
        showPortScanner(stage, user, "", null, true);
    }

    @SuppressWarnings("")
    public static void showPortScanner(Stage stage, User user, String initialTargetIp) {
        showPortScanner(stage, user, initialTargetIp, null, true);
    }

    @SuppressWarnings("")
    public static void showPortScanner(Stage stage, User user, String initialTargetIp, PcapNetworkInterface selectedDevice) {
        showPortScanner(stage, user, initialTargetIp, selectedDevice, false);
    }

    @SuppressWarnings({ "", "unchecked" })
    private static void showPortScanner(Stage stage, User user, String initialTargetIp, PcapNetworkInterface selectedDevice, boolean askForDevice) {
        if (askForDevice && selectedDevice == null) {
            selectedDevice = showDeviceSelectionDialog();
            if (selectedDevice == null) {
                return;
            }
        }
        PortScanning.setSelectedDevice(selectedDevice);

        Label titleLabel = new Label("Port Scanner");
        Label deviceLabel = new Label("Device: " + PortScanning.getDeviceName());
        AppTheme.subtle(deviceLabel);

        Label targetLabel = new Label("Target IP:");
        targetIpField = new TextField();
        targetIpField.setPromptText("e.g., 192.168.1.1");
        if (initialTargetIp != null) {
            targetIpField.setText(initialTargetIp.trim());
        }
        targetIpField.setPrefWidth(200);

        Label startLabel = new Label("Start Port:");
        startPortField = new TextField();
        startPortField.setText("1");
        startPortField.setPrefWidth(80);

        Label endLabel = new Label("End Port:");
        endPortField = new TextField();
        endPortField.setText("1024");
        endPortField.setPrefWidth(80);

        Label timeoutLabel = new Label("Timeout (ms):");
        TextField timeoutField = new TextField();
        timeoutField.setText("5000");
        timeoutField.setPrefWidth(80);

        Label filterLabel = new Label("Show:");
        ComboBox<String> statusFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Results",
                "Open",
                "Closed",
                "Filtered",
                "Unreachable",
                "Invalid Host"
        ));
        statusFilter.setValue("All Results");
        statusFilter.setPrefWidth(150);
        TextField searchField = new TextField();
        searchField.setPromptText("Search port or service");
        searchField.setPrefWidth(180);

        Button scanButton = new Button("Start Scan");
        Button stopButton = new Button("Stop");
        Button quickScanButton = new Button("Quick Scan (Common Ports)");
        Button saveButton = new Button("Save Results");
        Button clearButton = new Button("Clear Results");
        Button backButton = new Button("Back to Dashboard");
        AppTheme.primary(scanButton);
        AppTheme.secondary(quickScanButton);
        AppTheme.danger(stopButton);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setVisible(false);

        statusLabel = new Label("");
        statusLabel.getStyleClass().add("status-pill");

        TableColumn<Ports, Integer> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(new PropertyValueFactory<>("port_number"));
        portCol.setPrefWidth(80);

        TableColumn<Ports, String> protocolCol = new TableColumn<>("Protocol");
        protocolCol.setCellValueFactory(new PropertyValueFactory<>("protocols"));
        protocolCol.setPrefWidth(80);

        TableColumn<Ports, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("service_name"));
        serviceCol.setPrefWidth(120);

        TableColumn<Ports, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("port_status"));
        statusCol.setPrefWidth(100);

        TableColumn<Ports, String> riskCol = new TableColumn<>("Risk Level");
        riskCol.setCellValueFactory(new PropertyValueFactory<>("risk_level"));
        riskCol.setPrefWidth(100);

        tableView = new TableView<>();
        tableView.getColumns().addAll(portCol, protocolCol, serviceCol, statusCol, riskCol);
        FilteredList<Ports> filteredResults = new FilteredList<>(scanResults, port -> true);
        tableView.setItems(filteredResults);
        tableView.setPrefHeight(300);
        AppTheme.table(tableView);

        Runnable updateFilter = () -> filteredResults.setPredicate(port -> {
            String selectedStatus = statusFilter.getValue();
            String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

            boolean statusMatches = selectedStatus == null
                    || selectedStatus.equals("All Results")
                    || port.getPort_status().equalsIgnoreCase(selectedStatus);

            boolean queryMatches = query.isEmpty()
                    || String.valueOf(port.getPort_number()).contains(query)
                    || port.getService_name().toLowerCase().contains(query)
                    || port.getProtocols().toLowerCase().contains(query)
                    || port.getRisk_level().toLowerCase().contains(query);

            return statusMatches && queryMatches;
        });
        statusFilter.setOnAction(e -> updateFilter.run());
        searchField.textProperty().addListener((obs, oldText, newText) -> updateFilter.run());

        scanButton.setOnAction(e -> {
            String targetIp = targetIpField.getText().trim();
            if (targetIp.isEmpty()) {
                showAlert("Please enter a target IP address");
                return;
            }
            try {
                int startPort = Integer.parseInt(startPortField.getText());
                int endPort = Integer.parseInt(endPortField.getText());
                int timeout = Integer.parseInt(timeoutField.getText());

                if (startPort < 1 || endPort > 65535 || startPort > endPort) {
                    showAlert("Invalid port range. Ports must be between 1-65535.");
                    return;
                }

                scanResults.clear();
                progressBar.setVisible(true);
                progressBar.setProgress(0);
                statusLabel.setText("Scanning " + targetIp + "...");

                PortScanning.setResultConsumer(result -> {
                    Ports port = new Ports(result.port, result.protocol, result.service, result.status, targetIp);
                    Platform.runLater(() -> scanResults.add(port));
                });

                PortScanning.setProgressConsumer(progress -> {
                    Platform.runLater(() -> {
                        progressBar.setProgress(progress / 100.0);
                        statusLabel.setText(progress >= 100 ? "Scan completed" : "Scanning... " + progress + "%");
                    });
                });

                PortScanning.scanPorts(targetIp, startPort, endPort, timeout);

            } catch (NumberFormatException ex) {
                showAlert("Please enter valid numbers for ports and timeout");
            }
        });

        stopButton.setOnAction(e -> {
            PortScanning.stopScanning();
            statusLabel.setText("Scan stopped");
        });

        quickScanButton.setOnAction(e -> {
            String targetIp = targetIpField.getText().trim();
            if (targetIp.isEmpty()) {
                showAlert("Please enter a target IP address");
                return;
            }

            scanResults.clear();
            progressBar.setVisible(true);
            progressBar.setProgress(0);
            statusLabel.setText("Quick scanning common ports...");

            PortScanning.setResultConsumer(result -> {
                Ports port = new Ports(result.port, result.protocol, result.service, result.status, targetIp);
                Platform.runLater(() -> scanResults.add(port));
            });

            PortScanning.setProgressConsumer(progress -> {
                Platform.runLater(() -> {
                    progressBar.setProgress(progress / 100.0);
                    statusLabel.setText(progress >= 100 ? "Quick scan completed" : "Quick scanning common ports... " + progress + "%");
                });
            });

            int minPort = 1;
            int maxPort = 1024;
            try {
                int timeout = Integer.parseInt(timeoutField.getText());
                PortScanning.scanPorts(targetIp, minPort, maxPort, timeout);
            } catch (NumberFormatException ex) {
                showAlert("Please enter a valid timeout");
            }
        });

        saveButton.setOnAction(e -> {
            if (scanResults.isEmpty()) {
                showAlert("No results to save");
                return;
            }
            try {
                ArrayList<Ports> openPorts = new ArrayList<>();
                for (Ports p : scanResults) {
                    if (p.getPort_status().equals("OPEN")) {
                        openPorts.add(p);
                    }
                }
                if (!openPorts.isEmpty()) {
                    DAO.PortsDAO.saveScanResults(openPorts);
                    showAlert("Saved " + openPorts.size() + " open ports to database");
                } else {
                    showAlert("No open ports to save");
                }
            } catch (Exception ex) {
                showAlert("Error saving results: " + ex.getMessage());
            }
        });

        clearButton.setOnAction(e -> {
            scanResults.clear();
            statusLabel.setText("");
            progressBar.setProgress(0);
        });

        backButton.setOnAction(e -> {
            DashboardView.showDashboard(stage, user);
        });

        FlowPane inputBox = new FlowPane(10, 10);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getStyleClass().add("surface-card");
        inputBox.setStyle("-fx-padding: 16;");
        inputBox.getChildren().addAll(targetLabel, targetIpField, startLabel, startPortField,
                endLabel, endPortField, timeoutLabel, timeoutField);

        FlowPane filterBox = new FlowPane(10, 10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.getStyleClass().add("surface-card");
        filterBox.setStyle("-fx-padding: 14;");
        filterBox.getChildren().addAll(filterLabel, statusFilter, searchField);

        FlowPane buttonBox = new FlowPane(10, 10);
        buttonBox.setAlignment(Pos.CENTER);
        AppTheme.actionBar(buttonBox);
        buttonBox.getChildren().addAll(scanButton, stopButton, quickScanButton, saveButton, clearButton, backButton);

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        AppTheme.page(root, titleLabel);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        root.getChildren().addAll(titleLabel, deviceLabel, inputBox, buttonBox, filterBox, progressBar, statusLabel, tableView);

        Scene scene = new Scene(root,1900,900);
        AppTheme.apply(scene);
        stage.setTitle("Port Scanner");
        stage.setScene(scene);
        stage.show();
    }

    private static PcapNetworkInterface showDeviceSelectionDialog() {
        try {
            List<PcapNetworkInterface> devices = Pcaps.findAllDevs();
            if (devices == null || devices.isEmpty()) {
                showAlert("No network devices found");
                return null;
            }

            Dialog<PcapNetworkInterface> dialog = new Dialog<>();
            dialog.setTitle("Select Network Device");
            dialog.setHeaderText("Please select a network interface for port scanning");
            ButtonType confirmButton = new ButtonType("Open Port Scanner", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);
            AppTheme.dialog(dialog.getDialogPane());

            ComboBox<PcapNetworkInterface> comboBox = new ComboBox<>(FXCollections.observableArrayList(devices));
            comboBox.setMaxWidth(500);
            comboBox.setPrefWidth(500);
            comboBox.setValue(PacketScanningView.device != null ? PacketScanningView.device : devices.get(0));
            comboBox.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(PcapNetworkInterface item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(formatDeviceName(item, empty));
                }
            });
            comboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(PcapNetworkInterface item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(formatDeviceName(item, empty));
                }
            });

            dialog.getDialogPane().setContent(comboBox);
            dialog.setResultConverter(dialogButton -> dialogButton == confirmButton ? comboBox.getValue() : null);
            return dialog.showAndWait().orElse(null);
        } catch (Exception e) {
            showAlert("Error loading network devices: " + e.getMessage());
            return null;
        }
    }

    private static String formatDeviceName(PcapNetworkInterface item, boolean empty) {
        if (empty || item == null) {
            return null;
        }
        return item.getDescription() != null ? item.getDescription() : item.getName();
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Port Scanner");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
