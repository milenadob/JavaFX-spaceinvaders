package game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * An alien enemy in game. Extend GameElement.
 * @author Milena Dobkowska
 * @version 2.0
 * @see GameElement
 */
class Alien extends GameElement {
    /**each alien enemy has one bomb*/
    private Bomb bomb;
    /**each alien enemy has specified life points*/
    private int HP;

    /**
     * Alien constructor. Creates game element of type "alien" with specified image and size.
     * Each enemy has 15 life points
     * @param image image of alien shown in window
     * @param imageWidth width of image
     * @param imageHeight height of image
     */
    Alien(String image, int imageWidth, int imageHeight) {
        super("alien", new ImageView(image), imageWidth, imageHeight);
        this.HP=15;
    }

    /**
     * Subtract 5 life points from alien
     */
    void takeHP(){
        this.HP-= 5;
    }

    /**
     *Getter
     * @return life points of alien
     */
    int getHP() {
        return HP;
    }

    /**
     * Getter
     * @return alien bomb
     */
    Bomb getBomb() {
        return bomb;
    }

    /**
     * destroy/delete alien bomb
     */
    void setBomb() {
        this.bomb = null;
    }

    /**
     * random create alien bomb and add to game scene
     * @param gamePane game scene layout
     */
    void shoot(AnchorPane gamePane){
        if(Math.random()<0.3) {
            String bombImage = "file:images/laserRed02.png";
            bomb = new Bomb(getImage().getLayoutX() + 45, getImage().getLayoutY() + 45, "alienbullet", bombImage, 8, 16);
            gamePane.getChildren().add(bomb.getImage());
        }
    }
}
