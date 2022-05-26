package com.spameggsfoobar.pongfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class PongFXApplication extends Application {
    
    // screen dimensions
    private static final int width = 800;
    private static final int height = 600;
    // player (paddle) dimensions
    private static final int playerHeight = 100;
    private static final int playerWidth = 15;
    // ball properties
    private static final double ballRadius = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    // starting positions of the players and the ball
    private double player1XPos = 0;
    private double player1YPos = height / 2;
    private double player2XPos = width - playerWidth;
    private double player2YPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = width / 2;
    // scores
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    // game state
    private boolean gameStarted;

    @Override
    public void start(Stage stage) throws Exception {
        // title
        stage.setTitle("P O N G   F X");
        // create a canvas
        Canvas canvas = new Canvas(width, height);
        // get the graphics context
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // create a timeline for animation
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e->run(gc)));
        // repeat animation indefinitely
        tl.setCycleCount(Timeline.INDEFINITE);
        
        // bind controls - mouse = player Y, click to start
        canvas.setOnMouseMoved(e->player1YPos = e.getY());
        canvas.setOnMouseClicked(e->gameStarted = true);
        
        // set the scene and show the stage. Play the timeline
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }
    
    private void run(GraphicsContext gc) {
        // draw the BG
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        
        // set text colour and font
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        
        // playing
        if (gameStarted) {
            // change the position based on the speed
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            // AI Player logic
            // ball on the left 3/4 of the screen
            if (ballXPos < width - width / 4) {
                player2YPos = ballYPos - playerHeight / 2;
            } else { // right 1/4 of the screen (with the movement limited to 1 unit per iteration, this gives the AI some room to fail when the ball is really fast)
                // move 1 unit up if the ball is above the paddle, else one unit down
                player2YPos = (ballYPos > player2YPos + playerHeight / 2) ? player2YPos += 1 : player2YPos - 1;
            }

            // draw ball
            gc.fillOval(ballXPos, ballYPos, ballRadius, ballRadius);
        } else {
            // start text
            gc.setStroke((Color.WHITE));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click to Start", width / 2, height / 2);
            // reset the ball
            ballXPos = width / 2;
            ballYPos = height / 2;
            // reset to a random speed (=> direction)
            ballXSpeed = (new Random().nextInt(2) == 0) ? 1 : -1;
            ballYSpeed = (new Random().nextInt(2) == 0) ? 1 : -1;
        }
        
        // confine ball to the canvas. We don't need to do this for x because going out of bounds horizontally = someone scored a point
        if (ballYPos > height || ballYPos < 0) {
            // reflect y
            ballYSpeed *= -1;
        }
        
        // scoring
        // P1 missed
        if (ballXPos < player1XPos - playerWidth) {
            // = P2 scored
            ++scoreP2;
            gameStarted = false;
        }
        // P2 missed
        if (ballXPos > player2XPos + playerWidth) {
            // = P1 scored
            ++scoreP1;
            gameStarted = false;
        }
        
        // increase the ball speed each time the ball is hit
        if (((ballXPos < player1XPos + playerWidth) && ballYPos >= player1YPos && ballYPos <= player1YPos + playerHeight) || ((ballXPos + ballRadius > player2XPos) && ballYPos >= player2YPos && ballYPos <= player2YPos + playerHeight)) {
            // add a magnitude of 1 unit in the current direction
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            // and reflect the direction
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }
        
        // draw the score
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
        
        // draw the paddles
        gc.fillRect(player2XPos, player2YPos, playerWidth, playerHeight);
        gc.fillRect(player1XPos, player1YPos, playerWidth, playerHeight);
    }

    public static void main(String[] args) {
        launch();
    }
}