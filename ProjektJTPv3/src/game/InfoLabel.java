package game;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *  Label that shows player points and how much lifes are left
 * @author Milena Dobkowska
 * @version 1.0
 */
class InfoLabel {
    /**image representing life*/
    private final static String LIFE_IMAGE = "file:images/playerLife2_red.png";
    /**table storing imageviews of player lifes*/
    private ImageView[] playerLifes;
    /**amount of lifes that player have counting from 0*/
    private int playerLife;
    /**player points*/
    private int points;
    private Label pointsLabel;
    private AnchorPane gamePane;

    /**
     * Constructor. Creating info label and setting starting values of points to 0 and playerLife to 2.
     * @param gamePane game scene layout
     * @see #createInfoLabel()
     */
    InfoLabel(AnchorPane gamePane) {
        this.points=0;
        this.playerLife = 2;
        this.gamePane=gamePane;
        createInfoLabel();
    }

    /**
     * Creates parts of info label ,label with points and images of lifes
     */
    private void createInfoLabel(){
        //POINTS LABEL
        pointsLabel = new Label("POINTS : 00");
        pointsLabel.setLayoutX(gamePane.getWidth()-200);
        pointsLabel.setLayoutY(10);
        pointsLabel.setId("labelPoints");
        //LIFE IMAGE
        playerLifes = new ImageView[3];
        for (int i=0;i<playerLifes.length;i++){
            playerLifes[i] = new ImageView(LIFE_IMAGE);
            playerLifes[i].setLayoutX(gamePane.getWidth()-200 + (i*50));
            playerLifes[i].setLayoutY(51);
        }
    }

    /**
     * adding info label parts to game scene
     */
    void addtoPane(){
        gamePane.getChildren().add(pointsLabel);
        for (int i=0;i<=playerLife;i++){
            gamePane.getChildren().add(playerLifes[i]);
        }
    }

    /**
     * Getter
     * @return amount of points that player got
     */
    int getPoints() {
        return points;
    }

    /**
     * Setter. Adding specified amount of points to total points.
     * @param points how much points to add
     */
    void setPoints(int points) {
        this.points = this.points+points;
    }

    /**
     * Removing life from player lifes
     */
    void removeLife(){
        if(playerLife>=0)
        gamePane.getChildren().remove(playerLifes[playerLife]);
        if(playerLife>=0)playerLife--;
    }

    /**
     * Update points label to show current points count
     */
    void updateLabel(){
        String textToString = "POINTS : ";
        if(points<10){
            textToString = textToString + "0";
        }
        pointsLabel.setText(textToString+points);
    }

    /**
     * Getter
     * @return how much lifes player have left counting from 0
     */
    int getPlayerLife() {
        return playerLife;
    }
}
