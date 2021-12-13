package com.example.architecture.doc.resource;

import com.example.architecture.doc.services.ServiceFacade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CoreResource {
    
    private final ServiceFacade facade; 

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        facade.run();
        return ResponseEntity.ok("my body 123456");
    }

}
