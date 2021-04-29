package com.mjanus.kalah.repository;

import com.mjanus.kalah.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static com.mjanus.kalah.model.Player.PLAYER_SECOND;

@DataMongoTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository repository;

    @Test
    public void save_shouldSaveNewGame() {
        //given
        Game game = new Game(4);
        System.out.println(game);
        System.out.println(game.getBoard());
        //when
        repository.save(game);

        //then
        Assertions.assertNotNull(game.getId());
    }

    @Test
    public void save_shouldUpdateGame() {
        //given
        Game game = new Game(6);
        repository.save(game);
        Assertions.assertNull(game.getWinner());

        //when
        game.setWinner(PLAYER_SECOND);
        repository.save(game);

        //then
        Assertions.assertEquals(PLAYER_SECOND, game.getWinner());
    }

    @Test
    public void save_shouldGetGameById() {
        //given
        Game game = new Game(6);
        repository.save(game);
        Assertions.assertNull(game.getWinner());

        //when
        Optional<Game> saved = repository.findById(game.getId());

        //then
        Assertions.assertTrue(saved.isPresent());
        Assertions.assertEquals(saved.get().getId(), game.getId());
    }
}
