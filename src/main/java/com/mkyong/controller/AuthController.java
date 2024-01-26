package com.mkyong.controller;


import com.mkyong.model.Userr;
import com.mkyong.model.dtos.LoginUserDto;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.responses.LoginResponse;
import com.mkyong.service.AuthenticationService;
import com.mkyong.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final JWTService jwtService;

    public AuthController(
            AuthenticationService authenticationService_,
            JWTService jwtService_,
            AuthenticationManager authenticationManager_
    ) {
        this.authenticationService = authenticationService_;
        this.jwtService = jwtService_;
    }

    @GetMapping("/welcome")
    public String welcome() {

        return "This is a welcome message. \n this endpoint is not secure";
    }

    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/register")
    public Userr register(@RequestBody RegisterUserDto user) {
        return authenticationService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto) {
        Userr authenticatedUser = authenticationService.login(dto);
        System.out.println("authenticated user =>> " + authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
//        return ResponseEntity.ok(new LoginResponse());

    }

//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody) {
//
//    }


}


