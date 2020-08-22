package game;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main menu controller class. It contains event handlers and methods
 * for menu components added in menu.fxml file.
 * @author Milena Dobkowska
 * @version 1.0
 */
public class MenuController {
    /** true if options sub scene is hidden , false when options sub scene is shown*/
    private boolean isOptionsHidden=true;
    /** true if score sub scene is hidden , false when score sub scene is shown*/
    private boolean isScoreHidden=true;
    /** true if instructions sub scene is hidden , false when instructions sub scene is shown*/
    private boolean isInstructionsHidden=true;
    /** path of default image for player ship*/
    private String chosenShip= "file:images/playerShip2_blue.png";
    /** sub scene that is shown and we want to hide it after opening another sub scene*/
    private SubScene sceneToHide;
    /** map containing nicknames of players and their scores taken from PlayerList.txt file*/
    private Map<String, List<String>> playerScores;

    @FXML
    private Button startButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button scoreButton;
    @FXML
    private Button instrucitonsButton;
    @FXML
    private Button exitButton;
    @FXML
    private RadioButton blueRadio,greenRadio,orangeRadio,redRadio;
    @FXML
    private SubScene subScOptions;
    @FXML
    private SubScene subScInstructions;
    @FXML
    private SubScene subScScore;
    @FXML
    private ComboBox<String> playerList;
    @FXML
    private ListView<String> scoresList;

    /**
     * Initializing when application is started.
     * Add event listener to combo box which calls showScoresAfterPlayerSelected method
     * @see #showScoresAfterPlayerSelected()
     */
    public void initialize(){
        playerList.valueProperty().addListener((observableValue, s, t1) -> {
            if(t1!=null){
                showScoresAfterPlayerSelected();
            }
        });
    }


    /**
     * This method is part of the button click animation it moves button image down by 4 pixels on mouse press
     * @param event mouse press
     */
    @FXML
    public void onPress(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getSource().equals(startButton)){
                startButton.setLayoutY(startButton.getLayoutY()+4);
            }
            if(event.getSource().equals(optionsButton)){
                optionsButton.setLayoutY(optionsButton.getLayoutY()+4);
            }
            if(event.getSource().equals(scoreButton)){
                scoreButton.setLayoutY(scoreButton.getLayoutY()+4);
            }
            if(event.getSource().equals(instrucitonsButton)){
                instrucitonsButton.setLayoutY(instrucitonsButton.getLayoutY()+4);
            }
            if(event.getSource().equals(exitButton)){
                exitButton.setLayoutY(exitButton.getLayoutY()+4);
            }
        }
    }
    /**
     * This method is part of the button click animation it moves button image up by 4 pixels on mouse release
     * @param event mouse release
     */
    @FXML
    public void onRelease(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getSource().equals(startButton)){
                startButton.setLayoutY(startButton.getLayoutY()-4);
            }
            if(event.getSource().equals(optionsButton)){
                optionsButton.setLayoutY(optionsButton.getLayoutY()-4);
            }
            if(event.getSource().equals(scoreButton)){
                scoreButton.setLayoutY(scoreButton.getLayoutY()-4);
            }
            if(event.getSource().equals(instrucitonsButton)){
                instrucitonsButton.setLayoutY(instrucitonsButton.getLayoutY()-4);
            }
            if(event.getSource().equals(exitButton)){
                exitButton.setLayoutY(exitButton.getLayoutY()-4);
            }
        }
    }

    /**
     * Sliding panel animation after clicking option button
     * Hiding other sub scenes
     * @see #hideSubScene()
     */
    @FXML
    public void onOptionsButtonClick() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(subScOptions);
        if(isOptionsHidden) {
            hideSubScene();
            transition.setToX(-2100);
            isOptionsHidden=false;
            sceneToHide=subScOptions;
        }else {
            transition.setToX(0);
            isOptionsHidden=true;
        }
        transition.play();
    }

    /**
     * Sliding panel animation after clicking instruction button
     * Hiding other sub scenes
     * @see #hideSubScene()
     */
    @FXML
    public void onInstructionsButtonClick() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(subScInstructions);
        if(isInstructionsHidden) {
            hideSubScene();
            transition.setToX(-2100);
            isInstructionsHidden=false;
            sceneToHide=subScInstructions;
        }else {
            transition.setToX(0);
            isInstructionsHidden=true;
        }
        transition.play();
    }
    /**
     * Sliding panel animation after clicking score button
     * Hiding other sub scenes
     * addPlayerToComboBox method call
     * @see #addPlayerToComboBox()
     * @see #hideSubScene()
     */
    @FXML
    public void onScoreButtonClick(){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(subScScore);
        if(isScoreHidden) {
            hideSubScene();
            transition.setToX(-2100);
            isScoreHidden=false;
            sceneToHide=subScScore;
        }else {
            transition.setToX(0);
            isScoreHidden=true;
        }
        transition.play();

        try {
            addPlayerToComboBox();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Hide active sub scene with sliding animation
     */
    private void hideSubScene(){
        if(sceneToHide != null){
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(0.3));
            transition.setNode(sceneToHide);
            transition.setToX(0);
            isInstructionsHidden=true;
            isOptionsHidden=true;
            isScoreHidden=true;
            transition.play();
        }
    }

    /**
     * Close application after exit button click
     * @param event button click
     */
    @FXML
    public void onExitButtonClick(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * Set image of choosen ship picked in option sub scene window
     */
    private void shipRadioChoose(){
        if(blueRadio.isSelected())chosenShip= "file:images/playerShip2_blue.png";
        else if(greenRadio.isSelected())chosenShip= "file:images/playerShip2_green.png";
        else if(orangeRadio.isSelected())chosenShip= "file:images/playerShip2_orange.png";
        else if(redRadio.isSelected())chosenShip= "file:images/playerShip2_red.png";
    }

    /**
     * Create game stage and hide menu stage after "start" button click
     * @param event button click
     */
    @FXML
    public void onStartButtonClick(ActionEvent event){
        Stage mainStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(chosenShip != null){
            shipRadioChoose();
            GameViewManager gameManager = new GameViewManager();
            gameManager.createNewGame(mainStage,chosenShip);
        }
    }

    /**
     * Takes players nicknames and scores from PlayerList.txt file and puts it in playerScores map
     * Add nickname to combo box excluding duplicates
     * @throws IOException when file not found
     */
    private void addPlayerToComboBox() throws IOException {
        playerList.getItems().clear();
        scoresList.getItems().clear();
        playerScores = new HashMap<>();
        String input;
        String filename = "PlayerList.txt";
        File file = new File(filename);
        if(!file.exists())file.createNewFile();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        try{
            while ((input=br.readLine())!=null &&!input.equals("")){
                String[] pieces = input.split("\t");
                if(!playerScores.containsKey(pieces[0])){
                    playerScores.put(pieces[0], new ArrayList<>());
                    playerList.getItems().add(pieces[0]);
                }
                playerScores.get(pieces[0]).add(pieces[1]);
            }

        }finally {
            if(br!=null)br.close();
        }
    }

    /**
     * After selecting a player nickname in combo box show his scores in list view
     */
    private void showScoresAfterPlayerSelected(){
        if(playerList.getValue() != null){
            scoresList.getItems().clear();
            for(String s: playerScores.get(playerList.getValue())){
                scoresList.getItems().add(s);
            }
        }
    }
}
