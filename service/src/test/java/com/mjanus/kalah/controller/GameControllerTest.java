package com.mjanus.kalah.controller;

import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameExceptionHandler;
import com.mjanus.kalah.exception.GameNotFoundException;
import com.mjanus.kalah.exception.WrongMoveException;
import com.mjanus.kalah.model.Player;
import com.mjanus.kalah.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {GameController.class, GameExceptionHandler.class})
@WebMvcTest
public class GameControllerTest {

    private static final String URL = "http://localhost:8080/games/1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService service;

    @Test
    public void crateGame_shouldCreateGameWithSuccess() throws Exception {
        GameDto dto = GameDto.builder()
                .id("1")
                .uri(URL)
                .build();
        Mockito.when(service.createGame()).thenReturn(dto);
        mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(dto.getUri()));
    }

    @Test
    public void getGame_shouldReturnSavedGameWithSuccess() throws Exception {
        GameDto dto = GameDto.builder()
                .id("1")
                .uri(URL)
                .build();
        Mockito.when(service.getGame("1")).thenReturn(dto);
        mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(dto.getUri()));
    }

    @Test
    public void getGame_shouldThrowExceptionWhenGameNotExist() throws Exception {
        Mockito.when(service.getGame("1")).thenThrow(new GameNotFoundException("Game not found"));
        mockMvc.perform(get("/games/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof GameNotFoundException))
                .andExpect(result -> Assertions.assertEquals("Game not found", result.getResolvedException().getMessage()));
    }


    @Test
    public void play_shouldThrowExceptionWhenGameNotExist() throws Exception {
        Mockito.when(service.play("1", 1)).thenThrow(new GameNotFoundException("Game not found"));
        mockMvc.perform(put("/games/1/pits/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof GameNotFoundException))
                .andExpect(result -> Assertions.assertEquals("Game not found", result.getResolvedException().getMessage()));
    }

    @Test
    public void play_shouldThrowExceptionWhenGameWhenWrongMove() throws Exception {
        Mockito.when(service.play("1", 1)).thenThrow(new WrongMoveException("Wrong move exception"));
        mockMvc.perform(put("/games/1/pits/1"))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof WrongMoveException))
                .andExpect(result -> Assertions.assertEquals("Wrong move exception", result.getResolvedException().getMessage()));
    }

    @Test
    public void play_shouldReturnUpdatedStatusOfGame() throws Exception {
        Map<Integer, Integer> status = new HashMap<>();
        status.put(1, 6);
        status.put(2, 6);
        status.put(3, 6);
        status.put(4, 6);
        status.put(5, 6);
        status.put(6, 6);
        status.put(7, 0);
        status.put(8, 6);
        status.put(9, 6);
        status.put(10, 6);
        status.put(11, 6);
        status.put(12, 6);
        status.put(13, 6);
        status.put(14, 0);

        GameDto dto = GameDto.builder()
                .id("1")
                .uri(URL)
                .status(status)
                .playerTurn(Player.PLAYER_FIRST)
                .build();

        Mockito.when(service.play("1", 1)).thenReturn(dto);

        mockMvc.perform(put("/games/1/pits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(dto.getUri()))
                .andExpect(jsonPath("$.status").isMap())
                .andExpect(jsonPath("$.playerTurn").value(dto.getPlayerTurn().name()));
    }
}
