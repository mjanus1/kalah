package com.mjanus.kalah.repository;

import com.mjanus.kalah.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
}
