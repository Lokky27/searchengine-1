package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.RecursiveParse;
import searchengine.config.SitesList;
import searchengine.models.SiteEntity;
import searchengine.repos.PageRepository;
import searchengine.repos.SiteRepository;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList sitesList;
    private final RecursiveParse recursiveParse;

    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository,
                           PageRepository pageRepository,
                           SitesList sitesList,
                           RecursiveParse recursiveParse) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.sitesList = sitesList;
        this.recursiveParse = recursiveParse;
    }
    @Override
    public Optional<SiteEntity> findSiteById(Integer siteId) {
        return siteRepository.findById(siteId);
    }

    @Override
    public void startIndexing() {
        for (int i = 0; i < sitesList.getSites().size(); i++) {
            ForkJoinPool.commonPool().invoke(recursiveParse);
        }
    }

    @Override
    public void addSite(SiteEntity site) {
        siteRepository.save(site);
    }

    @Override
    public void deleteSiteById(Integer siteId) {
        siteRepository.deleteById(siteId);
    }

    /*
    public void parseSite(Site siteFromProperties) {

        Connection connection;
        SiteEntity site = new SiteEntity();
        try {
            connection = Jsoup
                    .connect(siteFromProperties.getUrl())
                    .maxBodySize(0)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("https://www.google.com");
            Document document = connection.get();
            site.setUrl(siteFromProperties.getUrl());
            site.setName(siteFromProperties.getName());
            site.setStatus(Status.INDEXING);
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
            Elements links = document.select("a[href~=^" + siteFromProperties.getUrl() + "|^/[a-z]]");
            for (Element link : links) {
                String cutUrl = link.attributes().get("href");
                String fullUrl = cutUrl.matches("^" +
                        siteFromProperties.getUrl() + ".*") ? cutUrl : siteFromProperties.getUrl() + cutUrl;
                fullUrl += fullUrl.matches(".+/$") ? "" : "/";
                Connection pageConnection = Jsoup.connect(fullUrl);
                PageEntity page = new PageEntity();
                page.setSite(site);
                page.setPath(fullUrl);
                page.setStatusCode(pageConnection.execute().statusCode());
                page.setContent(pageConnection.get().html());
                pageRepository.save(page);
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            site.setLastError(exception.getMessage());
        }
     }
     */
}
