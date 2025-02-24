package com.arastudent.tid0093.mazegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.arastudent.tid0093.mazegame.model.Color;
import com.arastudent.tid0093.mazegame.model.Direction;
import com.arastudent.tid0093.mazegame.model.Game;
import com.arastudent.tid0093.mazegame.model.Message;
import com.arastudent.tid0093.mazegame.model.Shape;
import com.arastudent.tid0093.mazegame.model.Square;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button resetButton;
    private Group imageViewGroup;
    private Group startViewGroup;
    private TextView movesTextView;
    private TextView levelTextView;
    private TextView goalsTextView;
    private int moveCount;

    // Initialize the game
    private Game game;

    //Bitmaps
    private Bitmap eyesUpBitmap;
    private Bitmap eyesDownBitmap;
    private Bitmap eyesLeftBitmap;
    private Bitmap eyesRightBitmap;
    private Bitmap goalBitmap;

    private int previousRow = -1;
    private int previousColumn = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        startButton = findViewById(R.id.start_button);
        resetButton = findViewById(R.id.reset_button);
        imageViewGroup = findViewById(R.id.levelView);
        startViewGroup = findViewById(R.id.startView);
        movesTextView = findViewById(R.id.movesTextView);
        levelTextView = findViewById(R.id.levelTextView);
        goalsTextView = findViewById(R.id.goalsTextView);

        startViewGroup.setVisibility(View.VISIBLE);

        // Load bitmaps
        eyesUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyesu);
        eyesDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyesd);
        eyesLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyesl);
        eyesRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyesr);
        goalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goal);

        // Set the button click listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGameLevel();
                // Initialize and start the game logic here
                startGame();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void startGame() {
        // Your game initialization logic here
        moveCount = 0;
        game = new Game();
        game.addLevel(5, 3);
        firstLevel();
    }

    protected void resetGame() {
        // Reset game logic here
        startGame();
        updateUI();
    }

    private void initiateGameLevel() {
        // Make the game level visible
        // Hide the start button and instruction
        imageViewGroup.setVisibility(View.VISIBLE);
        startViewGroup.setVisibility(View.GONE);
    }

    public void onClickToMove(View view) {
        String description = (String) view.getContentDescription();
        String[] pos = description.split("-");
        int destRow = Integer.parseInt(pos[0]);
        int destColumn = Integer.parseInt(pos[1]);

        if (game.canMoveTo(destRow, destColumn)) {
            game.moveTo(destRow, destColumn);
            if (destRow == game.getEyeballRow() && destColumn == game.getEyeballColumn()) {
                // Only count the valid move if it is not the current location
                moveCount += 1;
            }
            updateUI();
        } else {
            if (game.checkDirectionMessage(destRow, destColumn) != Message.OK) {
                switch (game.checkDirectionMessage(destRow, destColumn)) {
                    case MOVING_DIAGONALLY:
                        Toast.makeText(this, "You are not allowed to move Diagonally", Toast.LENGTH_SHORT).show();
                        break;
                    case BACKWARDS_MOVE:
                        Toast.makeText(this, "You are not allowed to move Backwards", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            } else if (game.MessageIfMovingTo(destRow, destColumn) == Message.DIFFERENT_SHAPE_OR_COLOR){
                Toast.makeText(this, "You can only move to a square with the same Colour OR Shape", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUI() {
        // Update other UI components
        int playerRow = game.getEyeballRow();
        int playerColumn = game.getEyeballColumn();
        Direction playerDirection = game.getEyeballDirection();

        // Restore the original tile image at the previous position
        if (previousRow != -1 && previousColumn != -1) {
            String prevImageViewId = "imageView" + (previousRow * 3 + previousColumn + 1);
            ImageView prevImageView = findImageViewByTag(prevImageViewId);
            prevImageView.setImageBitmap(getTileBitmap(previousRow, previousColumn));
        }

        String imageViewId = "imageView" + (playerRow * 3 + playerColumn + 1);
        ImageView playerImageView = findImageViewByTag(imageViewId);

        // Get the underlying tile image
        Bitmap originalTileBitmap = getTileBitmap(playerRow, playerColumn);

        // Combine the eye bitmap with the original tile bitmap
        Bitmap combinedBitmap = combineBitmaps(originalTileBitmap, getEyeBitmap(playerDirection));

        playerImageView.setImageBitmap(combinedBitmap);

        // Update previous position
        previousRow = playerRow;
        previousColumn = playerColumn;

        movesTextView.setText("Moves: " + moveCount);
        levelTextView.setText("Level: " + game.getCurrentLevel());
        goalsTextView.setText("Goals Completed: " + game.getCompletedGoalCount() + " of " + game.getGoalCount());

        if (game.getGoalCount() == 0){
            showWinDialog();
        }
    }

    private ImageView findImageViewByTag(String tag) {
        int resId = getResources().getIdentifier(tag, "id", getPackageName());
        return findViewById(resId);
    }

    private Bitmap getTileBitmap(int row, int column) {
        Square square = game.getSquare(row, column);
        int resId = getTileResourceId(square);
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    //this is to get the correct image based of shape and color
    private int getTileResourceId(Square square) {
        if (square == null) return R.drawable.blank;
        switch (square.getShape()) {
            case STAR:
                if (square.getColor() == Color.GREEN) return R.drawable.gs;
                if (square.getColor() == Color.YELLOW) return R.drawable.ys;
                if (square.getColor() == Color.RED) return R.drawable.rs;
                if (square.getColor() == Color.BLUE) return R.drawable.bs;
                break;
            case FLOWER:
                if (square.getColor() == Color.BLUE) return R.drawable.bf;
                if (square.getColor() == Color.RED) return R.drawable.rf;
                if (square.getColor() == Color.GREEN) return R.drawable.gf;
                if (square.getColor() == Color.YELLOW) return R.drawable.yf;
                break;
            case CROSS:
                if (square.getColor() == Color.GREEN) return R.drawable.gc;
                if (square.getColor() == Color.YELLOW) return R.drawable.yc;
                if (square.getColor() == Color.BLUE) return R.drawable.bc;
                if (square.getColor() == Color.RED) return R.drawable.rc;
                break;
            case DIAMOND:
                if (square.getColor() == Color.RED) return R.drawable.rd;
                if (square.getColor() == Color.YELLOW) return R.drawable.yd;
                if (square.getColor() == Color.BLUE) return R.drawable.bd;
                if (square.getColor() == Color.GREEN) return R.drawable.gd;
                break;
            case BLANK:
            default:
                return R.drawable.blank;
        }
        return R.drawable.blank; // default case
    }

    //this is to link the bitmaps for the direction being faced
    private Bitmap getEyeBitmap(Direction direction) {
        switch (direction) {
            case UP:
                return eyesUpBitmap;
            case DOWN:
                return eyesDownBitmap;
            case LEFT:
                return eyesLeftBitmap;
            case RIGHT:
                return eyesRightBitmap;
            default:
                return null;
        }
    }

    private Bitmap combineBitmaps(Bitmap background, Bitmap overlay) {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(overlay, 0, 0, null);
        return combinedBitmap;
    }

    private void showWinDialog() {
        int moves = moveCount; // Assuming moveCount is your move counter
        WinDialog dialog = WinDialog.newInstance(moves);
        dialog.show(getSupportFragmentManager(), "WinDialog");
    }

    private void firstLevel() {
        game.setLevel(0);
        game.addSquare(new Square(Color.BLANK, Shape.BLANK), 0, 0);
        game.addSquare(new Square(Color.GREEN, Shape.STAR), 0, 1);
        game.addSquare(new Square(Color.BLANK, Shape.BLANK), 0, 2);
        game.addSquare(new Square(Color.RED, Shape.FLOWER), 1, 0);
        game.addSquare(new Square(Color.BLUE, Shape.FLOWER), 1, 1);
        game.addSquare(new Square(Color.RED, Shape.DIAMOND), 1, 2);
        game.addSquare(new Square(Color.GREEN, Shape.CROSS), 2, 0);
        game.addSquare(new Square(Color.YELLOW, Shape.STAR), 2, 1);
        game.addSquare(new Square(Color.YELLOW, Shape.CROSS), 2, 2);
        game.addSquare(new Square(Color.GREEN, Shape.FLOWER), 3, 0);
        game.addSquare(new Square(Color.BLUE, Shape.CROSS), 3, 1);
        game.addSquare(new Square(Color.YELLOW, Shape.FLOWER), 3, 2);
        game.addSquare(new Square(Color.BLANK, Shape.BLANK), 4, 0);
        game.addSquare(new Square(Color.BLANK, Shape.BLANK), 4, 1);
        game.addSquare(new Square(Color.RED, Shape.STAR), 4, 2);
        game.addGoal(0, 1);
        game.addEyeball(4, 2, Direction.UP);
        Bitmap originalTileBitmap = getTileBitmap(0, 1);
        ImageView playerImageView = findViewById(R.id.imageView2);
        Bitmap combinedBitmap = combineBitmaps(originalTileBitmap, goalBitmap);
        playerImageView.setImageBitmap(combinedBitmap);

        updateUI();
    }
}