package JavaFXUI;

import DAO.TrafficDAO;
import Model.PacketData;
import Model.User;
import Npcap.PacketsFinder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.util.Duration;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNetworkInterface;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketScanningView {
    static List<PcapNetworkInterface> devices;
    public static PcapNetworkInterface device;
    public static PcapNetworkInterface showDeviceSelectionDialog(List<PcapNetworkInterface> devices,Stage stage,User user) throws Exception {
        if (devices == null || devices.isEmpty()) {
            showAlert("No network devices found");
            return null;
        }

        Dialog<PcapNetworkInterface> dialog = new Dialog<>();
        dialog.setTitle("Select Network Device");
        dialog.setHeaderText("Please select a network interface for packet scanning");
        ButtonType confirmButton = new ButtonType("Start Scan", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);
        AppTheme.dialog(dialog.getDialogPane());
        ComboBox<PcapNetworkInterface> comboBox = new ComboBox<>(FXCollections.observableArrayList(devices));
        comboBox.setMaxWidth(500);
        comboBox.setPrefWidth(500);
        comboBox.setValue(device != null ? device : devices.get(0));
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
        PcapNetworkInterface selectedDevice = dialog.showAndWait().orElse(null);
        if (selectedDevice != null) {
            device = selectedDevice;
            PacketScanningView.PacketScanningViews(stage,user);
        }
        return selectedDevice;
    }
    public static void PacketScanningViews(Stage stage, User user) throws Exception {
        ArrayList<PacketData> packetdata = TrafficDAO.retrieve_packets();
        Label title_page = new Label("Packet Scanning");
        Label deviceLabel = new Label("Device: " + formatDeviceName(device, false));
        AppTheme.subtle(deviceLabel);
        Label title = new  Label("Live packet capture history");
        AppTheme.subtle(title);
        TableView<PacketData> tableView = new TableView<>();
        TableColumn<PacketData,String> traffic_idColumn = new TableColumn<>("Traffic ID");traffic_idColumn.setCellValueFactory(new PropertyValueFactory<>("traffic_id"));
        TableColumn<PacketData, String> timestampColumn = new TableColumn<>("Time Created");timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        TableColumn<PacketData, String> securityFlagColumn = new TableColumn<>("Security Flag");securityFlagColumn.setCellValueFactory(new PropertyValueFactory<>("securityFlag"));
        TableColumn<PacketData, String> protocolColumn = new TableColumn<>("Protocol");protocolColumn.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        TableColumn<PacketData, String> srcIpColumn = new TableColumn<>("Source IP");srcIpColumn.setCellValueFactory(new PropertyValueFactory<>("srcIp"));
        TableColumn<PacketData, String> srcMacColumn = new TableColumn<>("Source MAC");srcMacColumn.setCellValueFactory(new PropertyValueFactory<>("srcMac"));
        TableColumn<PacketData, String> dstIpColumn = new TableColumn<>("Destination IP");dstIpColumn.setCellValueFactory(new PropertyValueFactory<>("dstIp"));
        TableColumn<PacketData, String> dstmacColumn = new TableColumn<>("Destination MAC");dstmacColumn.setCellValueFactory(new PropertyValueFactory<>("Dstmac"));
        TableColumn<PacketData, String> dstServiceColumn = new TableColumn<>("Service Provider");dstServiceColumn.setCellValueFactory(new PropertyValueFactory<>("dstService"));
        TableColumn<PacketData, Timestamp> ttlColumn = new TableColumn<>("Time To Live");ttlColumn.setCellValueFactory(new PropertyValueFactory<>("ttl"));
        TableColumn<PacketData, String> srcPortColumn = new TableColumn<>("Source Port");srcPortColumn.setCellValueFactory(new PropertyValueFactory<>("srcPort"));
        TableColumn<PacketData, String> dstPortColumn = new TableColumn<>("Destination Port");dstPortColumn.setCellValueFactory(new PropertyValueFactory<>("dstPort"));
        TableColumn<PacketData, String> tcpFlagsColumn = new TableColumn<>("TCP Flag");tcpFlagsColumn.setCellValueFactory(new PropertyValueFactory<>("tcpFlags"));
        ObservableList<PacketData> data = FXCollections.observableArrayList(packetdata);
        tableView.getColumns().addAll(Arrays.asList(traffic_idColumn, protocolColumn, srcIpColumn, srcMacColumn,dstIpColumn,dstmacColumn,dstServiceColumn,ttlColumn,srcPortColumn, securityFlagColumn,dstPortColumn,tcpFlagsColumn,timestampColumn));
        tableView.setItems(data);
        AppTheme.table(tableView);
        Button scanningOn = new  Button("Scanning ON");
        Button scanningOff = new  Button("Scanning OFF");
        Button cleanCache = new  Button("Clean Cache");
        Button refresh = new  Button("Refresh");
        Button scanSourceIp = new Button("Scan Source IP");
        Button scanDestinationIp = new Button("Scan Destination IP");
        Button back = new Button("Back");
        AppTheme.primary(scanningOn);
        AppTheme.danger(scanningOff);
        AppTheme.secondary(scanSourceIp);
        AppTheme.secondary(scanDestinationIp);
        back.setOnAction(event -> {
            DashboardView.showDashboard(stage,user);
        });
        scanSourceIp.setOnAction(event -> openSelectedIpInPortScanner(tableView, stage, user, true));
        scanDestinationIp.setOnAction(event -> openSelectedIpInPortScanner(tableView, stage, user, false));
        tableView.setRowFactory(tv -> {
            TableRow<PacketData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    PortScannerView.showPortScanner(stage, user, row.getItem().getDstIp(), device);
                }
            });
            return row;
        });
        cleanCache.setOnAction(event -> {
            try {
                TrafficDAO.truncate();
                PacketScanningViews(stage,user);
            } catch (Exception e) {
                showAlert("Error clearing packet cache: " + e.getMessage());
            }
        });
        Timeline autoRefresh = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    try {
                        ArrayList<PacketData> latest = TrafficDAO.retrieve_packets();
                        data.setAll(latest);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                })
        );
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        refresh.setOnAction(event -> {
            if (autoRefresh.getStatus() == Timeline.Status.RUNNING) {
                autoRefresh.stop();
                refresh.setText("Start Auto Refresh");
            } else {
                autoRefresh.play();
                refresh.setText("Stop Auto Refresh");
            }
        });
        scanningOn.setOnAction(event -> {
            Thread scanThread = new Thread(() -> {
                try {
                    PacketsFinder.StartPacketScanning();
                } catch (Exception ex) {
                    Platform.runLater(() -> showAlert("Packet scanning could not start: " + ex.getMessage()));
                }
            });
            scanThread.setDaemon(true);
            scanThread.start();
        });
        scanningOff.setOnAction(event -> {
            Thread scanThread = new Thread(() -> {
                try {
                    PacketsFinder.stopPacketScanning();
                } catch (NotOpenException e) {
                    Platform.runLater(() -> showAlert("Packet scanning could not stop: " + e.getMessage()));
                }
            });
            scanThread.setDaemon(true);
            scanThread.start();
        });
        VBox root = new VBox(10);
        FlowPane hBox = new FlowPane(10, 10);
        hBox.setAlignment(Pos.CENTER);
        AppTheme.actionBar(hBox);
        root.setAlignment(Pos.TOP_CENTER);
        AppTheme.page(root, title_page);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        hBox.getChildren().addAll(scanningOn,scanningOff,cleanCache,refresh,scanSourceIp,scanDestinationIp,back);
        root.getChildren().addAll(title_page,deviceLabel,title,tableView,hBox);
        Scene scene = new Scene(root,1900,600);
        AppTheme.apply(scene);
        stage.setTitle("Packet Scanning");
        stage.setScene(scene);
        stage.show();

    }

    private static void openSelectedIpInPortScanner(TableView<PacketData> tableView, Stage stage, User user, boolean sourceIp) {
        PacketData selectedPacket = tableView.getSelectionModel().getSelectedItem();
        if (selectedPacket == null) {
            showAlert("Select a packet row first");
            return;
        }

        String targetIp = sourceIp ? selectedPacket.getSrcIp() : selectedPacket.getDstIp();
        if (targetIp == null || targetIp.isBlank() || targetIp.startsWith("224.") || targetIp.startsWith("239.")) {
            showAlert("Selected packet does not contain a scannable unicast IP");
            return;
        }

        PortScannerView.showPortScanner(stage, user, targetIp, device);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Packet Scanning");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private static String formatDeviceName(PcapNetworkInterface item, boolean empty) {
        if (empty || item == null) {
            return "No device selected";
        }
        return item.getDescription() != null ? item.getDescription() : item.getName();
    }
}
