package com.mjanus.kalah.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pit {
    private final Integer index;
    private Integer stoneCount;

    public void addStones (Integer stones){
        this.stoneCount += stones;
    }
}
