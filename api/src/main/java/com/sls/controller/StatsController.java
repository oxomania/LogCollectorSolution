package com.sls.controller;

import com.sls.service.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class StatsController {

    private final Stats stats;

    public StatsController(@Autowired Stats stats) {
        this.stats = stats;
    }

    @CrossOrigin
    @GetMapping("/log-stats/{duration}")
    public ResponseEntity<Map<String, Integer>> logStats(@PathVariable int duration){
        return ResponseEntity.ok(this.stats.getLogCounts(duration));
    }
}