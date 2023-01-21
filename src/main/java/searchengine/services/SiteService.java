package searchengine.services;

import org.springframework.stereotype.Service;
import searchengine.models.SiteEntity;

import java.util.Optional;

@Service
public interface SiteService {
    Optional<SiteEntity> findSiteById(Integer siteId);
    void startIndexing();

    void addSite(SiteEntity site);

    void deleteSiteById(Integer siteId);

}
