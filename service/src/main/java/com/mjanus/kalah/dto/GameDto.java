package com.mjanus.kalah.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDto {

    @ApiModelProperty(notes = "Game identifier", name = "id")
    private final String id;
    @ApiModelProperty(notes = "Uri address to make move", name = "uri")
    private final String uri;
    @ApiModelProperty(notes = "Current position in pots", name = "status")
    private final Map<Integer, Integer> status;

    public GameDto(final String id, final String uri) {
        this(id, uri, null);
    }

    public GameDto(final String id, final String uri, final Map<Integer, Integer> status) {
        this.id = id;
        this.uri = uri;
        this.status = status;
    }
}
