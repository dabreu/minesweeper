package com.minesweeper.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.minesweeper.model.Game;

@Repository
public class MemoryRepository implements IGameRepository {

    private Map<UUID, Game> games = new HashMap<UUID, Game>();

    @Override
    public void save(Game game) {
        games.put(game.getId(), game);
    }

    @Override
    public Optional<Game> get(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

}
