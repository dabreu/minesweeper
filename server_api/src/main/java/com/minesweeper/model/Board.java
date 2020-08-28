package com.minesweeper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class models the Board for a Minesweeper Game.
 *
 */
public class Board {

    /** board's dimension parameters **/
    private int rows;
    private int columns;

    /** number of mines that the board contains **/
    private int mines;

    /**
     * matrix of cells representing each of the position's state of the board
     **/
    private List<List<Cell>> cells;

    /** number of cells already uncovered **/
    private int cellsUncovered;

    /** indicator that the board can be used (or not) to play **/
    private boolean finished;

    public Board(int rows, int columns, int mines) {
        validateParameters(rows, columns, mines);
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.cells = new ArrayList<List<Cell>>();
        this.cellsUncovered = 0;
        this.finished = false;
        initializeBoard();
        scatterMines();
    }

    /**
     * Reveals/uncovers a cell given its position. If the cell contains a mine,
     * then the board is finished. If the cell has no adjacent mines, then the
     * uncover is propagated to its adjacent cells (square) in cascade
     * 
     * @param row
     * @param column
     * @return
     */
    public Board uncover(int row, int column) {
        validateCanDoAction();
        validatePosition(row, column);
        doUncover(new CellPosition(row, column));
        return this;
    }

    /**
     * Sets a red flag on a cell given its position.
     * 
     * @param row
     * @param column
     * @return
     */
    public Board setRedFlag(int row, int column) {
        validateCanDoAction();
        validatePosition(row, column);
        getCell(row, column).setRedFlag();
        return this;
    }

    /**
     * Sets a question mark on a cell given its position
     * 
     * @param row
     * @param column
     * @return
     */
    public Board setQuestionMark(int row, int column) {
        validateCanDoAction();
        validatePosition(row, column);
        getCell(row, column).setQuestionMark();
        return this;
    }

    /**
     * Uncovers a given cell and, if it has not adjacent mines, propagates the
     * action to its adjacent cells
     * 
     * @param cellPosition
     */
    private void doUncover(CellPosition cellPosition) {
        Cell cell = getCell(cellPosition);
        if (cell.isCovered()) {
            cell.uncover();
            cellsUncovered++;
            if (!cell.hasAdjacentMines()) {
                // if the cell has not adjacent mines, then propagate the
                // uncover to its
                // adjacent cells
                getAdjacents(cellPosition).forEach(position -> doUncover(position));
            }
        }
        // the board will be finished when a mine is found or all the cells
        // without mine
        // are uncovered
        finished = (cell.hasMine() || areAllCellsWithoutMineUncovered());
    }

    /**
     * Initializes the board cells
     */
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            List<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < columns; j++) {
                row.add(new Cell());
            }
            cells.add(row);
        }
    }

    /**
     * Scatters the mines across the board
     */
    private void scatterMines() {
        int minesScattered = 0;
        while (minesScattered < mines) {
            CellPosition position = getRandomCellPosition();
            Cell cell = getCell(position);
            if (!cell.hasMine()) {
                getCell(position).addMine();
                incrementMineCounterOnAdjacentCells(position);
                minesScattered++;
            }
        }
    }

    protected Cell getCell(CellPosition position) {
        return getCell(position.x, position.y);
    }

    protected Cell getCell(int x, int y) {
        return cells.get(x).get(y);
    }

    private CellPosition getRandomCellPosition() {
        return new CellPosition(getRandom(this.rows), getRandom(this.columns));
    }

    protected int getRandom(int value) {
        return (int) (Math.random() * value);
    }

    /**
     * Increments the mines counter on the adjacent cells of the given position
     * 
     * @param cellPosition
     */
    private void incrementMineCounterOnAdjacentCells(CellPosition cellPosition) {
        getAdjacents(cellPosition).stream().map(position -> getCell(position)).filter(cell -> !cell.hasMine())
                .forEach(cell -> cell.incrementAdjacentMinesCounter());
    }

    /**
     * Returns the positions corresponding to the adjacent cells (the square) of
     * a given cell position
     * 
     * @param cellPosition
     * @return
     */
    protected List<CellPosition> getAdjacents(CellPosition cellPosition) {
        List<CellPosition> adjacents = new ArrayList<CellPosition>();
        int cellRow = cellPosition.x;
        int cellColumn = cellPosition.y;
        for (int row = cellRow - 1; row <= cellRow + 1; row++) {
            if (isValidRow(row)) {
                for (int column = cellColumn - 1; column <= cellColumn + 1; column++) {
                    if (isValidColumn(column)) {
                        CellPosition adjacent = new CellPosition(row, column);
                        if (!adjacent.equals(cellPosition)) {
                            adjacents.add(adjacent);
                        }
                    }
                }
            }
        }
        return adjacents;
    }

    /**
     * Validates the parameters used to create a Board
     * 
     * @param rows
     * @param columns
     * @param mines
     */
    private void validateParameters(int rows, int columns, int mines) {
        if (rows <= 0) {
            throw new BoardException("invalid rows number: " + rows);
        }
        if (columns <= 0) {
            throw new BoardException("invalid columns number: " + rows);
        }
        if (mines <= 0 || mines >= rows * columns) {
            throw new BoardException("invalid mines number: " + mines);
        }
    }

    private boolean isValidRow(int row) {
        return CellPosition.isInRange(this.rows, row);
    }

    private boolean isValidColumn(int column) {
        return CellPosition.isInRange(this.columns, column);
    }

    private void validatePosition(int row, int column) {
        if (!isValidRow(row)) {
            throw new CellPositionException("invalid row: " + row);
        }
        if (!isValidColumn(column)) {
            throw new CellPositionException("invalid column: " + column);
        }
    }

    private void validateCanDoAction() {
        if (isFinished()) {
            throw new BoardException("action not allowed, the board is finished");
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean areAllCellsWithoutMineUncovered() {
        int totalCellsWithoutMine = rows * columns - mines;
        return (cellsUncovered == totalCellsWithoutMine);
    }
}
