package searchengine.services;

import searchengine.models.SiteEntity;

import java.io.IOException;

public interface SiteIndexingService {

    void removeAllSites();
    void startIndexing() throws IOException;

    void addSite(SiteEntity site);

}
