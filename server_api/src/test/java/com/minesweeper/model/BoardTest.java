package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class BoardTest {

	@Test
	public void testBoardCreationWithNegativeRowsThrowsException() {
		Exception exception = assertThrows(BoardException.class, () -> {
			new Board(-1, 10, 5);
		});
		assertTrue(exception.getMessage().contains("invalid rows"));
	}

	@Test
	public void testBoardCreationWithNegativeColumnsThrowsException() {
		Exception exception = assertThrows(BoardException.class, () -> {
			new Board(10, -2, 5);
		});
		assertTrue(exception.getMessage().contains("invalid columns"));
	}

	@Test
	public void testBoardCreationWithNegativeMinesThrowsException() {
		Exception exception = assertThrows(BoardException.class, () -> {
			new Board(10, 9, -2);
		});
		assertTrue(exception.getMessage().contains("invalid mines"));
	}

	@Test
	public void testBoardCreationWithZeroMinesThrowsException() {
		Exception exception = assertThrows(BoardException.class, () -> {
			new Board(10, 9, 0);
		});
		assertTrue(exception.getMessage().contains("invalid mines"));
	}

	@Test
	public void testBoardCreationWithMinesGreatherOrEqualThanBoardPositionsThrowsException() {
		Exception exception = assertThrows(BoardException.class, () -> {
			new Board(10, 10, 100);
		});
		assertTrue(exception.getMessage().contains("invalid mines"));

		exception = assertThrows(BoardException.class, () -> {
			new Board(10, 10, 101);
		});
		assertTrue(exception.getMessage().contains("invalid mines"));
	}

	@Test
	public void testBoardIsNotFinishedWhenCreated() {
		Board board = new Board(6, 6, 10);
		assertFalse(board.isFinished());
	}

	@Test
	public void testBoardHasNotAllCellsUncoveredWhenCreated() {
		Board board = new Board(6, 6, 10);
		assertFalse(board.areAllCellsWithoutMineUncovered());
	}

	@Test
	public void testBoardCreationScattersMines() {
		int rows = 5;
		int columns = 5;
		int mines = 4;
		Board board = new Board(rows, columns, mines);
		List<Cell> cells = new ArrayList<Cell>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells.add(board.getCell(i, j));
			}
		}
		assertEquals(mines, countCellsWithMine(cells.stream()));
		assertEquals(21, cells.stream().filter(cell -> !cell.hasMine()).count());
	}

	@Test
	public void testBoardCreationCalculatesMinesAdjacency() {
		int rows = 4;
		int columns = 4;
		int mines = 3;
		Board board = new Board(rows, columns, mines);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				CellPosition position = new CellPosition(i, j);
				Cell cell = board.getCell(position);
				if (cell.hasMine()) {
					assertEquals(0, cell.getAdjacentMinesCounter());
				} else {
					assertEquals(
							countCellsWithMine(
									board.getAdjacents(position).stream().map(adjacent -> board.getCell(adjacent))),
							cell.getAdjacentMinesCounter());
				}
			}
		}
	}

	@Test
	public void testUncoverCellOnFinishedBoardThrowsException() {
		Board board = new Board(4, 4, 3) {
			@Override
			public boolean isFinished() {
				return true;
			}
		};
		Exception exception = assertThrows(BoardException.class, () -> {
			board.uncover(2, 0);
		});
		assertTrue(exception.getMessage().contains("action not allowed, the board is finished"));
	}

	@Test
	public void testUncoverCellWithInvalidRowPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.uncover(50, 5);
		});
		assertTrue(exception.getMessage().contains("invalid row"));
	}

	@Test
	public void testUncoverCellWithInvalidColumnPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.uncover(3, 50);
		});
		assertTrue(exception.getMessage().contains("invalid column"));
	}

	@Test
	public void testUncoverCellWithMineFinishesTheBoard() {
		Board board = getBoardWithFixedMines();
		board.uncover(2, 0);
		assertTrue(board.isFinished());
		assertFalse(board.areAllCellsWithoutMineUncovered());
	}

	@Test
	public void testUncoverExpectedCellsWithoutMineFinishesTheBoard() {
		Board board = getBoardWithFixedMines();
		board.uncover(0, 1);
		board.uncover(0, 2);
		board.uncover(0, 3);
		board.uncover(1, 0);
		board.uncover(1, 3);
		board.uncover(3, 0);
		assertTrue(board.isFinished());
		assertTrue(board.areAllCellsWithoutMineUncovered());
	}

	@Test
	public void testSetRedFlagOnFinishedBoardThrowsException() {
		Board board = new Board(4, 4, 3) {
			@Override
			public boolean isFinished() {
				return true;
			}
		};
		Exception exception = assertThrows(BoardException.class, () -> {
			board.setRedFlag(2, 0);
		});
		assertTrue(exception.getMessage().contains("action not allowed, the board is finished"));
	}

	@Test
	public void testSetRedFlagWithInvalidRowPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.setRedFlag(100, 0);
		});
		assertTrue(exception.getMessage().contains("invalid row"));
	}

	@Test
	public void testSetRedFlagWithInvalidColumnPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.setRedFlag(3, 50);
		});
		assertTrue(exception.getMessage().contains("invalid column"));
	}

	@Test
	public void testSetQuestionMarkOnFinishedBoardThrowsException() {
		Board board = new Board(4, 4, 3) {
			@Override
			public boolean isFinished() {
				return true;
			}
		};
		Exception exception = assertThrows(BoardException.class, () -> {
			board.setQuestionMark(2, 0);
		});
		assertTrue(exception.getMessage().contains("action not allowed, the board is finished"));
	}

	@Test
	public void testSetQuestionMarkWithInvalidRowPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.setQuestionMark(100, 0);
		});
		assertTrue(exception.getMessage().contains("invalid row"));
	}

	@Test
	public void testSetQuestionMarkWithInvalidColumnPositionThrowsException() {
		Board board = getBoardWithFixedMines();
		Exception exception = assertThrows(CellPositionException.class, () -> {
			board.setQuestionMark(3, 50);
		});
		assertTrue(exception.getMessage().contains("invalid column"));
	}

	private Board getBoardWithFixedMines() {
		// The mines will be placed in fixed positions in order to make tests
		// deterministic. With the seed used on the random generator the fixed positions
		// will be: {(2,0), (1,1), (0,0)}
		Random random = new Random(1);
		Board board = new Board(4, 4, 3) {
			@Override
			protected int getRandom(int value) {
				return random.nextInt(value);
			}
		};
		return board;
	}

	private long countCellsWithMine(Stream<Cell> cells) {
		return cells.filter(cell -> cell.hasMine()).count();
	}
}
