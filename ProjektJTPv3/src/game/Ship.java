package game;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Player ship game element
 * @author Milena Dobkowska
 * @version 1.0
 * @see GameElement
 */
class Ship extends GameElement{

    /**true if player pressed left arrow on keyboard*/
    private boolean isLeftKeyPressed;
    /**true if player pressed right arrow on keyboard*/
    private boolean isRightKeyPressed;
    /**list of ship bombs*/
    private List<Bomb> bombs = new ArrayList<>();
    private AnchorPane gamePane;
    private Scene gameScene;

    /**
     * Constructor. Creates game element of type "ship" with specified image and size.
     * @param choosenShip path to image of ship
     * @param gamePane game scene layout
     * @param gameScene game scene
     */
    Ship(String choosenShip, AnchorPane gamePane, Scene gameScene) {
        super("ship", new ImageView(choosenShip),110 , 50);
        this.gamePane=gamePane;
        this.gameScene=gameScene;
        this.getImage().setLayoutX(gamePane.getWidth()/2);
        this.getImage().setLayoutY(gamePane.getHeight()-70);
    }

    /**
     * Moving ship after key press
     * @see GameElement#moveLeft()
     * @see GameElement#moveRight()
     */
    void moveShip(){
        if (isLeftKeyPressed && !isRightKeyPressed){
            if (this.getImage().getLayoutX()>-20){
                moveLeft();
            }
        }
        if (isRightKeyPressed && !isLeftKeyPressed)
            if(this.getImage().getLayoutX()<gameScene.getWidth()-100){
                moveRight();
            }
    }

    /**
     * key listeners sets isLeftKeyPressed and isRightKeyPressed variables after left and right arrow press/release
     * calls shoot method after space bar press
     * @see #shoot()
     */
    void createKeyListeners(){
        gameScene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.LEFT){
                isLeftKeyPressed=true;
            }else if(keyEvent.getCode() == KeyCode.RIGHT){
                isRightKeyPressed=true;
            }else if(keyEvent.getCode() == KeyCode.SPACE){
                shoot();
            }
        });

        gameScene.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.LEFT){
                isLeftKeyPressed=false;
            }else if(keyEvent.getCode() == KeyCode.RIGHT){
                isRightKeyPressed=false;
            }
        });
    }

    /**
     * create ship bomb and add to scene if there is less bombs on scene than maxBombs
     */
    private void shoot(){
        int maxBombs = 5;
        if(bombs.size()<= maxBombs) {
            String bombImage = "file:images/laserRed08.png";
            Bomb bomb = new Bomb(this.getImage().getLayoutX() + 49, this.getImage().getLayoutY() - 38,
                    "shipbullet", bombImage,16,16);
            bombs.add(bomb);
            gamePane.getChildren().add(bomb.getImage());
        }
    }

    /**
     * Getter
     * @return ship bombs list
     */
    List<Bomb> getBombs() {
        return bombs;
    }
}