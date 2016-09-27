package com.hemkars.flappy.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture backGround;

    Texture[] birds;
    int flapState;
    int gameState = 0;
    float birdY = 0;
    float velocity = 0;



    Texture topTube;
    Texture bottomTube;
    float gap=325;
    float maxTubeOffset;
    Random randomGenerator;


    float  tubeVelocity=4;

    int noOfTubes=4;
    float[] tubex= new float[noOfTubes];
    float[] tubeOffset= new float[noOfTubes];

    float distanceBetweenTubes;


    Circle birdCircle;
    ShapeRenderer shapeRenderer;

    Rectangle[] topRectangles;
    Rectangle[] bottomRectangles;

    int score=0;
    int scoringTube=0;
    BitmapFont font=null;
    Texture gameover;

    @Override
    public void create() {
        shapeRenderer= new ShapeRenderer();
        batch = new SpriteBatch();
        backGround = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");


        topTube= new Texture("toptube.png");
        bottomTube= new Texture("bottomtube.png");
        maxTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
        randomGenerator= new Random();

        birdCircle= new Circle();
        topRectangles= new Rectangle[noOfTubes];
        bottomRectangles= new Rectangle[noOfTubes];

        font= new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5);

        gameover= new Texture("gameover.png");

        //tubex=Gdx.graphics.getWidth()/2-topTube.getWidth()/2;

        distanceBetweenTubes=Gdx.graphics.getWidth()/2;



        startGame();


    }

    public void startGame(){
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        for(int i=0; i < noOfTubes; i++){
            tubeOffset[i]= (randomGenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap-200);
            tubex[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2 +Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topRectangles[i]= new Rectangle();
            bottomRectangles[i]= new Rectangle();
        }
        }

    @Override
    public void render() {
        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState ==1) {
            if(tubex[scoringTube]<Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("score ",String.valueOf(score));
                if(scoringTube <  noOfTubes-1){
                    scoringTube++;

                }else{
                    scoringTube=0;
                }
            }

            if (Gdx.input.justTouched()) {
                Gdx.app.log("Touched", "Yup");
                velocity=-20;
               // gameState = 1;

            }

            for(int i=0;i<4;i++) {
                if(tubex[i]<-topTube.getWidth()){
                    tubex[i] += noOfTubes*distanceBetweenTubes;
                    tubeOffset[i]= (randomGenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap-200);
                }else {
                    tubex[i]=tubex[i]-tubeVelocity;

                }
                //tubex[i] = tubex[i] - tubeVelocity;
                batch.draw(topTube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], 100, topTube.getHeight());
                batch.draw(bottomTube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], 100, bottomTube.getHeight());
                topRectangles[i]= new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],100,topTube.getHeight());
                bottomRectangles[i]= new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],100,bottomTube.getHeight());
            }
            if(birdY >0 ) {
                velocity++;
                birdY -= velocity;
            }else{
                gameState=2;
            }

        } else if(gameState==0){
            if (Gdx.input.justTouched()) {

                Gdx.app.log("Touched", "Yup");
                gameState = 1;
            }
        }else if(gameState==2){
            batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
            if (Gdx.input.justTouched()) {

                Gdx.app.log("Touched", "Yup");
                gameState = 1;
                startGame();
                score=0;
                scoringTube=0;
                velocity=0;
            }

        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY, 90, 90);
        font.draw(batch,String.valueOf(score),100,150);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth() / 2 -20 ,birdY+birds[flapState].getHeight()/2,40);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);

       // shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i=0;i<4;i++) {
            //shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],100,topTube.getHeight());
            //shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],100,bottomTube.getHeight());

            if((Intersector.overlaps(birdCircle,topRectangles[i]) ) || Intersector.overlaps(birdCircle,bottomRectangles[i])){
               // Gdx.app.log("Collision","yes");
                gameState=2;
            }
        }
        //shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backGround.dispose();
    }
}
