package com.mjanus.kalah.service;

import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameNotFoundException;
import com.mjanus.kalah.exception.WrongMoveException;
import com.mjanus.kalah.model.*;
import com.mjanus.kalah.repository.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static com.mjanus.kalah.model.Player.PLAYER_FIRST;
import static com.mjanus.kalah.model.Player.PLAYER_SECOND;

@SpringBootTest
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GameServiceTest {

    @Autowired
    private GameRepository repository;

    @Autowired
    private GameService service;

    @Test
    public void crateGame_shouldCreateNewGame() {
        final GameDto game = this.service.createGame();
        Assertions.assertNotNull(game);
    }

    @Test
    public void getGame_shouldReadInitGameFromDatabase() {
        //given
        final GameDto game = this.service.createGame();

        //when
        final GameDto savedGame = this.service.getGame(game.getId());
        System.out.println(savedGame.getStatus());

        //then
        Assertions.assertNotNull(game);
        Assertions.assertNotNull(savedGame);
        Assertions.assertEquals(game.getId(), savedGame.getId());
        Assertions.assertEquals("{1=6, 2=6, 3=6, 4=6, 5=6, 6=6, 7=0, 8=6, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}", savedGame.getStatus().toString());
    }

    @Test
    public void getGame_shouldThrowExceptionWhenGameDoesNotExist() {
        this.service.createGame();
        Exception exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
            //when
            this.service.getGame("wrongGameId");
        });
        Assertions.assertEquals("Can not find game: wrongGameId", exception.getMessage());
    }

    @Test
    public void play_shouldMoveStonesTurnPlayerFirstStillPlayerFirst() {
        //given
        final GameDto game = this.service.createGame();

        //when
        final GameDto savedGame = this.service.play(game.getId(), 1);

        //then
        Assertions.assertEquals("{1=0, 2=7, 3=7, 4=7, 5=7, 6=7, 7=1, 8=6, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}", savedGame.getStatus().toString() );
        Assertions.assertEquals(PLAYER_FIRST, savedGame.getPlayerTurn());
    }

    @Test
    public void play_shouldMoveStonesTurnPlayerFirstNextMoveForSecondPlayer() {
        //given
        final GameDto game = this.service.createGame();

        //when
        final GameDto savedGame = this.service.play(game.getId(), 2);

        //then
        Assertions.assertEquals("{1=6, 2=0, 3=7, 4=7, 5=7, 6=7, 7=1, 8=7, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}", savedGame.getStatus().toString());
        Assertions.assertEquals(PLAYER_SECOND, savedGame.getPlayerTurn());
    }

    @Test
    public void play_shouldMoveStonesShouldTakeOpponentStones() {
        //given
        List<Pit> pits = Arrays.asList(
                new Pit(1, 1),
                new Pit(2, 0),
                new Pit(3, 7),
                new Pit(4, 6),
                new Pit(5, 6),
                new Pit(6, 6),
                new House(7),
                new Pit(8, 6),
                new Pit(9, 6),
                new Pit(10, 6),
                new Pit(11, 6),
                new Pit(12, 15),
                new Pit(13, 6),
                new House(14));

        Board board = new Board();
        board.setPits(pits);

        Game game = new Game();
        game.setPlayerTurn(PLAYER_FIRST);
        game.setBoard(board);

        repository.save(game);

        //when
        final GameDto savedGame = this.service.play(game.getId(), 1);

        //then
        Assertions.assertEquals("{1=0, 2=0, 3=7, 4=6, 5=6, 6=6, 7=16, 8=6, 9=6, 10=6, 11=6, 12=0, 13=6, 14=0}", savedGame.getStatus().toString());
        Assertions.assertEquals(PLAYER_SECOND, savedGame.getPlayerTurn());
    }

    @Test
    public void play_shouldGameOverWhenPitsAreEmpty() {
        //given
        List<Pit> pits = Arrays.asList(
                new Pit(1, 0),
                new Pit(2, 0),
                new Pit(3, 0),
                new Pit(4, 0),
                new Pit(5, 0),
                new Pit(6, 1),
                new Pit(7, 45),
                new Pit(8, 0),
                new Pit(9, 0),
                new Pit(10, 0),
                new Pit(11, 0),
                new Pit(12, 0),
                new Pit(13, 2),
                new Pit(14, 24));

        Board board = new Board();
        board.setPits(pits);

        Game game = new Game();
        game.setPlayerTurn(PLAYER_FIRST);
        game.setBoard(board);

        repository.save(game);

        //when
        final GameDto savedGame = this.service.play(game.getId(), 6);

        //then
        Assertions.assertEquals("{1=0, 2=0, 3=0, 4=0, 5=0, 6=0, 7=46, 8=0, 9=0, 10=0, 11=0, 12=0, 13=0, 14=26}", savedGame.getStatus().toString());
    }

    @Test
    public void play_shouldThrowExceptionWhenGameOver() {
        Game game = new Game(6);
        game.setPlayerTurn(PLAYER_FIRST);
        game.setWinner(PLAYER_SECOND);
        repository.save(game);
        String gameId = game.getId();

        Exception exception = Assertions.assertThrows(WrongMoveException.class, () -> {
            this.service.play(gameId,1);
        });
        Assertions.assertEquals("Game over", exception.getMessage());
    }

    @Test
    public void play_shouldThrowExceptionWhenWrongPitRange() {
        Game game = new Game(6);
        repository.save(game);
        String gameId = game.getId();

        Exception exception = Assertions.assertThrows(WrongMoveException.class, () -> {
            this.service.play(gameId,9999);
        });
        Assertions.assertEquals("Wrong pitId", exception.getMessage());
    }

    @Test
    public void play_shouldThrowExceptionWhenPitIdEqualsHouseId() {
        Game game = new Game(6);
        repository.save(game);
        String gameId = game.getId();

        Exception exception = Assertions.assertThrows(WrongMoveException.class, () -> {
            this.service.play(gameId,7);
        });
        Assertions.assertEquals("Can not move pits from house", exception.getMessage());
    }

    @Test
    public void play_shouldThrowExceptionWhenMoveOnPitWhereAreNotStones() {
        Game game = new Game(6);
        repository.save(game);
        String gameId = game.getId();
        this.service.play(gameId,1);

        Exception exception = Assertions.assertThrows(WrongMoveException.class, () -> {
            this.service.play(gameId,1);
        });
        Assertions.assertEquals("Can not make move when pit is empty", exception.getMessage());
    }

    @Test
    public void play_shouldThrowExceptionWhenWrongPlayerTurnMove() {
        Game game = new Game(6);
        repository.save(game);
        String gameId = game.getId();

        Assertions.assertEquals(PLAYER_FIRST, game.getPlayerTurn());

        Exception exception = Assertions.assertThrows(WrongMoveException.class, () -> {
            this.service.play(gameId,9);
        });
        Assertions.assertEquals("It is Player PLAYER_FIRST turn", exception.getMessage());
    }
}
