package com.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.batch.service.BatchUserSubscriptionService;
import com.poc.auser.main.entity.UserSubscription;

@RestController
@RequestMapping("/api/subscription")
public class BatchUserSubscriptionController {

    @Autowired
    private BatchUserSubscriptionService batchUserSubscriptionService;

    @PostMapping("/save")
    public ResponseEntity<String> saveUserSubscription(@RequestBody UserSubscription subscription) {
        batchUserSubscriptionService.saveUserSubscription(subscription);
        return ResponseEntity.ok("User subscription saved successfully!");
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserSubscription> getUserSubscription(@PathVariable String userId) {
        UserSubscription subscription = batchUserSubscriptionService.getUserSubscription(userId);
        return ResponseEntity.ok(subscription);
    }
}
