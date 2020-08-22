package game;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 * Abstract class representing all movable game elements
 * @author Milena Dobkowska
 * @version 3.0
 */
abstract class GameElement {
    /**type of element*/
    private String type;
    private ImageView image;
    private int imageWidth;
    private int imageHeight;
    /**true if element need to be removed */
    private boolean destroyed;

    /**
     * Constructor. When element is created we set destroyed to false.
     * @param type type of element
     * @param image element image
     * @param imageWidth width of image
     * @param imageHeight height of image
     */
    GameElement(String type, ImageView image, int imageWidth, int imageHeight) {
        this.type = type;
        this.image = image;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.destroyed=false;
    }

    /**
     * Getter
     * @return element image
     */
    ImageView getImage() {
        return image;
    }

    /**
     * Getter
     * @return is element destroyed?
     */
    boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Changing state of element to destroyed
     */
    void setDestroyed() {
        this.destroyed = true;
    }

    /**
     * Getter
     * @return type of element
     */
    String getType() {
        return type;
    }

    /**
     * Getter
     * @return width of image
     */
    int getImageWidth() {
        return imageWidth;
    }

    /**
     * Getter
     * @return height of image
     */
    int getImageHeight() {
        return imageHeight;
    }

    /**
     * Moving game elements left by amount of pixels based on their types
     */
    void moveLeft(){
        if(type.equals("alien") || type.equals("boss")){
            this.image.setLayoutX(image.getLayoutX() - 7);
        }else{
            this.image.setLayoutX(image.getLayoutX() - 3);
        }
    }
    /**
     * Moving game elements right by amount of pixels based on their types
     */
    void moveRight(){
        if(type.equals("alien") || type.equals("boss")){
            this.image.setLayoutX(image.getLayoutX() + 7);
        }else{
            this.image.setLayoutX(image.getLayoutX() + 3);
        }
    }
    /**
     * Moving game elements up by amount of pixels based on their types
     */
    void moveUp(){
        if(type.equals("alien") || type.equals("boss")){
            this.image.setLayoutY(image.getLayoutY() - 7);
        }else{
            this.image.setLayoutY(image.getLayoutY() - 3);
        }
    }
    /**
     * Moving game elements down by amount of pixels based on their types
     */
    void moveDown(){
        if(type.equals("alien") || type.equals("boss")){
            this.image.setLayoutY(image.getLayoutY() + 7);
        }else{
            this.image.setLayoutY(image.getLayoutY() + 3);
        }
    }

    /**
     * creates hit box
     * @return rectangle representing hit box
     */
    private Rectangle getBounds(){
        return new Rectangle(this.image.getLayoutX(),this.image.getLayoutY(),imageWidth,imageHeight);
    }

    /**
     * Check if two elements collide with each other
     * @param second element that can collide with this element
     * @return true if element collide, false if not
     */
    boolean checkIfElementsCollide(GameElement second){
        return second.getBounds().getBoundsInParent().intersects(this.getBounds().getBoundsInParent());
    }
}
