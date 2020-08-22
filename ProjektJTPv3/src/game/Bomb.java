package game;

import javafx.scene.image.ImageView;

/**
 *  Bomb game element that can be shot by aliens, boss or player
 * @author Milena Dobkowska
 * @version 2.0
 * @see GameElement
 */
class Bomb extends GameElement{

    /**
     * Bomb constructor. Creates bomb at specified position.
     * @param x horizontal coordinate for placing bomb on scene
     * @param y vertical coordinate for placing bomb on scene
     * @param type type of bomb
     * @param image image of bomb
     * @param imageWidth width of image
     * @param imageHeight height of image
     */
    Bomb(double x, double y, String type, String image, int imageWidth,int imageHeight) {
        super(type, new ImageView(image), imageWidth, imageHeight);

        this.getImage().setLayoutX(x);
        this.getImage().setLayoutY(y);
        this.getImage().setFitWidth(imageWidth);
        this.getImage().setFitHeight(imageHeight);
    }

    /**
     * Moving bomb in specified direction based on its type
     * @see GameElement#moveDown()
     * @see GameElement#moveLeft()
     * @see GameElement#moveRight()
     * @see GameElement#moveUp()
     */
    void moveBomb() {
        switch (getType()) {
            case "shipbullet":
                this.moveUp();
                break;
            case "bossbullet1":
                this.moveDown();
                this.moveLeft();
                break;
            case "bossbullet2":
                this.moveDown();
                this.moveRight();
                break;
            default:
                this.moveDown();
                break;
        }
    }
}
