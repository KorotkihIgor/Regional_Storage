package ru.netology.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseToken {

    @JsonProperty("auth-token")
//    @JsonProperty("authorization")
    private String token;
}
