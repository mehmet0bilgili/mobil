package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";
    boolean player1Turn = true;

    byte[][] board = new byte[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) row.getChildAt(j);
                btn.setOnClickListener(new CellListener(i, j));
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return board[row][col] == 0;
    }

    public int gameEnded(int row, int col) {
        int symbol = board[row][col];
        boolean win = true;
        for (int i = 0; i < 3; i++) {
            if (board[i][col] != symbol) {
                win = false;
                break;
            }
        }
        if (win) {
            return symbol;
        }

        return -1;

        /*for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return 1; //for rows
            else if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return 1; //for columns
            else if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return 1; // from upper left to bottom right diagnol
            else if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return 1; // from upper right to bottom left diagnol
        }
        return -1;*/
    }

    class CellListener implements View.OnClickListener{
        int row, col;

        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if(!isValidMove(row,col)) {
                Toast.makeText(MainActivity.this, "Cell is full.", Toast.LENGTH_LONG).show();
                return;
            }
            if (player1Turn) {
                ((Button)v).setText(PLAYER_1);
                board[row][col] = 1;
            } else {
                ((Button)v).setText(PLAYER_2);
                board[row][col] = 2;
            }
            if (gameEnded(row, col) == -1) {
                player1Turn =! player1Turn;
            } else if(gameEnded(row,col) == 0) {
                Toast.makeText(MainActivity.this, "DRAW.", Toast.LENGTH_LONG).show();
            } else if(gameEnded(row,col) == 1) {
                Toast.makeText(MainActivity.this, "Player1 won.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this, "Player2 won.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("Player1Turn", player1Turn);
        byte[] boardSingle = new byte[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardSingle[3+i+j] = board[i][j];
            }
        }
        outState.putByteArray("board", boardSingle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        byte [] boardSingle = savedInstanceState.getByteArray("board");
        for (int i = 0; i < 9; i++) {
            board[i/3][i%3] = boardSingle[i];
        }
        TableLayout table = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                if (board[i][j] == 1) {
                    button.setText("X");
                } else if (board[i][j] == 2) {
                    button.setText("O");
                }
            }
        }
    }
}