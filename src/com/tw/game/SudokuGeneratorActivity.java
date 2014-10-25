package com.tw.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tw.game.factory.SudokuFactory;
import com.tw.game.result.Error;
import com.tw.game.result.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuGeneratorActivity extends Activity {
    TextView selectedTextView;

    Sudoku sudoku = new Sudoku(new SudokuFactory());
    List<List<Integer>> sudokuPuzzle = sudoku.getPuzzle();
    List<List<Integer>> sudokuGrid = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sudoku);
        sudoku.generatePuzzle();

        sudokuGrid.add(Arrays.asList(R.id.r0_c0, R.id.r0_c1, R.id.r0_c2, R.id.r0_c3, R.id.r0_c4, R.id.r0_c5, R.id.r0_c6, R.id.r0_c7, R.id.r0_c8));
        sudokuGrid.add(Arrays.asList(R.id.r1_c0, R.id.r1_c1, R.id.r1_c2, R.id.r1_c3, R.id.r1_c4, R.id.r1_c5, R.id.r1_c6, R.id.r1_c7, R.id.r1_c8));
        sudokuGrid.add(Arrays.asList(R.id.r2_c0, R.id.r2_c1, R.id.r2_c2, R.id.r2_c3, R.id.r2_c4, R.id.r2_c5, R.id.r2_c6, R.id.r2_c7, R.id.r2_c8));
        sudokuGrid.add(Arrays.asList(R.id.r3_c0, R.id.r3_c1, R.id.r3_c2, R.id.r3_c3, R.id.r3_c4, R.id.r3_c5, R.id.r3_c6, R.id.r3_c7, R.id.r3_c8));
        sudokuGrid.add(Arrays.asList(R.id.r4_c0, R.id.r4_c1, R.id.r4_c2, R.id.r4_c3, R.id.r4_c4, R.id.r4_c5, R.id.r4_c6, R.id.r4_c7, R.id.r4_c8));
        sudokuGrid.add(Arrays.asList(R.id.r5_c0, R.id.r5_c1, R.id.r5_c2, R.id.r5_c3, R.id.r5_c4, R.id.r5_c5, R.id.r5_c6, R.id.r5_c7, R.id.r5_c8));
        sudokuGrid.add(Arrays.asList(R.id.r6_c0, R.id.r6_c1, R.id.r6_c2, R.id.r6_c3, R.id.r6_c4, R.id.r6_c5, R.id.r6_c6, R.id.r6_c7, R.id.r6_c8));
        sudokuGrid.add(Arrays.asList(R.id.r7_c0, R.id.r7_c1, R.id.r7_c2, R.id.r7_c3, R.id.r7_c4, R.id.r7_c5, R.id.r7_c6, R.id.r7_c7, R.id.r7_c8));
        sudokuGrid.add(Arrays.asList(R.id.r8_c0, R.id.r8_c1, R.id.r8_c2, R.id.r8_c3, R.id.r8_c4, R.id.r8_c5, R.id.r8_c6, R.id.r8_c7, R.id.r8_c8));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                EditText number = (EditText) findViewById(sudokuGrid.get(i).get(j));

                if (sudokuPuzzle.get(i).get(j) != null) {
                    number.setText(String.valueOf(sudokuPuzzle.get(i).get(j)));
                    number.setTypeface(null, Typeface.BOLD_ITALIC);
                    number.setFocusable(false);
                    number.setTextColor(Color.BLACK);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(number.getWindowToken(), 0);

                number.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        selectedTextView = (TextView) view;
                        return false;
                    }
                });
            }
        }
    }

    public void editField(View view) {
        if (selectedTextView == null) return;
        selectedTextView.setText(((Button) view).getText());
    }

    public void solvePuzzle(View view) {
        ArrayList<ArrayList<Integer>> solvedPuzzle = sudoku.getSolvedPuzzle();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                EditText number = (EditText) findViewById(sudokuGrid.get(i).get(j));
                number.setText(String.valueOf(solvedPuzzle.get(i).get(j)));
                number.setTextColor(Color.parseColor("#2709E6"));
                if (sudokuPuzzle.get(i).get(j) != null) {
                    number.setText(String.valueOf(solvedPuzzle.get(i).get(j)));
                    number.setTypeface(null, Typeface.BOLD_ITALIC);
                    number.setFocusable(false);
                    number.setTextColor(Color.BLACK);
                }
            }
        }
    }

    public void showResult(View view) {
        List<List<Integer>> userSolution = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            userSolution.add(new ArrayList<Integer>());
            for (int j = 0; j < 9; j++) {
                TextView textView = (TextView) findViewById(sudokuGrid.get(i).get(j));
                userSolution.get(i).add(Integer.parseInt(String.valueOf(textView.getText())));
            }
        }
        Result result = this.sudoku.validateSolution(userSolution);
        if (result.isCorrect()) {
            Intent yesAction = new Intent(SudokuGeneratorActivity.this, SudokuGeneratorActivity.class);
            alertMessageBuilder("Congratulations! You won. Do you want to start a new game?", yesAction, 0);
        } else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    EditText number = (EditText) findViewById(sudokuGrid.get(i).get(j));
                    if(number.getCurrentTextColor() == getResources().getColor(R.color.error_background))
                        ((EditText) findViewById(sudokuGrid.get(i).get(j))).setTextColor(Color.parseColor("#2709E6"));
                }
            }
            for (Error error : result.getErrors())
                ((EditText) findViewById(sudokuGrid.get(error.getRow()).get(error.getColumn()))).setTextColor(getResources().getColor(R.color.error_background));
            Toast.makeText(this, "Your Solution is not right.", Toast.LENGTH_LONG).show();
        }
    }

    private void alertMessageBuilder(String message, final Intent yesAction, final int status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(yesAction);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}