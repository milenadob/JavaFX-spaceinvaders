package game;

import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Sub scene shown after completing the level
 * @author Milena Dobkowska
 * @version 1.0
 */
class NextLevelSubScene extends SubScene {

    private AnchorPane nextLevelPane;
    private GameViewManager gv;

    /**
     * Constructor. Creating sub scene with anchor pane layout and size 400x400
     * @param gv game view manager
     * @see #initializeNextLevelSubScene()
     */
    NextLevelSubScene(GameViewManager gv) {
        super(new AnchorPane(),400,400);
        this.gv=gv;
        initializeNextLevelSubScene();
    }

    /**
     * Initializing sub scene layout and its elements
     * @see #createLabel()
     * @see #buttonNextLevel()
     */
    private void initializeNextLevelSubScene(){
        nextLevelPane = (AnchorPane) this.getRoot();
        setLayoutY(100);
        setLayoutX(400);
        nextLevelPane.getStyleClass().add("sub1");
        createLabel();
        buttonNextLevel();
    }

    /**
     * Creating label "CLEAR"
     */
    private void createLabel(){
        Label nextLevelLabel = new Label("CLEAR!");
        nextLevelLabel.setId("labelEnd");
        nextLevelLabel.setLayoutX(135);
        nextLevelLabel.setLayoutY(100);
        nextLevelPane.getChildren().add(nextLevelLabel);
    }

    /**
     * creating button that sets new level after click
     * @see GameViewManager#createGameLoop()
     * @see GameViewManager#setLevel()
     * @see GameViewManager#getInfo()
     */
    private void buttonNextLevel(){
        Button nextL = new Button("NEXT LEVEL");
        nextL.setLayoutX(105);
        nextL.setLayoutY(250);
        nextL.setOnAction(event -> {
            if(gv.getInfo().getPlayerLife()>0)gv.getInfo().setPoints((gv.getInfo().getPlayerLife()+1)*100);
            gv.getInfo().updateLabel();
            gv.setLevel();
            gv.createGameLoop();
        });
        nextLevelPane.getChildren().add(nextL);
    }


}
