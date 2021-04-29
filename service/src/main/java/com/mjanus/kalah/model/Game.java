package com.mjanus.kalah.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
@ToString
public class Game {
    @EqualsAndHashCode.Include
    private String id;
    private Board board;
    private Player winner;
    private Player playerTurn;

    public Game(int pitStones) {
        this.board = new Board(pitStones);
        playerTurn = Player.PLAYER_FIRST;
    }
}
