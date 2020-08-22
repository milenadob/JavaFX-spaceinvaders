package game;

import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *  Sub scene shown after losing the game
 * @author Milena Dobkowska
 * @version 2.0
 */
class EndGameSubScene extends SubScene {

    private AnchorPane endPane;
    private Stage menuStage;
    private Stage gameStage;
    /**text area for entering player name*/
    private TextArea nickName;
    /**amount of points got in game*/
    private int points;

    /**
     * Constructor. Creates sub scene with anchor pane layout and size 400x400
     * @param gameStage game stage
     * @param menuStage menu stage
     */
    EndGameSubScene(Stage gameStage, Stage menuStage) {
        super(new AnchorPane(), 400, 400);
        this.gameStage=gameStage;
        this.menuStage=menuStage;
    }

    /**
     * Setter
     * @param points amount of points that player got in game
     */
    void setPoints(int points) {
        this.points = points;
    }

    /**
     * Initializing parts of sub scene and sub scene layout.
     * @see #createEndGameLabel()
     * @see #createEndScoreLabel()
     * @see #createEnterName()
     * @see #createMenuButton()
     * @see #createSaveButton()
     */
    void initializeEndScene(){
        setLayoutY(30);
        setLayoutX(400);
        endPane=(AnchorPane) this.getRoot();
        endPane.getStyleClass().add("sub1");
        createEndGameLabel();
        //createMenuButton();
        createEndScoreLabel();
        createEnterName();
        createSaveButton();
    }

    /**
     * Creates label with text "END GAME"
     */
    private void createEndGameLabel(){
        Label message = new Label();
        message.setLayoutX(135);
        message.setLayoutY(50);
        message.setAlignment(Pos.CENTER);
        message.setText("END GAME");
        message.setId("labelEnd");
        endPane.getChildren().add(message);
    }

    /**
     * Creates button that shows menu stage after click and closes game stage
     */
    private void createMenuButton(){
        Button menuButton = new Button("MAIN MENU");
        menuButton.setLayoutX(105);
        menuButton.setLayoutY(300);
        menuButton.setOnAction(event -> {
            menuStage.show();
            gameStage.close();

        });
        endPane.getChildren().add(menuButton);
    }

    /**
     * Creates label that shows how much points got player
     */
    private void createEndScoreLabel(){
        Label endScore = new Label("End Score: "+points);
        endScore.setLayoutX(100);
        endScore.setLayoutY(110);
        endPane.getChildren().add(endScore);
    }

    /**
     * Creates text area where player can enter his name
     */
    private void createEnterName(){
        Label lab1 = new Label("Enter your nickname: ");
        lab1.setLayoutY(150);
        lab1.setLayoutX(100);
        endPane.getChildren().add(lab1);
        nickName = new TextArea();
        nickName.setLayoutX(80);
        nickName.setLayoutY(190);
        nickName.setPrefWidth(240);
        nickName.setPrefHeight(30);
        endPane.getChildren().add(nickName);
    }

    /**
     * Creates button that after click saves player name from text area to file
     * @see #storeNickAndPoints()
     */
    private void createSaveButton(){
        Button save = new Button("SAVE");
        save.setLayoutX(105);
        save.setLayoutY(240);
        save.setOnAction(event -> {
            try {
                storeNickAndPoints();
                //save.setDisable(true);
                menuStage.show();
                gameStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endPane.getChildren().add(save);
    }

    /**
     * Saves player nickname to "PlayerList.txt" file
     * @throws IOException when file not found
     */
    private void storeNickAndPoints() throws IOException {
        if (!nickName.getText().isEmpty()) {
            String filename = "PlayerList.txt";
            File file = new File(filename);
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                bw.write(String.format("%s\t%s", nickName.getText(), points));
                bw.newLine();
            } finally {
                if (bw != null) {
                    bw.close();
                    fw.close();
                }
            }
        }

    }

}
