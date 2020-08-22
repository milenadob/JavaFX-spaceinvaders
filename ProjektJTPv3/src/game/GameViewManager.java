package game;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

/**
 *  Managing every game element. Starting and creating game.
 * @author Milena Dobkowska
 * @version 2.0
 */
class GameViewManager {

    //create window
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage menuStage;
    private Stage gameStage;
    private AnimationTimer gameTimer;

    //helping variables
    /**helper variable to create delay for alien bomb shoot*/
    private double t;
    /**helper variable for creating delay for moving aliens and boss*/
    private double speed;
    /**number of level in a game*/
    private int level;
    /**true if there is collision between a bullet and an alien*/
    private volatile boolean bulletsAlienCollision;
    /**true if level end*/
    private boolean exit;

    //gameElements
    private Ship ship;
    private AlienManager alienManager;
    private InfoLabel infoLabel;
    private Boss boss;
    private Star star;
    private EndGameSubScene endGameSubScene;
    private NextLevelSubScene nextLevelSubScene;

    //semaphore
    Semaphore semaphore = new Semaphore(1);

    /**
     * Constructor. Set game scene, scene layout, attach a css file to scene.
     */
    GameViewManager() {
        gamePane=new AnchorPane();
        gameScene=new Scene(gamePane, Screen.getPrimary().getVisualBounds().getWidth()-100,Screen.getPrimary().getVisualBounds().getHeight()-40);
        gameStage=new Stage();
        gameStage.setScene(gameScene);
        gameScene.getStylesheets().add("game.css");
    }

    /**
     * Creating new game. Hiding menu stage and showing game stage.
     * @param menuStage menu stage
     * @param choosenShip path to choosen ship image
     * @see #initializeElements()
     * @see #createGameLoop()
     */
    void createNewGame(Stage menuStage, String choosenShip){
        this.menuStage = menuStage;
        this.menuStage.hide();
        this.ship=new Ship(choosenShip,gamePane,gameScene);
        initializeElements();
        createGameLoop();
        gameStage.show();
    }

    /**
     * Initializing game elements.
     * Seting starting level to 1.
     */
    private void initializeElements(){
        this.level=1;
        this.bulletsAlienCollision=false;
        this.ship.createKeyListeners();
        this.alienManager=new AlienManager(gamePane);
        this.infoLabel=new InfoLabel(gamePane);
        this.boss=new Boss(gamePane,5);
        this.star=new Star();
        this.endGameSubScene=new EndGameSubScene(gameStage,menuStage);
        this.nextLevelSubScene=new NextLevelSubScene(this);
    }

    /**
     * Creating game loop. Starting timer after level initialization.
     * @see #nextLevel()
     * @see #update()
     */
    void createGameLoop(){
        nextLevel();
        gameTimer= new AnimationTimer() {
            @Override
            public void handle(long l) {

                update();
            }
        };
        gameTimer.start();
    }

    /**
     * Updating game inside game timer.
     * Moving elements. Checking for collision. Checking if end game condition is true.
     * @see Ship#moveShip()
     * @see #playerBulletsControl()
     * @see Star#setNewStarPosition()
     * @see #alienController()
     * @see #alienBulletsController()
     * @see AlienManager#moveAliens()
     * @see Boss#moveBoss()
     * @see Boss#healthBarupdate()
     * @see #playerBulletsBossCollide()
     * @see #bossBulletsControl()
     * @see #endGameCheck()
     */
    private void update(){
        t+=0.016;
        speed+=0.016;
        ship.moveShip();
        star.moveDown();
        playerBulletsControl();
        if(star.getImage().getLayoutY()>gameScene.getHeight())star.setNewStarPosition();

        if(level%3!=0){
            alienController();
            alienBulletsCollideCheck();
            alienBulletsController();
            if (t > 2) {
                t = 0;
            }
            if(speed>(2-0.05*level)){
                alienManager.moveAliens();
                speed=0;
            }
            if(alienManager.getAlienList().size()==0){
                gameTimer.stop();
                gamePane.getChildren().add(nextLevelSubScene);
            }

        }else{
            if(speed>(1-0.05*level)){
                boss.moveBoss();
                speed=0;
            }
            boss.healthBarupdate();
            bossBulletsCollideCheck();
            playerBulletsBossCollide();
            bossBulletsControl();
        }
        endGameCheck();
    }

