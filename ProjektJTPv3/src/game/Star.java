package game;

import javafx.scene.image.ImageView;

import java.util.Random;

/**
 * Star game element
 * @author Milena Dobkowska
 * @version 1.0
 * @see GameElement
 */
class Star extends GameElement {

    private static final Random RAND = new Random();

    /**
     * Constructor. Create game element of type "star" with specified image and size
     */
    Star() {
        super("star", new ImageView("file:images/star_gold.png"), 30, 30);
    }

    /**
     * Specify position for star. Random x coordinate.
     */
    void setNewStarPosition(){
        getImage().setLayoutX(RAND.nextInt(1200));
        getImage().setLayoutY(-5);
    }
}
