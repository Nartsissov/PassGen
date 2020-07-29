package ru.nartsiss.passwordgenerator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.text.RandomStringGenerator;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%&*()_+-=[]|,./?><";

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        generateElements(root);

        Scene scene = new Scene(root, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        Image icon = new Image("icon.jpg");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setTitle("PassGen by Nartsiss");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateElements(Group parent) {
        CheckBox upper = new CheckBox("Use UPPER-case characters");
        CheckBox lower = new CheckBox("Use lower-case characters");
        CheckBox digits = new CheckBox("Use digits");
        CheckBox characters = new CheckBox("Use special symbols");
        upper.fire();
        lower.fire();
        digits.fire();
        characters.fire();

        Slider size = new Slider(0, 128, 16);
        size.setMinSize(400, 10);
        size.setShowTickLabels(true);
        size.setMajorTickUnit(32);
        size.setMinorTickCount(32);
        size.setSnapToTicks(true);

        Label count = new Label("Count:");
        count.setLayoutX(upper.getLayoutX() + 200);
        count.setLayoutY(upper.getLayoutY() + 95);
        Spinner<Integer> spinner = new Spinner<>(1, 100, 1, 1);
        spinner.setLayoutX(upper.getLayoutX() + 250);
        spinner.setLayoutY(upper.getLayoutY() + 85);

        Button button = new Button("Generate!");
        Label label = new Label();
        button.setOnAction(event -> {
            parent.getChildren().remove(label);
            if (!upper.isSelected() && !lower.isSelected() && !digits.isSelected() && !characters.isSelected()) {
                label.setText("You must select at least one condition to generate passwords.");
                label.setTextFill(Color.RED);
                parent.getChildren().add(label);
                return;
            }
            if (size.valueProperty().intValue() <= 0) {
                label.setText("The minimum required password length is 1 character.");
                label.setTextFill(Color.RED);
                parent.getChildren().add(label);
                return;
            }
            List<String> passwords = generatePasswords(spinner.getValue(), upper.isSelected(), lower.isSelected(), digits.isSelected(), characters.isSelected(), size.valueProperty().intValue());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(String.join("\n", passwords)), null);
            label.setText("Generated passwords are copied to the clipboard.");
            label.setTextFill(Color.GREEN);
            parent.getChildren().add(label);
        });

        Label sliderValue = new Label("Length: " + size.valueProperty().intValue());
        size.valueProperty().addListener((observable, oldValue, newValue) -> sliderValue.setText("Length: " + size.valueProperty().intValue()));

        upper.setLayoutX(5);
        upper.setLayoutY(5);
        lower.setLayoutX(5);
        lower.setLayoutY(35);
        digits.setLayoutX(5);
        digits.setLayoutY(65);
        characters.setLayoutX(5);
        characters.setLayoutY(95);
        size.setLayoutX(5);
        size.setLayoutY(135);
        button.setLayoutX(5);
        button.setLayoutY(175);
        sliderValue.setLayoutX(button.getLayoutX() + 70);
        sliderValue.setLayoutY(button.getLayoutY() + 4);
        label.setLayoutX(5);
        label.setLayoutY(200);

        parent.getChildren().addAll(upper, lower, digits, characters, size, button, sliderValue, count, spinner);
    }

    private List<String> generatePasswords(int count, boolean isUseUpper, boolean isUseLower, boolean isUseDigits, boolean isUseSymbols, int length) {
        List<String> chars = new ArrayList<>();
        if (isUseUpper) chars.add(UPPER);
        if (isUseLower) chars.add(LOWER);
        if (isUseDigits) chars.add(DIGITS);
        if (isUseSymbols) chars.add(SYMBOLS);

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .selectFrom(String.join("", chars).toCharArray())
                .build();

        List<String> passwords = new ArrayList<>();

        if (length <= 0) return passwords;

        for (int i = 0; i < count; i++) passwords.add(randomStringGenerator.generate(length));
        return passwords;
    }
}

