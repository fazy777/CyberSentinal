package JavaFXUI;

import Model.User;
import DAO.ReportDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ExportView {
    public static void Export(Stage stage, User user) throws SQLException, ClassNotFoundException {
        int total = ReportDAO.getTotalIncidents();
        int open = ReportDAO.getOpenCount();
        int critical = ReportDAO.getCriticalCount();
        int resolved = ReportDAO.getResolvedCount();
        Label header = new Label("Incident Statistics Report");
        Button backBtn = new Button("Back to Dashboard");
        Button exportButton = new Button("Export");
        AppTheme.primary(exportButton);
        HBox root0 = new HBox(20);
        HBox root = new HBox(20);
        HBox root1 = new HBox(20);
        root.getChildren().addAll(
                createStatCard("Total", total, "black"),
                createStatCard("Open", open, "#e67e22"),
                createStatCard("Critical", critical, "#c0392b"),
                createStatCard("Resolved", resolved, "#27ae60")
        );
        root0.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        root1.setAlignment(Pos.CENTER);
        AppTheme.actionBar(root1);
        root1.getChildren().addAll(backBtn,exportButton);
        root0.getChildren().addAll(header);

        VBox vbox = new VBox(20);


        vbox.getChildren().addAll(root0, root, root1);
        vbox.setAlignment(Pos.CENTER);
        AppTheme.page(vbox, header);
        backBtn.setOnAction(event -> {
            DashboardView.showDashboard(stage,user);
        });
        exportButton.setOnAction(event -> {
            try {
                boolean success = ReportDAO.file_generation();
                if(success){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Successful");
                    alert.setHeaderText("Export Successful");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText("Export Failed");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        Scene scene = new Scene(vbox,900,600);
        AppTheme.apply(scene);
        stage.setTitle("Report");
        stage.setScene(scene);
        stage.show();


    }
    private static VBox createStatCard(String title, int value, String color) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("subtle-text");

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.getStyleClass().add("metric-value");
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        VBox card = new VBox(5, titleLabel, valueLabel);
        card.setPadding(new Insets(15));
        card.getStyleClass().addAll("surface-card", "metric-card");
        card.setPrefWidth(150);
        card.setAlignment(Pos.CENTER);

        return card;
    }
}
