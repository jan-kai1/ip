package gui;

import chatterbox.ChatterboxGui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private ChatterboxGui chatter;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user_image.png"));
    private Image chatterImage = new Image(this.getClass().getResourceAsStream("/images/Chatterbox_image.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setChatterbox(ChatterboxGui c) {

        chatter = c;
        if (c.hasTasks()) {
            dialogContainer.getChildren()
                    .add(DialogBox.getChatterboxDialog("History found!", chatterImage));
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = chatter.processInput(input);
        if (response == null) {
            Stage stage = (Stage) dialogContainer.getScene().getWindow();
            stage.close();
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getChatterboxDialog(response, chatterImage)
        );
        userInput.clear();
    }


}
