package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CellPositionTest {

    @Test
    public void testEqualsReturnsFalseIfPositionHasDifferentCoordinates() {
        assertFalse(new CellPosition(1, 1).equals(new CellPosition(1, 2)));
    }

    @Test
    public void testEqualsReturnsTrueIfPositionHasSameCoordinates() {
        assertTrue(new CellPosition(2, 4).equals(new CellPosition(2, 4)));
    }
}
