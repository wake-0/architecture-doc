package com.example.architecture.doc.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceFacade {
    
    private final MyService firstService;
    private final MySecondService secondService; 

    public void run() {
        System.out.println("run...");
        this.firstService.run();
        this.secondService.run();
    }

}
