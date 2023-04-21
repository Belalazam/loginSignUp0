package com.example.loginSignUp.controller;

import com.example.loginSignUp.entity.User;
import com.example.loginSignUp.repo.UserRepository;
import com.example.loginSignUp.service.EmailService;
import dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
@RequestMapping("/api/auth")
public class loginSignupController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/loginuser")
    AllResponse loginPage(@RequestBody LoginRequest loginRequest)
    {
        AllResponse allResponse = new AllResponse();
        try {
            User user = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
            if (user != null) {
                allResponse.setSuccess(true);
            } else {
                allResponse.setSuccess(false);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            allResponse.setSuccess(false);
        }
        return allResponse;
    }
    @PostMapping("/signupuser")
    AllResponse signupPage(@RequestBody SignUpRequest signUpRequest)
    {
        User newUser = new User();
        AllResponse allResponse = new AllResponse();
        try {
            User user = userRepository.findByEmail(signUpRequest.getEmail());
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setPassword(signUpRequest.getPassword());
            newUser.setUsername(signUpRequest.getUsername());

            if (user == null) {
                allResponse.setSuccess(true);
            } else {
                allResponse.setSuccess(false);
                return allResponse;
            }
        }
        catch (Exception e)
        {
            System.out.println("error " + e);
            allResponse.setSuccess(false);
            return allResponse;
        }
        userRepository.save(newUser);
        return allResponse;
    }
    @PostMapping("/otpRequest")
    AllResponse otpRequestPage(@RequestBody OtpRequest otpRequest)
    {
        User newUser = new User();
        AllResponse allResponse = new AllResponse();
        try {
            User user = userRepository.findByEmail(otpRequest.getEmail());
            if (user != null) {
                allResponse.setSuccess(true);
            } else {
                allResponse.setSuccess(false);
                return allResponse;
            }
            String randomCode = generateRandomCode();
            user.setPassword(randomCode);
            emailService.sendSimpleMessage(otpRequest.getEmail(),"confirmation code",randomCode);
            userRepository.save(user);
        }
        catch (Exception e)
        {
            System.out.println("error " + e);
            allResponse.setSuccess(false);
            return allResponse;
        }
        return allResponse;
    }
    @PostMapping("/newpassword")
    AllResponse newPassword(@RequestBody NewPasswordSetRequest newPasswordSetRequest)
    {
        User newUser = new User();
        AllResponse allResponse = new AllResponse();
        try {
            User user = userRepository.findByEmail(newPasswordSetRequest.getEmail());
            if (user != null) {
                allResponse.setSuccess(true);
            } else {
                allResponse.setSuccess(false);
                return allResponse;
            }
            user.setPassword(newPasswordSetRequest.getNewPassword());
            userRepository.save(user);
        }
        catch (Exception e)
        {
            System.out.println("error " + e);
            allResponse.setSuccess(false);
            return allResponse;
        }
        return allResponse;
    }
    @PostMapping("/validateotp")
    AllResponse validateOtp(@RequestBody ValidateRequest validateRequest)
    {
        User newUser = new User();
        AllResponse allResponse = new AllResponse();
        try {
            User user = userRepository.findByEmail(validateRequest.getEmail());
            if (user != null) {
                allResponse.setSuccess(true);
            }else {
                allResponse.setSuccess(false);
                return allResponse;
            }
            if(user.getPassword().equals(validateRequest.getOtp()))
            {
                allResponse.setSuccess(true);
            }
            else
            {
                allResponse.setSuccess(false);
            }
            userRepository.save(user);
        }
        catch (Exception e)
        {
            System.out.println("error " + e);
            allResponse.setSuccess(false);
            return allResponse;
        }
        return allResponse;
    }

    public static String generateRandomCode() {
        Random random = new Random();
        int code = random.nextInt(999999 - 100000 + 1) + 100000;
        return String.format("%06d", code);
    }

}
