package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.SiteService;
import searchengine.services.StatisticsService;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteService siteService;

    @Autowired
    public ApiController(StatisticsService statisticsService, SiteService siteService) {
        this.statisticsService = statisticsService;
        this.siteService = siteService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/indexing")
    public HashMap<String, Boolean> startIndexing()
    {
        long start = System.currentTimeMillis();
        siteService.startIndexing();
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        System.out.println("It's done! Finally!");
        System.out.println("It takes: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        return response;
    }
}
