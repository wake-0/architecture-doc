package com.example.architecture.doc.services;

import org.springframework.stereotype.Service;

@Service
class MyServiceImpl implements MyService {
    
    @Override
    public void run() {
        System.out.println("MyService run...");        
    }

}
