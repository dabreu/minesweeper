package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.minesweeper.model.Game.Status;

public class GameTest {

    @Test
    public void testGameIsStartedWhenCreated() {
        Game game = new Game(5, 6, 10);
        assertEquals(Status.Started, game.getStatus());
    }

    @Test
    public void testGameStartTimeIsSetWhenCreated() {
        String fixedNow = "2020-08-27T22:00:00.00";
        Game game = new Game(5, 6, 10) {
            protected Clock getClock() {
                return Clock.fixed(Instant.parse(fixedNow + "Z"), ZoneOffset.UTC);
            };
        };
        assertEquals(LocalDateTime.parse(fixedNow), game.getStartTime());
    }

    @Test
    public void testUncoverCellOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3) {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };
        Exception exception = assertThrows(GameException.class, () -> {
            game.uncoverCell(2, 0);
        });
        assertTrue(exception.getMessage().contains("action not allowed, the game is finished"));
    }

    @Test
    public void testUncoverCellChangesGameStatusToLostIfBoardFinishesWithMineUncovered() {
        Board board = mock(Board.class);
        when(board.uncover(2, 0)).thenReturn(board);
        when(board.isFinished()).thenReturn(true);
        when(board.areAllCellsWithoutMineUncovered()).thenReturn(false);
        Game game = new Game(4, 4, 5) {
            @Override
            protected Board getBoard() {
                return board;
            }
        };
        game.uncoverCell(2, 0);
        assertTrue(game.isFinished());
        assertEquals(Status.Lost, game.getStatus());
    }

    @Test
    public void testUncoverCellChangesGameStatusToWonIfBoardFinishesWithAllCellsWithoutMineUncovered() {
        Board board = mock(Board.class);
        when(board.uncover(2, 0)).thenReturn(board);
        when(board.isFinished()).thenReturn(true);
        when(board.areAllCellsWithoutMineUncovered()).thenReturn(true);
        Game game = new Game(4, 4, 5) {
            @Override
            protected Board getBoard() {
                return board;
            }
        };
        game.uncoverCell(2, 0);
        assertTrue(game.isFinished());
        assertEquals(Status.Won, game.getStatus());
    }

    @Test
    public void testGameEndTimeIsCalculatedWhenUncoverCellAndGameIsLost() throws Exception {
        Board board = mock(Board.class);
        when(board.uncover(2, 0)).thenReturn(board);
        when(board.isFinished()).thenReturn(true);
        when(board.areAllCellsWithoutMineUncovered()).thenReturn(false);
        Game game = new Game(4, 4, 5) {
            @Override
            protected Board getBoard() {
                return board;
            }
        };
        Thread.sleep(1);
        game.uncoverCell(2, 0);
        assertTrue(game.getEndTime().isAfter(game.getStartTime()));
    }

    @Test
    public void testGameEndTimeIsCalculatedWhenUncoverCellAndGameIsWon() throws Exception {
        Board board = mock(Board.class);
        when(board.uncover(2, 0)).thenReturn(board);
        when(board.isFinished()).thenReturn(true);
        when(board.areAllCellsWithoutMineUncovered()).thenReturn(true);
        Game game = new Game(4, 4, 5) {
            @Override
            protected Board getBoard() {
                return board;
            }
        };
        Thread.sleep(1);
        game.uncoverCell(2, 0);
        assertTrue(game.getEndTime().isAfter(game.getStartTime()));
    }

    @Test
    public void testSetRedFlagOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3) {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };
        Exception exception = assertThrows(GameException.class, () -> {
            game.setRedFlag(2, 0);
        });
        assertTrue(exception.getMessage().contains("action not allowed, the game is finished"));
    }

    @Test
    public void testSetQuestionMarkOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3) {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };
        Exception exception = assertThrows(GameException.class, () -> {
            game.setQuestionMark(2, 0);
        });
        assertTrue(exception.getMessage().contains("action not allowed, the game is finished"));
    }
}
