package com.tw.game;

import com.tw.game.checker.Checker;
import com.tw.game.factory.Factory;
import com.tw.game.generator.NumberGenerator;
import com.tw.game.level.SudokuLevels;
import com.tw.game.result.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Sudoku {
    private final List<List<Integer>> solvedPuzzle;
    private final Checker checker;
    private List<List<Integer>> puzzle = new ArrayList<>();
    private final NumberGenerator generator;
    private SudokuLevels difficultyLevel;


    public List<List<Integer>> getSolvedPuzzle() {
        return solvedPuzzle;
    }

    public Sudoku(Factory factory, SudokuLevels difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        this.generator = factory.getRandomNumberGenerator();
        this.solvedPuzzle = factory.getSolutionGenerator().createSolvedPuzzle();
        this.checker = factory.getSolutionChecker();
    }

    public List<List<Integer>> getPuzzle() {
        return puzzle;
    }

    public void generatePuzzle(String level) {
        Map<String, Integer> levels = this.difficultyLevel.getLevels();
        for (int i = 0; i < 9; i++) {
            this.puzzle.add(Arrays.asList(new Integer[9]));
            List<Integer> indexes = this.generator.getNumbers();
            for (int j = 0; j < levels.get(level.toLowerCase()); j++)
                this.puzzle.get(i).set(indexes.get(j) - 1, this.solvedPuzzle.get(i).get(indexes.get(j) - 1));
        }
    }

    public Result validateSolution(List<List<Integer>> solution) {
        return this.checker.validateSolution(solution);
    }
}