package com.example.thegameoflife;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    public Random RANDOM = new Random();
    public int width,height;
    private Cell[][] board;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        board = new Cell[width][height];
        init();
    }

    private void init() {
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = new Cell(i, j, RANDOM.nextBoolean());
            }
        }
    }

    public Cell get(int i, int j) {
        return board[i][j];
    }

    public int nbNeighboursOf(int i, int j) {
        int nb = 0;

        for (int k = i -1; k <= i +1; k++) {
            for (int l = j - 1; l <= j +1; l++) {
                if ((k != i || l != j) && k >= 0
                        && k < width && l >=0 && l < height) {
                    Cell cell = board[k][l];
                    if (cell.alive) {
                        nb++;
                    }
                }
            }
        }
        return nb;
    }

    public void nextGeneration() {
        List<Cell> liveCells = new ArrayList<Cell>();
        List<Cell> deadCells = new ArrayList<Cell>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board[i][j];
                int nbNeighbours = nbNeighboursOf(cell.x, cell.y);

                //Rule #1 + #2
                //Rule #1 Слишком мало соседей - смерть
                //Rule #2 Слишком много соседей - смерть
                if (cell.alive &&
                        (nbNeighbours < 2 || nbNeighbours > 3)) {
                    deadCells.add(cell);
                }

                //Rule #3 Рождение клетки
                if ((cell.alive && (nbNeighbours == 3 || nbNeighbours == 2))
                        ||
                        (!cell.alive && nbNeighbours == 3)) {
                    liveCells.add(cell);
                }
            }
        }

        // Обновление будущих живых и мертвых колонок
        for (Cell cell : liveCells) {
            cell.reborn();
        }
        for (Cell cell : deadCells) {
            cell.die();
        }
    }
}