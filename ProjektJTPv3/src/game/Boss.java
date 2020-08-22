package game;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Boss enemy in game.
 * @author Milena Dobkowska
 * @version 1.0
 * @see GameElement
 */
class Boss extends GameElement{

    /**list of boss bombs*/
    private List<Bomb> bombs = new ArrayList<>();
    private static final Random RAND = new Random();
    /**max amount of bombs boss can shoot*/
    private int maxBombs;
    /**current life points of boss*/
    private int HP;
    /**max life points boss have in this round*/
    private int maxHP;
    /**rectangle representing current life point of boss */
    private Rectangle healthBar;
    private AnchorPane gamePane;

    /**
     * Boss constructor. Game element of type "boss" and specified image.
     * Specifies amount of bombs and image placement at the upper center of scene
     * @param gamePane game scene layout
     * @param maxBombs max amount of bombs
     * @see #setBossPosition()
     */
    Boss(AnchorPane gamePane, int maxBombs) {
        super("boss",new ImageView("file:images/shipYellow_manned.png"),124,108);
        this.gamePane = gamePane;
        this.maxBombs=maxBombs;
        setBossPosition();
    }

    /**
     * Placing boss image at specified position
     */
    void setBossPosition(){
        this.getImage().setLayoutX(gamePane.getWidth()/2);
        this.getImage().setLayoutY(70);
    }

    /**
     * Creating boss bombs of different types and adding them to scene
     */
    void shoot(){
        if(Math.random()<0.3) {
            String bombImage="file:images/laserRed02.png";
            if(bombs.size()<=maxBombs) {
                int r = RAND.nextInt(3);
                Bomb bomb;
                switch (r){
                    case 1:
                        bomb = new Bomb(getImage().getLayoutX() + 62, getImage().getLayoutY() + 54, "bossbullet1",bombImage ,8,16);
                        break;
                    case 2:
                        bomb = new Bomb(getImage().getLayoutX() + 62, getImage().getLayoutY() + 54, "bossbullet2",bombImage,8,16);
                        break;
                    default:
                        bomb = new Bomb(getImage().getLayoutX() + 62, getImage().getLayoutY() + 54, "bossbullet3",bombImage,8,16);
                        break;
                }
                bombs.add(bomb);
                gamePane.getChildren().add(bomb.getImage());
            }
        }
    }

    /**
     * Moving boss image within range of the scene
     * @see GameElement#moveDown()
     * @see GameElement#moveLeft()
     * @see GameElement#moveRight()
     * @see GameElement#moveUp()
     */
    void moveBoss(){
        int r = RAND.nextInt(4);
        switch (r){
            case 1:
                if(this.getImage().getLayoutX()<1200)
                    this.moveLeft();
                break;
            case 2:
                if(this.getImage().getLayoutX()>50)
                    this.moveRight();
                break;
            case 3:
                if (this.getImage().getLayoutY()<500)
                    this.moveDown();
                break;
            case 4:
                if (this.getImage().getLayoutY()>50)
                    this.moveUp();
                break;
        }
    }

    /**
     * Create rectangle representing boss life points and add it to scene
     */
    void createHealthBar(){
        Label hp= new Label("BOSS HP:");
        hp.setStyle("-fx-text-fill:white");
        healthBar = new Rectangle(500,20, Color.DARKRED);
        gamePane.getChildren().add(healthBar);
        gamePane.getChildren().add(hp);
    }

    /**
     * updating healthBar width when boss life points are decreasing
     */
    void healthBarupdate(){
        if(healthBar !=null) {
            double pr=(double)HP/maxHP;
            healthBar.setWidth(500*pr);
        }
    }

    /**
     * Subtract 5 life points from boss
     */
    void takeHP(){
        this.HP-= 5;
    }
    /**
     *Getter
     * @return life points of boss
     */
    int getHP() {
        return HP;
    }

    /**
     * Sets starting and maximum amount of life points for boss
     * @param HP life points
     */
    void setHP(int HP) {
        this.HP = HP;
        this.maxHP=HP;
    }

    /**
     * Getter
     * @return list of boss bombs
     */
    List<Bomb> getBombs() {
        return bombs;
    }
}
