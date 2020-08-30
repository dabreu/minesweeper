package com.minesweeper.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minesweeper.model.Game;

public interface GameRepository extends MongoRepository<Game, UUID> {

}
