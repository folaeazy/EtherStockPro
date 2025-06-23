package com.fola.EtherStockPro.controllers;


import com.fola.EtherStockPro.DTO.Requests.LoginRequestDTO;
import com.fola.EtherStockPro.DTO.Requests.RegisterRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "create a new user")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return  ResponseEntity.ok(userService.registerUser(registerRequestDTO));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return  ResponseEntity.ok(userService.loginUser(loginRequestDTO));
    }

}
