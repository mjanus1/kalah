package com.mjanus.kalah.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor
public class Board {
    private static final int PIT_START_INDEX = 1;
    private static final int PIT_END_INDEX = 14;
    private static final int[] HOUSE_INDEXES = {7, 14};

    private List<Pit> pits;

    public Board(int pitStones) {
        initBoard(pitStones);
    }

    private void initBoard(int pitStones) {
        this.pits = new ArrayList<>();
        for (int index = Board.PIT_START_INDEX; index <= Board.PIT_END_INDEX; index++) {
            if(isHouse(index)) {
                pits.add(new House(index));
            } else {
                pits.add(new Pit(index, pitStones));
            }
        }
    }

    public boolean isHouse(int index) {
        return IntStream.of(HOUSE_INDEXES).anyMatch(i -> i == index);
    }
}
