package com.mjanus.kalah.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.mjanus.kalah.util.Constant.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Board {
    private List<Pit> pits;

    public Board(int pitStones) {
        initBoard(pitStones);
    }

    public Pit getPit(final int index) {
        return this.pits.get((index - 1) % PIT_END_INDEX);
    }

    public int calculateStonesForPlayer(Player player, boolean includeHouse) {
        return pits.stream()
                .filter(p -> p.pitOwner().equals(player) && (includeHouse || !p.isHouse()))
                .map(Pit::getStoneCount)
                .reduce(0, Integer::sum);
    }

    public void cleanPits() {
        pits.forEach(pit -> {
            if(!pit.isHouse()) {
                pit.setStoneCount(EMPTY_STONES);
            }
        });
    }

    private void initBoard(int pitStones) {
        this.pits = new ArrayList<>();
        for (int index = PIT_START_INDEX; index <= PIT_END_INDEX; index++) {
            if(isHouse(index)) {
                pits.add(new House(index));
            } else {
                pits.add(new Pit(index, pitStones));
            }
        }
    }

    private boolean isHouse(int index) {
        return IntStream.of(HOUSE_INDEXES).anyMatch(i -> i == index);
    }
}
