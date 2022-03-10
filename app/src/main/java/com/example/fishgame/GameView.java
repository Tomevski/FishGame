package com.example.fishgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static com.example.fishgame.utils.Constants.CIRCLE_RADIUS;
import static com.example.fishgame.utils.Constants.COORDINATE_Y_POS;
import static com.example.fishgame.utils.Constants.GB_RB_SPEED;
import static com.example.fishgame.utils.Constants.GREEN_SCORE;
import static com.example.fishgame.utils.Constants.HEART_POS;
import static com.example.fishgame.utils.Constants.HIT_BALL_SPEED;
import static com.example.fishgame.utils.Constants.LIFE_NUMBER;
import static com.example.fishgame.utils.Constants.POS_CONSTANT;
import static com.example.fishgame.utils.Constants.REDBALL_CIRCLE_RADIUS;
import static com.example.fishgame.utils.Constants.SCORE_COOR_X;
import static com.example.fishgame.utils.Constants.SCORE_COOR_Y;
import static com.example.fishgame.utils.Constants.SPEED_DECREASER;
import static com.example.fishgame.utils.Constants.TEXT_SIZE;
import static com.example.fishgame.utils.Constants.YB_SPEED;
import static com.example.fishgame.utils.Constants.YELLOW_SCORE;

public class GameView extends View {

    private Bitmap fish[] = new Bitmap[2];

    private int coordinate_X = 10;
    private int coordinate_Y;
    private int Fspeed;
    private int canvasWidth, canvasHeight;
    private int score, lifeCounter;
    private boolean touch;
    private Bitmap backgroundImage;
    private Paint scorepaint = new Paint();
    private Bitmap life[] = new Bitmap[2];
    //Yellow ball
    private int yellowX, yellowY, yellowSpeed = YB_SPEED;
    private Paint yellowPaint = new Paint();
    //Green ball
    private int greenX, greenY, greenSpeed = GB_RB_SPEED;
    private Paint greenPaint = new Paint();
    //Red ball
    private int redX, redY, redSpeed = GB_RB_SPEED;
    private Paint redPaint = new Paint();


    public GameView(Context context) {
        super(context);

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);


        //Yellow
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);
        //Green
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);
        //Red
        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);


        scorepaint.setColor(Color.RED);
        scorepaint.setTextSize(TEXT_SIZE);
        scorepaint.setTypeface(Typeface.DEFAULT);
        scorepaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        coordinate_Y = COORDINATE_Y_POS;

        score = 0;
        lifeCounter = LIFE_NUMBER;

    }
    //Game logic
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage, 0, 0, null);

        int minY = fish[0].getHeight();
        int maxY = canvasHeight - fish[0].getHeight() * 3;

        coordinate_Y = coordinate_X + Fspeed;

        if (coordinate_Y < minY) {
            coordinate_Y = minY;
        }
        if (coordinate_Y > maxY) {
            coordinate_Y = maxY;
        }

        Fspeed = Fspeed + 10;

        if (touch) {
            canvas.drawBitmap(fish[1], coordinate_X, coordinate_Y, null);
            touch = false;

        } else {
            canvas.drawBitmap(fish[0], coordinate_X, coordinate_Y, null);
        }
        //Yellow ball fx
        yellowX = yellowX - yellowSpeed;

        if (hitBallChecker(yellowX, yellowY)) {
            score = score + YELLOW_SCORE;
            yellowX = HIT_BALL_SPEED;

        }

        if (yellowX < 0) {
            yellowX = canvasWidth + POS_CONSTANT;
            yellowY = (int) Math.floor(Math.random() * (maxY - minY)) + minY;
        }
        canvas.drawCircle(yellowX, yellowY, CIRCLE_RADIUS, yellowPaint);

        //Green ball fx
        greenX = greenX - greenSpeed;

        if (hitBallChecker(greenX, greenY)) {
            score = score + GREEN_SCORE;
            greenX = HIT_BALL_SPEED;

        }

        if (greenX < 0) {
            greenX = canvasWidth + POS_CONSTANT;
            greenY = (int) Math.floor(Math.random() * (maxY - minY)) + minY;
        }
        canvas.drawCircle(greenX, greenY, CIRCLE_RADIUS, greenPaint);
        //Red ball fx

        redX = redX - redSpeed;

        if (hitBallChecker(redX, redY)) {
            redX = HIT_BALL_SPEED;
            lifeCounter--;

            if (lifeCounter == 0) {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);
            }

        }

        if (redX < 0) {
            redX = canvasWidth + POS_CONSTANT;
            redY = (int) Math.floor(Math.random() * (maxY - minY)) + minY;
        }
        canvas.drawCircle(redX, redY, REDBALL_CIRCLE_RADIUS, redPaint);
        //Score fx
        canvas.drawText("My score : " + score, SCORE_COOR_X, SCORE_COOR_Y, scorepaint);

        //Red ball life decreasing fx
        for (int i = 0; i < 3; i++) {
            int coordinate_x = (int) (HEART_POS + life[0].getWidth() * 1.5 * i);
            int coordinate_y = 30;

            if (i < lifeCounter) {
                canvas.drawBitmap(life[0], coordinate_x, coordinate_y, null);
            } else {
                canvas.drawBitmap(life[1], coordinate_x, coordinate_y, null);
            }
        }


    }

    public boolean hitBallChecker(int x, int y) {
        if (coordinate_X < x && x < (coordinate_X + fish[0].getWidth()) && coordinate_Y < y && y < (coordinate_Y + fish[0].getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;

            Fspeed = SPEED_DECREASER;
        }

        return true;
    }
}
