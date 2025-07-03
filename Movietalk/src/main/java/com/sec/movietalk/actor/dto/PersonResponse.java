package com.sec.movietalk.actor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonResponse {

    private Long id;
    private String name;
    private Integer gender;

    @JsonProperty("profile_path")
    private String profilePath;
}