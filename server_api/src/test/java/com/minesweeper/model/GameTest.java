package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.minesweeper.service.GameInfo;

public class GameTest {

    @Test
    public void testGameIdIsSetWhenCreated() {
        Game game = new Game(5, 6, 10, "user");
        assertNotNull(game.getId());
    }

    @Test
    public void testUsernamesSetWhenCreated() {
        Game game = new Game(5, 6, 10, "user");
        assertEquals("user", game.getUsername());
    }

    @Test
    public void testGameIsStartedWhenCreated() {
        Game game = new Game(5, 6, 10, "user");
        assertEquals(Status.Started, game.getStatus());
    }

    @Test
    public void testGameStartTimeIsSetWhenCreated() {
        String fixedNow = "2020-08-27T22:00:00.00";
        Game game = new Game(5, 6, 10, "user") {
            protected Clock getClock() {
                return Clock.fixed(Instant.parse(fixedNow + "Z"), ZoneOffset.UTC);
            };
        };
        assertEquals(LocalDateTime.parse(fixedNow), game.getStartTime());
    }

    @Test
    public void testGameEndTimeIsNotSetWhenCreated() {
        String fixedNow = "2020-08-27T22:00:00.00";
        Game game = new Game(5, 6, 10, "user") {
            protected Clock getClock() {
                return Clock.fixed(Instant.parse(fixedNow + "Z"), ZoneOffset.UTC);
            };
        };
        assertNull(game.getEndTime());
    }

    @Test
    public void testGameBelongsToUser() {
        Game game = new Game(5, 6, 10, "user");
        assertTrue(game.belongsTo("user"));
    }

    @Test
    public void testGameDoesNotBelongToUser() {
        Game game = new Game(5, 6, 10, "user");
        assertFalse(game.belongsTo("anotheruser"));
    }

    @Test
    public void testUncoverCellOnFinishedGameThrowsException() {
        Game game = new Game(5, 6, 10, "user") {
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
        Game game = new Game(4, 4, 5, "user") {
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
        Game game = new Game(4, 4, 5, "user") {
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
        Game game = new Game(4, 4, 5, "user") {
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
        Game game = new Game(4, 4, 5, "user") {
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
    public void testGetDurationReturnsValueOnStartedGame() {
        Game game = new Game(4, 4, 5, "user");
        assertNotNull(game.getDuration());
    }

    @Test
    public void testSetRedFlagOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3, "user") {
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
    public void testRemoveRedFlagOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3, "user") {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };
        Exception exception = assertThrows(GameException.class, () -> {
            game.removeRedFlag(2, 0);
        });
        assertTrue(exception.getMessage().contains("action not allowed, the game is finished"));
    }

    @Test
    public void testSetQuestionMarkOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3, "user") {
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

    @Test
    public void testRemoveQuestionMarkOnFinishedGameThrowsException() {
        Game game = new Game(4, 4, 3, "user") {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };
        Exception exception = assertThrows(GameException.class, () -> {
            game.removeQuestionMark(2, 0);
        });
        assertTrue(exception.getMessage().contains("action not allowed, the game is finished"));
    }

    @Test
    public void testGetInfoOnNewGame() {
        Game game = new Game(3, 3, 1, "user");
        GameInfo gameInfo = game.toGameInfo();
        assertNotNull(gameInfo.id);
        assertEquals(Status.Started.name(), gameInfo.status);
        assertNotNull(gameInfo.duration);
        assertTrue(gameInfo.board.cells.stream().flatMap(aRow -> aRow.stream()).allMatch(value -> "C".equals(value)));
    }
}
