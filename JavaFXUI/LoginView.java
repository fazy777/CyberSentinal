package JavaFXUI;

import Model.User;
import DAO.UserDAO;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginView {
    public static void Loginview(Stage stage) throws Exception {
        Label title = new Label("CyberSentinel");
        Label subtitle = new Label("Incident and network monitoring console");
        AppTheme.subtle(subtitle);
        Button login_button = new Button("Login");
        AppTheme.primary(login_button);
        Label status = new Label();
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password: ");
        login_button.setOnAction(event->{
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user;
            try {
                user = UserDAO.login(username,password);
            } catch (ClassNotFoundException | SQLException e) {
                status.setText("Login service error: " + e.getMessage());
                status.getStyleClass().setAll("error-text");
                return;
            }
            if(user!=null ){
                status.setText("Login Successful");
                status.getStyleClass().setAll("success-text");
                DashboardView.showDashboard(stage,user);
            }else{
                usernameField.clear();
                passwordField.clear();
                status.setText("Login Failed");
                status.getStyleClass().setAll("error-text");
            }
        });
        usernameField.setMaxWidth(320);
        passwordField.setMaxWidth(320);
        login_button.setMaxWidth(320);

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().addAll("surface-card", "auth-card");
        title.getStyleClass().add("screen-title");
        card.getChildren().addAll(
                title,
                subtitle,
                usernameField,
                passwordField,
                login_button,
                status
        );

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        AppTheme.page(root, null);
        root.getChildren().add(card);
        Scene scene = new Scene(root,560,420);
        AppTheme.apply(scene);
        stage.setTitle("CyberSentinel Login");
        stage.setScene(scene);
        stage.show();
    }
}
