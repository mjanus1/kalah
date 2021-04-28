package com.mjanus.kalah.service;

import com.mjanus.kalah.config.GameConfiguration;
import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameNotFoundException;
import com.mjanus.kalah.mapper.GameMapper;
import com.mjanus.kalah.model.Game;
import com.mjanus.kalah.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameMapper mapper;
    private final GameConfiguration gameConfig;

    @Override
    @Transactional
    public GameDto createGame() {
        Game game = new Game(gameConfig.getPitStones());
        repository.save(game);
        return mapper.toDto(game);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDto getGame(String gameId) {
        Game game = repository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Can not find game: " + gameId));
        return mapper.toFullDto(game);
    }

    @Override
    @Transactional
    public GameDto play(String gameId, Integer pitId) {
        Game game = repository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Can not find game: " + gameId));

        //TODO Move logic
        return mapper.toFullDto(game);
    }
}
