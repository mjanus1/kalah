package com.mjanus.kalah.mapper;

import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.model.Board;
import com.mjanus.kalah.model.Game;
import com.mjanus.kalah.model.Pit;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GameMapper {

    public static final String SERVER_PORT = "server.port";
    public static final String URI_TEMPLATE = "http://%s:%s/games/%s";
    private final Environment environment;

    public GameDto toDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .uri(getUrl(game.getId()))
                .build();
    }

    public GameDto toFullDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .uri(getUrl(game.getId()))
                .status(mapStatus(game.getBoard()))
                .playerTurn(game.getPlayerTurn())
                .build();
    }

    private Map<Integer, Integer> mapStatus(Board board) {
        return board.getPits().stream().collect(Collectors.toMap(Pit::getIndex, Pit::getStoneCount));
    }

    private String getUrl(final String gameId) {
        final int port = environment.getProperty(SERVER_PORT, Integer.class, 8080);
        return String.format(URI_TEMPLATE, InetAddress.getLoopbackAddress().getHostName(), port, gameId);
    }
}
