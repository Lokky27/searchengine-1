package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.SiteIndexingService;
import searchengine.services.StatisticsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteIndexingService siteIndexingService;
    @Autowired
    public ApiController(StatisticsService statisticsService, SiteIndexingService siteIndexingService) {
        this.statisticsService = statisticsService;
        this.siteIndexingService = siteIndexingService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/indexing")
    public HashMap<String, Boolean> startIndexing() throws IOException {
        HashMap<String, Boolean> response = new HashMap<>();
        siteIndexingService.startIndexing();
        response.put("result", true);
        return response;
    }
}
