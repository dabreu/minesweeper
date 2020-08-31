package com.minesweeper.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.minesweeper.model.User;

/**
 * Repository to handle persistence of users
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds the username having the given active token
     * 
     * @param token
     * @return
     */
    @Query("{'activeToken' : ?0}")
    public Optional<User> findUsernameByActiveToken(@Param("token") String token);
}
