package searchengine.services;

import org.springframework.stereotype.Service;
import searchengine.models.SiteEntity;

@Service
public interface SiteService {
    void startIndexing();

    void addSite(SiteEntity site);

    void deleteSiteById(Integer siteId);
}
