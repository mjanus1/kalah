package com.mjanus.kalah.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjanus.kalah.model.Player;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class GameDto {

    @ApiModelProperty(notes = "Game identifier", name = "id")
    private final String id;
    @ApiModelProperty(notes = "Uri address to make move", name = "uri")
    private final String uri;
    @ApiModelProperty(notes = "Current position in pots", name = "status")
    private final Map<Integer, Integer> status;
    @ApiModelProperty(notes = "Name of player turn move", name = "playerTurn")
    private final Player playerTurn;
}
