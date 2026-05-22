package JavaFXUI;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.nio.file.Files;
import java.nio.file.Path;

public final class AppTheme {
    private AppTheme() {
    }

    public static void apply(Scene scene) {
        String css = findStylesheet();
        if (css != null && !scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }
        wrapPage(scene);
    }

    public static void page(VBox root, Label title) {
        root.getStyleClass().add("page");
        root.setPadding(new Insets(28));
        if (title != null) {
            title.getStyleClass().add("screen-title");
        }
    }

    private static void wrapPage(Scene scene) {
        Parent currentRoot = scene.getRoot();
        if (!(currentRoot instanceof Region content)
                || currentRoot instanceof StackPane
                || !content.getStyleClass().contains("page")
                || Boolean.TRUE.equals(content.getProperties().get("themeWrapped"))) {
            return;
        }

        content.getProperties().put("themeWrapped", true);
        content.getStyleClass().add("stage-glass");
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane shell = new StackPane(content);
        shell.getStyleClass().add("stage-background");
        shell.setPadding(new Insets(34));
        scene.setRoot(shell);
    }

    public static void card(Region region) {
        region.getStyleClass().add("surface-card");
    }

    public static void actionBar(Region region) {
        region.getStyleClass().add("action-bar");
    }

    public static void table(TableView<?> table) {
        table.getStyleClass().add("data-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    public static void dialog(DialogPane pane) {
        String css = findStylesheet();
        if (css != null && !pane.getStylesheets().contains(css)) {
            pane.getStylesheets().add(css);
        }
        pane.getStyleClass().add("themed-dialog");
    }

    public static void primary(Button button) {
        button.getStyleClass().add("primary-button");
    }

    public static void secondary(Button button) {
        button.getStyleClass().add("secondary-button");
    }

    public static void danger(Button button) {
        button.getStyleClass().add("danger-button");
    }

    public static void subtle(Node node) {
        node.getStyleClass().add("subtle-text");
    }

    private static String findStylesheet() {
        var resource = AppTheme.class.getResource("app.css");
        if (resource != null) {
            return resource.toExternalForm();
        }

        Path sourceCss = Path.of("src", "JavaFXUI", "app.css");
        if (Files.exists(sourceCss)) {
            return sourceCss.toUri().toString();
        }
        return null;
    }
}
