package com.mjanus.kalah.service;

import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GameServiceTest {

    @Autowired
    private GameService service;

    @Test
    public void crateGame_shouldCreateNewGame() {
        final GameDto game = this.service.createGame();
        Assertions.assertNotNull(game);
    }

    @Test
    public void getGame_shouldReadGameFromDatabase() {
        //given
        final GameDto game = this.service.createGame();

        //when
        final GameDto savedGame = this.service.getGame(game.getId());
        System.out.println(savedGame.getStatus());

        //then
        Assertions.assertNotNull(game);
        Assertions.assertNotNull(savedGame);
        Assertions.assertEquals(game.getId(), savedGame.getId());
        Assertions.assertEquals(savedGame.getStatus().toString(), "{1=6, 2=6, 3=6, 4=6, 5=6, 6=6, 7=0, 8=6, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}");
    }

    @Test
    public void getGame_shouldThrowExceptionWhenGameDoesNotExist() {
        Exception exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
            //given
            final GameDto game = this.service.createGame();

            //when
            final GameDto savedGame = this.service.getGame("wrongGameId");
        });
        Assertions.assertEquals(exception.getMessage(), "Can not find game: wrongGameId");
    }
}
