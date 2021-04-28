package com.mjanus.kalah.repository;

import com.mjanus.kalah.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {
}
