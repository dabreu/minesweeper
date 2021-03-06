package com.minesweeper.service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.minesweeper.model.Game;
import com.minesweeper.repository.GameRepository;
import com.minesweeper.security.SecurityContext;
import com.minesweeper.security.UnauthorizedException;

/**
 * Implementation of the Game Service
 */
@Service
public class GameService implements IGameService {

    public static int DEFAULT_ROWS = 10;
    public static int DEFAULT_COLUMNS = 10;
    public static int DEFAULT_MINES = 10;

    private GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public GameInfo createGame(Integer rows, Integer columns, Integer mines) {
        String username = SecurityContext.getPrincipal();
        Game game = new Game(getParameter(rows, DEFAULT_ROWS), getParameter(columns, DEFAULT_COLUMNS), getParameter(mines, DEFAULT_MINES), username);
        save(game);
        return game.toGameInfo();
    }

    @Override
    public GameInfo getGame(UUID id) {
        return get(id).toGameInfo();
    }

    @Override
    public GameInfo uncoverCell(UUID id, Integer row, Integer column) {
        return executeAndSave(id, row, column, game -> game.uncoverCell(row, column));
    }

    @Override
    public GameInfo setRedFlag(UUID id, Integer row, Integer column) {
        return executeAndSave(id, row, column, game -> game.setRedFlag(row, column));
    }

    @Override
    public GameInfo removeRedFlag(UUID id, Integer row, Integer column) {
        return executeAndSave(id, row, column, game -> game.removeRedFlag(row, column));
    }

    @Override
    public GameInfo setQuestionMark(UUID id, Integer row, Integer column) {
        return executeAndSave(id, row, column, game -> game.setQuestionMark(row, column));
    }

    @Override
    public GameInfo removeQuestionMark(UUID id, Integer row, Integer column) {
        return executeAndSave(id, row, column, game -> game.removeQuestionMark(row, column));
    }

    private Integer getParameter(Integer value, int defaultValue) {
        return (value != null ? value : defaultValue);
    }

    private void save(Game game) {
        repository.save(game);
    }

    private Game get(UUID id) {
        Optional<Game> game = repository.findById(id);
        if (game.isPresent()) {
            return getGameIfBelongsToPrincipal(game.get());
        }
        throw new GameNotFoundException("Game not found: " + id);
    }

    private GameInfo executeAndSave(UUID id, Integer row, Integer column, Consumer<Game> action) {
        Game game = get(id);
        action.accept(game);
        save(game);
        return game.toGameInfo();
    }

    private Game getGameIfBelongsToPrincipal(Game game) {
        if (game.belongsTo(SecurityContext.getPrincipal())) {
            return game;
        }
        throw new UnauthorizedException("Game does not belong to user");
    }
}
