package com.mkyong.controller;


import com.mkyong.helpers.Patcher;
import com.mkyong.model.Userr;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.repository.UserRepository;
import com.mkyong.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.net.http.HttpResponse;
import java.nio.Buffer;
import java.security.Security;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    Patcher patcher;  // custom class for updating fields
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<Userr> findAll() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<Userr> allUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Userr currentUser = (Userr) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);

    }

    // create a user

//    @GetMapping("/{id}")
//    public Optional<Userr> findById(@PathVariable Long id) {
//        return userService.findById(id);
//    }

    @GetMapping("/{email}")
    public Optional<Userr> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    // update a user
    @PatchMapping("/update")
    public ResponseEntity<Userr> update(@RequestBody RegisterUserDto user) {
        System.out.println("dto in controller" + user.getName());
        return ResponseEntity.ok(userService.update(user));
    }

    // delete a user
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @GetMapping(value = "/{userId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrCodeFile(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(UserService.generateQRCodeImage(userId), HttpStatus.OK);

    }

    @GetMapping(value = "/{userId}/qrcodeSvg")
    public ResponseEntity<String> qrCodeSvg(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(UserService.getQRCodeSvg(userId, 700, 700, false), HttpStatus.OK);
    }


}
