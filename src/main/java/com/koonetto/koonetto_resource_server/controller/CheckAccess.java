package com.koonetto.koonetto_resource_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CheckAccess {

    @GetMapping(path = "/check")
    public ResponseEntity<String> clientAccess() {
        return ResponseEntity.ok("OK");
    }


    @GetMapping(path = "/user")
    public ResponseEntity<String> userAccess(@RequestParam(name = "email", required = true) String email) {
        return ResponseEntity.ok(String.format("Welcome %s", email));
    }

}
