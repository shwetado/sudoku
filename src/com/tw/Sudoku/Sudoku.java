package com.tw.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sudoku {
    private List<ArrayList<Integer>> solvedPuzzle = new ArrayList<>();
    private List<List<Integer>> puzzle = new ArrayList<>();
    private NumberGenerator generator;

    public Sudoku(NumberGenerator generator) {
        this.generator = generator;
    }
    public List<ArrayList<Integer>> getSolvedPuzzle() {
        return solvedPuzzle;
    }
    public List<List<Integer>> getPuzzle() {
        return puzzle;
    }

    public void generatePuzzle() {
        for (int i = 0; i < 9; i++) {
            this.puzzle.add(Arrays.asList(new Integer[9]));
            List<Integer> indexes = this.generator.getNumbers();
            for (int j = 0; j < 3; j++)
                this.puzzle.get(i).set(indexes.get(j) - 1, this.getSolvedPuzzle().get(i).get(indexes.get(j) - 1));
        }
    }

    public void createSolvedPuzzle() {
        int size = 3;
        List<ArrayList<ArrayList<Integer>>> puzzle = getPuzzleWithFirstLineInserted(size);
        int[][] orders = {{1, 2, 0}, {2, 0, 1}};
        insertRowsInBlock(puzzle, size, orders, 0, new int[]{0, 1, 2});
        puzzle.add(3, order2dArrayElements(createCopy(puzzle.get(0)), orders[0]));
        insertRowsInBlock(puzzle, size, orders, 3, orders[0]);
        puzzle.add(6, createCopy(puzzle.get(0)));
        insertRowsInBlock(puzzle, size, orders, 6, orders[1]);
        formatPuzzle(puzzle);
    }

    public boolean isUserSolutionRight(List<List<Integer>> solvedPuzzle){
        boolean isCorrect = true;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if(solvedPuzzle.get(i).get(j) != this.getSolvedPuzzle().get(i).get(j).intValue())
                    isCorrect = false;
        return isCorrect;
    }

    private void insertRowsInBlock(List<ArrayList<ArrayList<Integer>>> puzzle, int size, int[][] orders, int index, int[] order) {
        for (int row = 0; row < size; row++) {
            List<Integer> numbers = puzzle.get(index).get(row);
            for (int i : order)    numbers.add(numbers.get(i));
            for (int i = 2; i >= 0; i--)   numbers.remove(i);
        }
        for (int[] order1 : orders)
            puzzle.add(order2dArrayElements(createCopy(puzzle.get(index)), order1));
    }

    private ArrayList<ArrayList<Integer>> order2dArrayElements(ArrayList<ArrayList<Integer>> numbers, int[] order) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        for (int i : order)    list.add(numbers.get(i));
        return list;
    }

    private ArrayList<ArrayList<Integer>> createCopy(ArrayList<ArrayList<Integer>> numbers) {
        ArrayList<ArrayList<Integer>> copy = new ArrayList<>();
        for (ArrayList<Integer> number : numbers)   copy.add((ArrayList<Integer>) number.clone());
        return copy;
    }

    private List<ArrayList<ArrayList<Integer>>> getPuzzleWithFirstLineInserted(int size) {
        List<Integer> numbers = this.generator.getNumbers();
        List<ArrayList<ArrayList<Integer>>> puzzle = new ArrayList<>();
        puzzle.add(new ArrayList<>());
        for (int row = 0; row < size; row++) {
            puzzle.get(0).add(new ArrayList<>());
            for (int column = 0; column < size; column++)   puzzle.get(0).get(row).add(column, numbers.get((row * 3) + column));
        }
        return puzzle;
    }

    private void formatPuzzle(List<ArrayList<ArrayList<Integer>>> puzzle) {
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)    numbers.add(puzzle.get(i).get(j).get(k));
            this.solvedPuzzle.add(numbers);
        }
    }
}