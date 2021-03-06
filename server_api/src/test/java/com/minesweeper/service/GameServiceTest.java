package com.minesweeper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.minesweeper.model.Game;
import com.minesweeper.model.Game.Status;
import com.minesweeper.repository.GameRepository;
import com.minesweeper.security.SecurityContext;
import com.minesweeper.security.UnauthorizedException;

public class GameServiceTest {

    private GameRepository repository;
    private GameService service;

    @BeforeEach
    public void before() {
        repository = mock(GameRepository.class);
        service = new GameService(repository);
        SecurityContext.setPrincipal("user");
    }

    @Test
    public void testCreateNewGameUsesDefaultValuesIfParametersAreNotIndicated() {
        GameInfo gameInfo = service.createGame(null, null, null);
        assertEquals(GameService.DEFAULT_ROWS, gameInfo.board.cells.size());
        gameInfo.board.cells.forEach(row -> assertEquals(GameService.DEFAULT_COLUMNS, row.size()));
    }

    @Test
    public void testCreateNewGameSavesChanges() {
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        GameInfo gameInfo = service.createGame(null, null, null);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }

    @Test
    public void testCreateNewGameAssociatesThePrincipalToTheGame() {
        SecurityContext.setPrincipal("testprincipal");
        GameInfo gameInfo = service.createGame(10, 10, 8);
        assertEquals("testprincipal", gameInfo.username);
    }

    @Test
    public void testGetGameThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.getGame(UUID.randomUUID());
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testGetGameThrowsExceptionIfNotAuthorizedUser() {
        SecurityContext.setPrincipal("anotheruser");
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            service.getGame(game.getId());
        });
        assertTrue(exception.getMessage().contains("Game does not belong to user"));
    }

    @Test
    public void testGetGameReturnsTheGameInfo() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        GameInfo gameInfo = service.getGame(game.getId());
        assertEquals(game.getId(), gameInfo.id);
        assertEquals(Status.Started.name(), gameInfo.status);
        assertEquals(10, gameInfo.board.cells.size());
    }

    @Test
    public void testUncoverThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.uncoverCell(UUID.randomUUID(), 2, 2);
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testUncoverSavesChanges() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        GameInfo gameInfo = service.uncoverCell(game.getId(), 2, 2);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }

    @Test
    public void testSetRedFlagThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.setRedFlag(UUID.randomUUID(), 2, 2);
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testSetRedFlagSavesChanges() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        GameInfo gameInfo = service.setRedFlag(game.getId(), 2, 2);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }

    @Test
    public void testRemoveRedFlagThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.removeRedFlag(UUID.randomUUID(), 2, 2);
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testRemoveRedFlagSavesChanges() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        service.setRedFlag(game.getId(), 2, 2);
        GameInfo gameInfo = service.removeRedFlag(game.getId(), 2, 2);
        verify(repository, times(2)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }

    @Test
    public void testSetQuestionMarkThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.setQuestionMark(UUID.randomUUID(), 2, 2);
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testSetQuestionMarkSavesChanges() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        GameInfo gameInfo = service.setQuestionMark(game.getId(), 2, 2);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }

    @Test
    public void testRemoveQuestionMarkThrowsExceptionIfNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            service.removeQuestionMark(UUID.randomUUID(), 2, 2);
        });
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testRemoveQuestionMarkSavesChanges() {
        Game game = new Game(10, 10, 8, "user");
        when(repository.findById(game.getId())).thenReturn(Optional.of(game));
        ArgumentCaptor<Game> argCaptor = ArgumentCaptor.forClass(Game.class);
        service.setQuestionMark(game.getId(), 2, 2);
        GameInfo gameInfo = service.removeQuestionMark(game.getId(), 2, 2);
        verify(repository, times(2)).save(argCaptor.capture());
        assertEquals(gameInfo.id, argCaptor.getValue().getId());
    }
}
