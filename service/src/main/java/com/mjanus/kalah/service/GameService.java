package com.mjanus.kalah.service;

import com.mjanus.kalah.dto.GameDto;

public interface GameService {
    /**
     * Creates new Kalah game.
     *
     * @return new game
     */
    GameDto createGame();


    /**
     * Makes a move of player.
     *
     * @param gameId unique identifier of a game
     * @return current state of game
     * @throws com.mjanus.kalah.exception.GameNotFoundException
     *         if the gameId does not exist in database
     *
     */
    GameDto getGame(String gameId);

    /**
     * Makes a move of player.
     *
     * @param gameId unique identifier of a game
     * @param pitId  id of the pit selected to make a move
     * @return modified game
     * @throws com.mjanus.kalah.exception.GameNotFoundException
     *         if the gameId does not exist in database
     * @throws com.mjanus.kalah.exception.WrongMoveException
     *         if player use wrong pit or use home in game
     *
     */
    GameDto play(String gameId, int pitId);
}
