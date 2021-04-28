package com.mjanus.kalah.model;

public enum Player {
    PLAYER_FIRST(7),
    PLAYER_SECOND(14);

    private final Integer houseIndex;

    Player(final Integer houseIndex) {
        this.houseIndex = houseIndex;
    }

    public Integer getHouseIndex() {
        return houseIndex;
    }
}
