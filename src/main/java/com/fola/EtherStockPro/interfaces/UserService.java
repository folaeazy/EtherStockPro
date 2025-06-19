package com.fola.EtherStockPro.interfaces;

import com.fola.EtherStockPro.DTO.Requests.LoginRequestDTO;
import com.fola.EtherStockPro.DTO.Requests.RegisterRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.TransactionDTO;
import com.fola.EtherStockPro.DTO.UserDTO;
import com.fola.EtherStockPro.entity.User;

import java.util.List;

public interface UserService {

    ApiResponse <String> registerUser(RegisterRequestDTO registerRequestDTO);

    ApiResponse <String> loginUser(LoginRequestDTO loginRequestDTO);

    ApiResponse <List<UserDTO>>  getAllUsers();

    User getCurrentLoggedInUser();

    ApiResponse<UserDTO> updateUser(Long id, UserDTO userDTO);

    ApiResponse<Void> deleteUser(Long id);

    ApiResponse<List<TransactionDTO>> getUserTransactions(Long id);

}
