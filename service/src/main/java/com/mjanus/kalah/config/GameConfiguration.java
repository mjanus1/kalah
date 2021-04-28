package com.mjanus.kalah.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GameConfiguration {

    @Value("${kalah.pit.stones:6}")
    private int pitStones;
}
