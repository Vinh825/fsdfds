package com.example.IAM.publicApi.controller;


import com.example.IAM.publicApi.model.request.AuthenticationRequest;
import com.example.IAM.publicApi.model.response.AuthenticationResponse;
import com.example.IAM.publicApi.service.ExAuthenticationService;

import com.example.IAM.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/public/login")
public class ExAuthenticationController {

    @Autowired
    private ExAuthenticationService exAuthenticationService;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = exAuthenticationService.authenticate(request);
        if (!Objects.isNull(response.getError())) {
            return ResponseHandler.unauthorized(response.getError());
        }
        return ResponseHandler.success(response);
    }

}
