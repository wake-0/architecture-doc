package com.example.architecture.doc.services;

import org.springframework.stereotype.Service;

@Service
class MySecondServiceImpl implements MySecondService {

    @Override
    public void run() {
        System.out.println("MySecondService run...");        
    }

}
