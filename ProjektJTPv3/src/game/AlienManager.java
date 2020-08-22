package game;

import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages aliens, creates new aliens and moves them
 * @author Milena Dobkowska
 * @version 1.0
 */
class AlienManager {

    private AnchorPane gamePane;
    private static final Random RAND = new Random();
    /**list of all created aliens*/
    private List<Alien> alienList = new ArrayList<>();
    /**different images for creating aliens*/
    private String[] aliens={
            "file:images/ufoBlue.png",
            "file:images/ufoGreen.png",
            "file:images/ufoRed.png",
            "file:images/ufoYellow.png",
    };
    //Move helper variables
    /**true if aliens can be moved down*/
    private boolean down;
    /**true if aliens can be moved left*/
    private boolean left;
    /**true if aliens can be moved right*/
    private boolean right;
    /**for rotating aliens*/
    private int angle;

    /**
     * Alien manager constructor.
     * Sets start moving direction to right
     * @param gamePane game scene layout
     */
    AlienManager(AnchorPane gamePane) {
        this.gamePane=gamePane;
        this.down=false;
        this.left=false;
        this.right=true;
        this.angle=0;
    }

    /**
     * Creates new alien with random image from aliens table
     * @return alien
     */
    private Alien newAlien(){
        return new Alien(aliens[RAND.nextInt(aliens.length)],90,90);
    }

    /**
     * Creates aliens images at specified positions and adds them to game scene
     * @param howMany how many aliens needs to be created
     * @see #newAlien()
     */
    void createAliens(int howMany){
        for(int i=0;i<howMany;i++){
            alienList.add(newAlien());
        }
        int i=0,j=1,rowCount=0;
        for(Alien a:alienList){
            if(rowCount==a.getImageWidth()*10){
                j++;
                rowCount=0;
                i=0;
            }
            a.getImage().setLayoutX(200 + a.getImageWidth() * i);
            a.getImage().setLayoutY(a.getImageHeight()*j);
            i++;
            rowCount+=a.getImageWidth();
            gamePane.getChildren().add(a.getImage());
        }
    }

    /**
     * Calculating moving direction for aliens and moving their images
     * @see GameElement#moveDown()
     * @see GameElement#moveLeft()
     * @see GameElement#moveRight()
     */
    void moveAliens(){

        int posXL=(int)alienList.get(0).getImage().getLayoutX();
        int posXR=(int)alienList.get(alienList.size()-1).getImage().getLayoutX()+91;

        if(posXR<=1200 &&!down && right){
            for(Alien a:alienList){
                a.moveRight();

            }
        }else if(posXL>=50&&!down && left){
            for(Alien a:alienList){
                a.moveLeft();
            }
        }else if(posXR>1200 || posXL<50){
            down=true;
        }
        if(down){
            for(Alien a:alienList){
                a.moveDown();

            }
            if(posXR>1200){right=false;left=true;}
            if(posXL<50){right=true;left=false;}
            down=false;
        }
    }

    /**
     * Rotating alien image
     */
    void rotateAliens(){
        if (angle>=360)angle=0;
        for(Alien a:alienList) {
            a.getImage().setRotate(angle);
            angle += 1;
        }
    }

    /**
     * Getter
     * @return list of aliens
     */
    List<Alien> getAlienList() {
        return alienList;
    }
}
