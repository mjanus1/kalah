package com.mjanus.kalah.model;

import lombok.Getter;

@Getter
public class House extends Pit {
    private static final int INIT_STONE_COUNT_AT_HOME = 0;

    public House(final Integer index) {
        super(index, INIT_STONE_COUNT_AT_HOME);
    }
}
