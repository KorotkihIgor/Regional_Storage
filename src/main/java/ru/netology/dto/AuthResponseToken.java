package ru.netology.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseToken {

    @JsonProperty("auth-token")
//    @JsonProperty("authorization")
    private String token;
}
