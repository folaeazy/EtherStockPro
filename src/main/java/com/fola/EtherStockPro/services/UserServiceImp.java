package com.fola.EtherStockPro.services;

import com.fola.EtherStockPro.DTO.Requests.LoginRequestDTO;
import com.fola.EtherStockPro.DTO.Requests.RegisterRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.TransactionDTO;
import com.fola.EtherStockPro.DTO.UserDTO;
import com.fola.EtherStockPro.entity.User;
import com.fola.EtherStockPro.enums.UserRoles;
import com.fola.EtherStockPro.exceptions.InvalidCredentialsException;
import com.fola.EtherStockPro.exceptions.NotFoundException;
import com.fola.EtherStockPro.interfaces.UserService;
import com.fola.EtherStockPro.repository.UserRepository;
import com.fola.EtherStockPro.security.AuthUser;
import com.fola.EtherStockPro.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;



    @Override
    public ApiResponse<String> registerUser(RegisterRequestDTO registerRequestDTO) {

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())){
            return ApiResponse.<String>builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message("User already exist")
                    .build();
        }
        UserRoles userRoles  = UserRoles.MANAGER;
        if(registerRequestDTO.getRole() != null) {
            userRoles = registerRequestDTO.getRole();
        }

        // creating new user
        User newUser = User.builder()
                .name(registerRequestDTO.getName())
                .email((registerRequestDTO.getEmail()))
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .phoneNumber(registerRequestDTO.getPhoneNumber())
                .role(userRoles)
                .build();

        userRepository.save(newUser);

        return ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("user created successfully")
                .build();
    }

    @Override
    public ApiResponse<String> loginUser(LoginRequestDTO loginRequestDTO) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
            AuthUser authUser =  (AuthUser) authentication.getPrincipal();

            //  Generate a JWT Auth token if no errors from above
            String token = jwtUtils.generateToken(authUser.getUsername());
            return ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .message("User logged in successfully")
                    .id(authUser.getUser().getId())
                    .role(authUser.getUser().getRole())
                    .token(token)
                    .expirationTime("1 month")
                    .build();

        } catch (BadCredentialsException | UsernameNotFoundException e ) {
            throw new InvalidCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    @Override
    public ApiResponse<List<UserDTO>> getAllUsers() {

        //----------LOCAL MODEL MAPPER TO SKIP TRANSACTION FIELD--------------//
        PropertyMap<User, UserDTO> skipTransactions = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getTransactions());
            }
        };

        ModelMapper localMapper = new ModelMapper();
        localMapper.addMappings(skipTransactions);
        //------------------------ENDS HERE---------------//

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); //in order of latest user

        List<UserDTO> userDTOS = localMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
        //filter away the users transaction
        userDTOS.forEach(userDto -> userDto.setTransactions(null));


        return ApiResponse.<List<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .userDTOS(userDTOS)
                .build();
    }

    @Override
    public ApiResponse<UserDTO> getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user not found"));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setTransactions(null);

        return ApiResponse.<UserDTO>builder()
                .userDTO(userDTO)
                .build();
    }

    @Override
    public ApiResponse<UserDTO> updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if(userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        userRepository.save(existingUser);

        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .message("update successful")
                .build();

    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
         userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

         userRepository.deleteById(id);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("user deleted successfully")
                .build();


    }

    @Override
    public ApiResponse<List<TransactionDTO>> getUserTransactions(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // map to userDTO
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        // Access the Transactions field to its DTO and render user and supply null to avoid circular or recursive calling
        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });

        return ApiResponse.<List<TransactionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .transactionDTOS(userDTO.getTransactions())
                .build();
    }
}
