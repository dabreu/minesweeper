package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CellTest {

    @Test
    public void testNewCellHasNoMine() {
        Cell cell = new Cell();
        assertFalse(cell.hasMine());
    }

    @Test
    public void testNewCellIsCovered() {
        Cell cell = new Cell();
        assertTrue(cell.isCovered());
    }

    @Test
    public void testNewCellHasNoAdjacentMines() {
        Cell cell = new Cell();
        assertFalse(cell.hasAdjacentMines());
    }

    @Test
    public void testCellHasMineAfterAdded() {
        Cell cell = new Cell();
        cell.addMine();
        assertTrue(cell.hasMine());
    }

    @Test
    public void testCellHasAdjacentMinesAfterIncrementingItsCounter() {
        Cell cell = new Cell();
        cell.incrementAdjacentMinesCounter();
        cell.incrementAdjacentMinesCounter();
        cell.incrementAdjacentMinesCounter();
        assertTrue(cell.hasAdjacentMines());
        assertEquals(3, cell.getAdjacentMinesCounter());
    }

    @Test
    public void testUncoverCell() {
        Cell cell = new Cell();
        cell.uncover();
        assertFalse(cell.isCovered());
    }

    @Test
    public void testSetRedFlagOnUncoveredCellThrowsException() {
        Cell cell = new Cell();
        cell.uncover();
        Exception exception = assertThrows(CellException.class, () -> {
            cell.setRedFlag();
        });
        assertTrue(exception.getMessage().contains("cannot add red flag on an uncovered cell"));
    }

    @Test
    public void testSetRedFlagDoesNotUncoverTheCell() {
        Cell cell = new Cell();
        cell.setRedFlag();
        assertTrue(cell.isCovered());
    }

    @Test
    public void testSetRedFlagCanBeDoneOnACellWithQuestionMark() {
        Cell cell = new Cell();
        cell.setQuestionMark();
        cell.setRedFlag();
        assertTrue(cell.isCovered());
    }

    @Test
    public void testSetQuestionMarkOnUncoveredCellThrowsException() {
        Cell cell = new Cell();
        cell.uncover();
        Exception exception = assertThrows(CellException.class, () -> {
            cell.setQuestionMark();
        });
        assertTrue(exception.getMessage().contains("cannot add question mark on an uncovered cell"));
    }

    @Test
    public void testSetQuestionMarkDoesNotUncoverTheCell() {
        Cell cell = new Cell();
        cell.setQuestionMark();
        assertTrue(cell.isCovered());
    }

    @Test
    public void testSetQuestionMarkCanBeDoneOnACellWithRedFlag() {
        Cell cell = new Cell();
        cell.setRedFlag();
        cell.setQuestionMark();
        assertTrue(cell.isCovered());
    }
}