    /**
     * clearing stage for another level. adding to scene needed game elements.
     * creating helper threads for points managing.
     * @see #starPlayerCollision()
     * @see #alienPlayerBulletCollision()
     * @see Star#setNewStarPosition()
     * @see InfoLabel#addtoPane()
     * @see Boss#setBossPosition()
     * @see Boss#setHP(int)
     * @see Boss#createHealthBar()
     */
    private void nextLevel(){
        exit=true;
        gamePane.getChildren().clear();
        for (int b = 0; b < ship.getBombs().size(); b++) {
            ship.getBombs().get(b).setDestroyed();
        }
        gamePane.getChildren().add(ship.getImage());
        star.setNewStarPosition();
        gamePane.getChildren().add(star.getImage());
        infoLabel.addtoPane();
        if (level % 3 != 0) {
            if(level>6)alienManager.createAliens(30);
            else alienManager.createAliens(20);
        }else {
            boss.setBossPosition();
            boss.setHP(400+100*level);
            gamePane.getChildren().add(boss.getImage());
            boss.createHealthBar();
        }
        exit=false;
        starPlayerCollision();
        alienPlayerBulletCollision();

    }

    /**
     * Getter
     * @return info label
     */
    InfoLabel getInfo() {
        return infoLabel;
    }

    /**
     * Incrementing level number
     */
    void setLevel() {
        this.level = this.level+ 1;
    }

    /**
     * Moving player bullets and removing them after collision or getting out of range
     * @see Bomb#moveBomb()
     */
    private void playerBulletsControl(){

        for (int b = 0; b < ship.getBombs().size(); b++) {
            if (ship.getBombs().get(b).isDestroyed() || ship.getBombs().get(b).getImage().getLayoutY() < 0) {
                gamePane.getChildren().remove(ship.getBombs().get(b).getImage());
                ship.getBombs().remove(b);
                continue;
            }
            ship.getBombs().get(b).moveBomb();
        }
    }

    /**
     * Creates new thread that checks if ship is colliding with star and adds points if collision is detected
     * javafx thread updates info label and sets new position for star
     * @see Star#setNewStarPosition()
     * @see InfoLabel#setPoints(int)
     * @see InfoLabel#updateLabel()
     */
    private void starPlayerCollision(){
        Runnable task=new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (star.checkIfElementsCollide(ship)) {
                        try {
                            semaphore.acquire();
                            infoLabel.setPoints(10);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            semaphore.release();
                        }


                        Platform.runLater(() -> {
                            infoLabel.updateLabel();
                            star.setNewStarPosition();
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (infoLabel.getPlayerLife() < 0 || exit )break;
                }
            }
        };
        new Thread(task).start();
    }

