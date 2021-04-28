package com.mjanus.kalah.controller;

import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/games")
@Api(value = "Game Controller")
public class GameController {

    private final GameService service;

    @PostMapping
    @ApiOperation(value="Create new Kalah game")
    public ResponseEntity<GameDto> createNewGame() {
        final GameDto dto = this.service.createGame();
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{gameId}")
    @ApiOperation(value="Get game by Id")
    public ResponseEntity<GameDto> getGameById(@PathVariable String gameId) {
        final GameDto dto = this.service.getGame(gameId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping("/{gameId}/pits/{pitId}")
    @ApiOperation(value="Make a move")
    public ResponseEntity<GameDto> makeMove(@PathVariable String gameId, @PathVariable int pitId) {
        final GameDto dto = this.service.play(gameId, pitId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
