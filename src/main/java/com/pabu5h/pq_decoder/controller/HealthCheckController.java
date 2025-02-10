package com.pabu5h.pq_decoder.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
public class HealthCheckController {
    private volatile boolean maintenance;

    @Value("${service.version}")
    private String version;

//    @Value("${auth.path}")
//    private String authPath;
//    @Value("${oqg.path}")
//    private String oqgPath;
//    @Value("${m3.path}")
//    private String m3Path;
//    @Value("${tcm.path}")
//    private String tcmPath;
//    @Value("${owl.path}")
//    private String owlPath;

//    @Autowired
//    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(/*HttpServletResponse httpServletResponse*/) {
        return ResponseEntity.ok()./*headers(headers).*/body("COMTRADE:" + version);
    }
    @GetMapping("/health")
    public String checkHealth() {
//        System.out.println("Health check called at " + LocalDateTime.now() + " and maintenance is " + maintenance);
        return maintenance ? "DOWN" : "UP";
    }
    @Scheduled(cron = "${maintenance.start.cron}")
    public void startMaintenance() {
        maintenance = true;
    }

    @Scheduled(cron = "${maintenance.stop.cron}")
    public void stopMaintenance() {
        maintenance = false;
    }
    }