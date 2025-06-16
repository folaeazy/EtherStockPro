package com.fola.EtherStockPro.DTO.Requests;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {


    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;


}
