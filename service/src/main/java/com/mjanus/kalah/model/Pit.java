package com.mjanus.kalah.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.IntStream;

import static com.mjanus.kalah.model.Player.PLAYER_FIRST;
import static com.mjanus.kalah.model.Player.PLAYER_SECOND;
import static com.mjanus.kalah.util.Constant.HOUSE_INDEXES;
import static com.mjanus.kalah.util.Constant.PLAYER_FIRST_HOUSE_ID;

@Getter
@AllArgsConstructor
public class Pit {
    private final Integer index;
    private Integer stoneCount;

    public void addStones (Integer stones){
        this.stoneCount += stones;
    }

    public boolean isHouse() {
        return IntStream.of(HOUSE_INDEXES).anyMatch(i -> i == index);
    }

    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public Player pitOwner() {
        return index <= PLAYER_FIRST_HOUSE_ID ? PLAYER_FIRST : PLAYER_SECOND;
    }
}
