package com.thembu.guessza.clientconfig;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.thembu.guessza.clientconfig.dto.ConfigResponse;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

private final MapsProperties apiKey;

public  ConfigController (MapsProperties apiKey) {
 this.apiKey = apiKey;
}

@GetMapping("")
@ResponseStatus(HttpStatus.OK)
public ConfigResponse getKey() {
    return new ConfigResponse(apiKey.apiKey());
};


}
