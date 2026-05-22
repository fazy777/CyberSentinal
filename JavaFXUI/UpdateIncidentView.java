package JavaFXUI;

import Model.User;
import DAO.IncidentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateIncidentView {
    public static void viewIncident(Stage stage, User user) throws Exception{
        Label title = new Label("Incident Update");
        Label subtitle = new Label("Update an incident lifecycle status by incident ID.");
        AppTheme.subtle(subtitle);
        ObservableList<String> options = FXCollections.observableArrayList("Open", "In Progress", "Resolved", "Closed");
        ComboBox<String> status_comboBox = new ComboBox<>(options);status_comboBox.setPromptText("Select Status");
        TextField id = new TextField();id.setPromptText("IncidentID");
        id.setMaxWidth(400);
        Button Update = new Button("Update");
        Button back = new Button("Back");
        AppTheme.primary(Update);
        back.setOnAction(event -> {
           DashboardView.showDashboard(stage,user);
        });
        Update.setOnAction(event -> {
            String status = status_comboBox.getValue();
            if (status == null || id.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Enter an incident ID and select a status.");
                return;
            }
            int id_temp;
            try {
                id_temp = Integer.parseInt(id.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid ID", "Incident ID must be a whole number.");
                return;
            }
            boolean success;
            try {
                success = IncidentDAO.updateIncident(id_temp, status);
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", e.getMessage());
                return;
            }
            if(success){
                showAlert(Alert.AlertType.INFORMATION, "Updated Successfully", "Incident Updated Successfully");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Match", "No incident was found for that ID.");
            }
        });

        VBox root = new VBox(10);
        VBox card = new VBox(14);
        HBox hBox = new HBox(10);
        root.setAlignment(Pos.CENTER);
        AppTheme.page(root, title);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(460);
        card.getStyleClass().add("surface-card");
        card.setStyle("-fx-padding: 24;");
        hBox.setAlignment(Pos.CENTER);
        AppTheme.actionBar(hBox);
        hBox.getChildren().addAll(Update, back);
        card.getChildren().addAll(subtitle,id,status_comboBox,hBox);
        root.getChildren().addAll(title,card);
        Scene scene = new Scene(root,900,600);
        AppTheme.apply(scene);
        stage.setTitle("Incident Update");
        stage.setScene(scene);
        stage.show();

    }

    private static void showAlert(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Incident Update");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
