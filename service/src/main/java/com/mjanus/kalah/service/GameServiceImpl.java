package com.mjanus.kalah.service;

import com.mjanus.kalah.config.GameConfiguration;
import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameNotFoundException;
import com.mjanus.kalah.exception.WrongMoveException;
import com.mjanus.kalah.mapper.GameMapper;
import com.mjanus.kalah.model.Game;
import com.mjanus.kalah.model.Pit;
import com.mjanus.kalah.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mjanus.kalah.model.Player.PLAYER_FIRST;
import static com.mjanus.kalah.model.Player.PLAYER_SECOND;
import static com.mjanus.kalah.util.Constant.PIT_END_INDEX;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameMapper mapper;
    private final GameConfiguration gameConfig;

    @Override
    @Transactional
    public GameDto createGame() {
        Game game = new Game(gameConfig.getPitStones());
        repository.save(game);
        return mapper.toDto(game);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDto getGame(String gameId) {
        Game game = repository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Can not find game: " + gameId));
        return mapper.toFullDto(game);
    }

    //TODO this part of api could be catchable for performance efficient
    @Override
    @Transactional
    public GameDto play(String gameId, Integer pitId) {
        Game game = repository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Can not find game: " + gameId));

        validateMove(game, pitId);
        makeMove(game, pitId);

        repository.save(game);
        return mapper.toFullDto(game);
    }

    private void validateMove(final Game game, final Integer pitId) {
        final Pit chosenPit = game.getBoard().getPit(pitId);

        if(game.getWinner() != null) {
            throw new WrongMoveException("Game over");
        }
        if(chosenPit.isHouse()) {
            throw new WrongMoveException("Can not move pits from house");
        }
        if (chosenPit.getStoneCount() == 0) {
            throw new WrongMoveException("Can not make move when pit is empty");
        }
        if(game.getPlayerTurn() != chosenPit.pitOwner()) {
            throw new WrongMoveException("It is Player's " + game.getPlayerTurn().name() + "turn");
        }
    }

    private void makeMove(final Game game, Integer pitId) {
        final Pit chosenPit = game.getBoard().getPit(pitId);
        int stoneToDistribute = chosenPit.getStoneCount();
        chosenPit.setStoneCount(0);

        while (stoneToDistribute > 0) {
            final Pit currentPit = game.getBoard().getPit(++pitId);
            currentPit.addStones(1);
            stoneToDistribute--;
        }

        determinePlayerTurn(game, pitId);
        actionForLastEmptyPit(game, pitId);
        checkGameOver(game);
    }

    private void determinePlayerTurn(final Game game, final Integer lastPitId) {
        final Pit pit = game.getBoard().getPit(lastPitId);

        if(pit.isHouse() && pit.pitOwner().equals(game.getPlayerTurn())) {
            game.setPlayerTurn(game.getPlayerTurn());
        } else {
            if (PLAYER_FIRST.equals(game.getPlayerTurn())) {
                game.setPlayerTurn(PLAYER_SECOND);
            } else {
                game.setPlayerTurn(PLAYER_FIRST);
            }
        }
    }

    private void actionForLastEmptyPit(final Game game, final Integer lastPitId) {
        final Pit endPit = game.getBoard().getPit(lastPitId);
        if (!endPit.isHouse() && endPit.pitOwner().equals(game.getPlayerTurn()) && (endPit.getStoneCount() == 1)) {

            final Pit opponentPit = game.getBoard().getPit(PIT_END_INDEX - endPit.getIndex());

            if (opponentPit.getStoneCount() > 0) {
                final Pit house = game.getBoard().getPit(game.getPlayerTurn().getHouseIndex());

                house.addStones(endPit.getIndex() + opponentPit.getStoneCount());
                opponentPit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void checkGameOver(final Game game) {
        final int firstPlayerStonesCount = game.getBoard().calculateStonesForPlayer(PLAYER_FIRST, false);
        final int secondPlayerStonesCount = game.getBoard().calculateStonesForPlayer(PLAYER_SECOND, false);
        if ((firstPlayerStonesCount == 0) || (secondPlayerStonesCount == 0)) {
            final Pit houseFirstPlayer = game.getBoard().getPit(PLAYER_FIRST.getHouseIndex());
            final Pit houseSecondPlayer = game.getBoard().getPit(PLAYER_SECOND.getHouseIndex());
            houseFirstPlayer.addStones(firstPlayerStonesCount);
            houseSecondPlayer.addStones(secondPlayerStonesCount);

            determineWinner(game, houseFirstPlayer, houseSecondPlayer);
        }
    }

    private void determineWinner(final Game game, final Pit houseFirstPlayer, final Pit houseSecondPlayer) {
        if (houseFirstPlayer.getStoneCount() > houseSecondPlayer.getStoneCount()) {
            game.setWinner(PLAYER_FIRST);
        } else {
            game.setWinner(PLAYER_SECOND);
        }
    }
}
