package quickBops;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class StartupUIController {

    //Controller stage
    private final Stage startupStage;

    //path to send to main controller
    @FXML
    private TextField textToMain;

    //button opens main scene
    @FXML
    private Button btnOpenMain;

    //error label
    @FXML
    private Label errorLabel;

    //open finder button
    @FXML
    private Button openFinder;


    public StartupUIController() {

        //create new stage
        startupStage = new Stage();

        //load FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StartupUI.fxml"));
            loader.setController(this);
            startupStage.setScene(new Scene(loader.load()));
            startupStage.setTitle("Welcome to QuickBops");
            openFinder.requestFocus();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showStage() {
        startupStage.show();
    }

    @FXML
    private void initialize() {
        //add listener to button to go to main stage
        try {
            btnOpenMain.setOnAction(event -> openMainLayout());
        }
        catch (NullPointerException e){
            errorLabel.setText("Please select a folder");
        }
    }

    @FXML
    private void openFinder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("QuickBops - Select a Folder Containing MP3s");
        File selectedDirectory = chooser.showDialog(startupStage);
        if(selectedDirectory.isDirectory()){
            textToMain.setText(selectedDirectory.toString() + "/");
        }
        errorLabel.setText("");
    }

    private void openMainLayout() {
        if(textToMain.getText().equals("")){
            errorLabel.setText("Please choose a folder");
        }
        else {
            MainUIController mainController = new MainUIController(this);
            mainController.showStage();
        }
    }

    public String getEnteredDirectory() {
        return textToMain.getText();
    }
}
