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
    public void testRemoveRedFlagOnCellWithoutFlagThrowsException() {
        Cell cell = new Cell();
        Exception exception = assertThrows(CellException.class, () -> {
            cell.removeRedFlag();
        });
        assertTrue(exception.getMessage().contains("cannot remove red flag"));
    }

    @Test
    public void testRemoveRedFlagLetsTheCellCovered() {
        Cell cell = new Cell();
        cell.setRedFlag();
        cell.removeRedFlag();
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

    @Test
    public void testRemoveQuestionMarkOnCellWithoutFlagThrowsException() {
        Cell cell = new Cell();
        Exception exception = assertThrows(CellException.class, () -> {
            cell.removeQuestionMark();
        });
        assertTrue(exception.getMessage().contains("cannot remove question mark"));
    }

    @Test
    public void testRemoveQuestionMarkLetsTheCellCovered() {
        Cell cell = new Cell();
        cell.setQuestionMark();
        cell.removeQuestionMark();
        assertTrue(cell.isCovered());
    }

    @Test
    public void testGetInfoOnCoveredCell() {
        Cell cell = new Cell();
        assertEquals("C", cell.getInfo());
    }

    @Test
    public void testGetInfoOnFlaggedCell() {
        Cell cell = new Cell();
        cell.setRedFlag();
        assertEquals("F", cell.getInfo());
    }

    @Test
    public void testGetInfoOnQuestionMarkedCell() {
        Cell cell = new Cell();
        cell.setQuestionMark();
        assertEquals("Q", cell.getInfo());
    }

    @Test
    public void testGetInfoOnUncoveredCellWithMine() {
        Cell cell = new Cell();
        cell.addMine();
        cell.uncover();
        assertEquals("M", cell.getInfo());
    }

    @Test
    public void testGetInfoOnCoveredCellWithMine() {
        Cell cell = new Cell();
        cell.addMine();
        assertEquals("C", cell.getInfo());
    }

    @Test
    public void testGetInfoOnUncoveredCellWithAdjacentMines() {
        Cell cell = new Cell();
        cell.incrementAdjacentMinesCounter();
        cell.incrementAdjacentMinesCounter();
        cell.uncover();
        assertEquals("2", cell.getInfo());
    }

}
