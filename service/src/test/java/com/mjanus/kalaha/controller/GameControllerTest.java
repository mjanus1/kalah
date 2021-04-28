package com.mjanus.kalaha.controller;

import com.mjanus.kalah.controller.GameController;
import com.mjanus.kalah.dto.GameDto;
import com.mjanus.kalah.exception.GameExceptionHandler;
import com.mjanus.kalah.exception.GameNotFoundException;
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
        GameDto dto = new GameDto("1", URL);
        Mockito.when(service.createGame()).thenReturn(dto);
        mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(dto.getUri()));
    }

    @Test
    public void getGame_shouldReturnSavedGameWithSuccess() throws Exception {
        GameDto dto = new GameDto("1", URL);
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
}