    /**
     * Creates new thread that adds points if there was a collision between ship bullet and alien
     * javafx thread updates label
     * @see InfoLabel#setPoints(int)
     * @see InfoLabel#updateLabel()
     */
    private void alienPlayerBulletCollision(){
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(bulletsAlienCollision){
                        try {
                            semaphore.acquire();
                            infoLabel.setPoints(5);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            semaphore.release();
                        }


                        Platform.runLater(() -> infoLabel.updateLabel());
                             bulletsAlienCollision=false;
                            }
                    if (infoLabel.getPlayerLife() < 0 || exit)break;
                        }
            }
        };
        new Thread(task2).start();
    }

    /**
     * Moving and rotating aliens.
     * Checking for collision between alien and player bullet
     * Removing bullets that collided
     * Removing aliens if they have no life points left
     * @see AlienManager#rotateAliens()
     * @see GameElement#checkIfElementsCollide(GameElement)
     * @see GameElement#setDestroyed()
     * @see Alien#takeHP()
     */
    private void alienController(){
        if (alienManager.getAlienList().size() > 0)
            alienManager.rotateAliens();

        for (int a = 0; a < alienManager.getAlienList().size(); a++) {
            for (Bomb b : ship.getBombs()) {
                boolean collide = alienManager.getAlienList().get(a).checkIfElementsCollide(b);
                if (collide) {
                    bulletsAlienCollision=true;
                    gamePane.getChildren().remove(b.getImage());
                    b.setDestroyed();
                    alienManager.getAlienList().get(a).takeHP();
                    if(alienManager.getAlienList().get(a).getHP()==0){
                        gamePane.getChildren().remove(alienManager.getAlienList().get(a).getImage());
                        alienManager.getAlienList().get(a).setDestroyed();
                    }
                }
            }
            if (alienManager.getAlienList().get(a).isDestroyed()) {
                if (alienManager.getAlienList().get(a).getBomb() != null)
                    gamePane.getChildren().remove(alienManager.getAlienList().get(a).getBomb().getImage());
                alienManager.getAlienList().remove(a);
            }
        }
    }

    /**
     * Creating alien bullets and moving them
     * Checking if bullets are out of range or if they collide with ship
     * Removing player life if there was collision
     * @see InfoLabel#removeLife()
     * @see Alien#setBomb()
     * @see GameElement#checkIfElementsCollide(GameElement)
     * @see Bomb#moveBomb()
     * @see Alien#shoot(AnchorPane)
     */
    private void alienBulletsController(){
        for (Alien a : alienManager.getAlienList()) {
            if (a.getBomb() == null) {
                if (t > 2)
                    a.shoot(gamePane);
            } else {
                a.getBomb().moveBomb();

                if (a.getBomb().getImage().getLayoutY() > gameScene.getHeight()) {
                    gamePane.getChildren().remove(a.getBomb().getImage());
                    a.setBomb();
                    continue;
                }
                if (a.getBomb().checkIfElementsCollide(ship)) {
                    gamePane.getChildren().remove(a.getBomb().getImage());
                    a.setBomb();
                    infoLabel.removeLife();
                }
            }
        }
    }

    /**
     * Checking if ship bullets collide with boss
     * Adding points and removing boss life points
     * Ending level when boss lost all his life points
     * @see GameElement#checkIfElementsCollide(GameElement)
     * @see InfoLabel#setPoints(int)
     * @see Boss#takeHP()
     * @see InfoLabel#updateLabel()
     * @see GameElement#setDestroyed()
     */
    private void playerBulletsBossCollide(){
        for (Bomb b : ship.getBombs()) {
            boolean collide = boss.checkIfElementsCollide(b);
            if (collide) {
                infoLabel.setPoints(5);
                boss.takeHP();
                infoLabel.updateLabel();
                gamePane.getChildren().remove(b.getImage());
                b.setDestroyed();
                if (boss.getHP() <= 0) {
                    gamePane.getChildren().remove(b.getImage());
                    gamePane.getChildren().remove(boss.getImage());
                    gameTimer.stop();
                    gamePane.getChildren().add(nextLevelSubScene);
                }
            }
        }
    }

    /**
     * Creating boss bullets and moving them
     * Checking if boss bullets collide with ship
     * @see Bomb#moveBomb()
     * @see Boss#shoot()
     * @see GameElement#checkIfElementsCollide(GameElement)
     * @see InfoLabel#removeLife()
     */
    private void bossBulletsControl(){
        if (t > 0.5){
            boss.shoot();
            t = 0;
        }
        for (int b = 0; b < boss.getBombs().size(); b++) {
            if (boss.getBombs().get(b).checkIfElementsCollide(ship) || boss.getBombs().get(b).getImage().getLayoutY() > gameScene.getHeight()) {
                gamePane.getChildren().remove(boss.getBombs().get(b).getImage());
                if(boss.getBombs().get(b).checkIfElementsCollide(ship))infoLabel.removeLife();
                boss.getBombs().remove(b);
                continue;
            }
            boss.getBombs().get(b).moveBomb();
        }
    }

    /**
     * Ending game if player lost all his lifes.
     * Stopping game timer, showing end game sub scene
     * @see InfoLabel#getPlayerLife()
     * @see InfoLabel#setPoints(int)
     * @see EndGameSubScene#setPoints(int)
     * @see EndGameSubScene#initializeEndScene()
     */
    private void endGameCheck() {
        if (infoLabel.getPlayerLife() < 0) {
            gameTimer.stop();
            endGameSubScene.setPoints(infoLabel.getPoints());
            endGameSubScene.initializeEndScene();
            gamePane.getChildren().add(endGameSubScene);
        }
    }
    private void alienBulletsCollideCheck(){
        for (Alien a : alienManager.getAlienList()) {
            for(int c=0;c<ship.getBombs().size();c++) {
                if(a.getBomb()!=null)
                if (a.getBomb().checkIfElementsCollide(ship.getBombs().get(c))) {
                    gamePane.getChildren().remove(a.getBomb().getImage());
                    a.setBomb();
                    gamePane.getChildren().remove(ship.getBombs().get(c).getImage());
                    ship.getBombs().remove(c);
                }
            }
        }
    }

    private void bossBulletsCollideCheck(){
        for (int b = 0; b < boss.getBombs().size(); b++) {
            for(int c=0;c<ship.getBombs().size();c++){
                if(boss.getBombs().get(b).checkIfElementsCollide(ship.getBombs().get(c))){
                    gamePane.getChildren().remove(boss.getBombs().get(b).getImage());
                    boss.getBombs().remove(b);
                    gamePane.getChildren().remove(ship.getBombs().get(c).getImage());
                    ship.getBombs().remove(c);
                    break;
                }
            }
        }
    }

}
