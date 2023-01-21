package searchengine.services;

import org.springframework.stereotype.Component;
import searchengine.config.SitesList;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.util.concurrent.RecursiveAction;
@Component
public class RecursiveParser extends RecursiveAction {
    private SitesList sitesList;
    private SiteRepository siteRepository;
    private PageRepository pageRepository;
    @Override
    protected void compute() {

    }
}
