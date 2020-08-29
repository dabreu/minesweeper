package com.minesweeper.repository;

import java.util.Optional;
import java.util.UUID;

import com.minesweeper.model.Game;

/**
 * Interface for the repository of games
 */
public interface IGameRepository {

    public void save(Game game);

    public Optional<Game> get(UUID id);
}
