package com.thembu.guessza.config.clientconfig;


import com.thembu.guessza.round.dto.RoundResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.thembu.guessza.config.clientconfig.dto.ConfigResponse;

@RestController
@RequestMapping("/api/config")
@SecurityRequirement(name = "bearerAuth")

public class ConfigController {

private final MapsProperties apiKey;

public  ConfigController (MapsProperties apiKey) {
 this.apiKey = apiKey;
}

@GetMapping("")
@ResponseStatus(HttpStatus.OK)
@ApiResponse(responseCode = "200" , description = "Fetched Api key successfully" , content = @Content(schema = @Schema(implementation = ConfigResponse.class)))
public ConfigResponse getKey() {
    return new ConfigResponse(apiKey.apiKey());
};


}
