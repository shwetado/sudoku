package com.tw.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tw.game.checker.SolutionChecker;
import com.tw.game.result.Cell;
import com.tw.game.solver.SudokuSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuSolverActivity extends Activity {
    private TextView selectedTextView;
    private List<List<Integer>> sudokuGrid = new ArrayList<>();
    private List<List<Integer>> puzzle = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.solver);
        SudokuActivity.addTextViews(this.sudokuGrid);
        setEditTextProperties();
    }

    public void showResult(View view) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            puzzle.add(Arrays.asList(new Integer[9]));
            for (int j = 0; j < 9; j++) {
                EditText editText = (EditText) findViewById(sudokuGrid.get(i).get(j));
                if (editText.getText().toString().equals("")) {
                    count++;
                    puzzle.get(i).set(j, 0);
                } else puzzle.get(i).set(j, Integer.parseInt(editText.getText().toString()));
            }
        }
        String message = "Do you want to solve this puzzle ?";
        if (!new SolutionChecker().validateSolution(puzzle).isCorrect() || count <= 0) {
            Toast.makeText(this, "Invalid Puzzle", Toast.LENGTH_LONG).show();
            return;
        } else if (count == 81)
            message = "Do you want to solve empty Grid?";
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        showSolvePuzzle(new SudokuSolver(new SolutionChecker()).solvePuzzle(puzzle));
                    }
                }).setNegativeButton("No", null);
        builder.create().show();
    }

    public void clearPuzzle(View view) {
        setEditTextProperties();
    }

    public void loadPuzzle(View view) {
        finish();
        startActivity(new Intent(this, SudokuGeneratorActivity.class));
    }

    public void editField(View view) {
        SudokuActivity.editField(view, selectedTextView);
    }

    public void clearNumber(View view) {
        SudokuActivity.clearNumber(selectedTextView);
    }

    private void setEditTextProperties() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                EditText number = (EditText) findViewById(sudokuGrid.get(i).get(j));
                number.setText("");
                number.setInputType(InputType.TYPE_NULL);
                number.setKeyListener(null);
                number.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        selectedTextView = (TextView) view;
                        return false;
                    }
                });
                number.setOnFocusChangeListener(SudokuActivity.onFocusChangeListener);
            }
    }

    private void showSolvePuzzle(List<List<Integer>> solvedPuzzle) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                EditText number = (EditText) findViewById(sudokuGrid.get(i).get(j));
                SudokuActivity.setTextColor(solvedPuzzle, i, j, number);
                SudokuActivity.setProperties(puzzle, solvedPuzzle, new Cell(i, j), number, 0, true);
            }
        }
    }
}