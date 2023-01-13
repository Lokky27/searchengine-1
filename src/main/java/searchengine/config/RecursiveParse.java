package searchengine.config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import searchengine.models.PageEntity;
import searchengine.models.SiteEntity;
import searchengine.models.Status;
import searchengine.repos.PageRepository;
import searchengine.repos.SiteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.RecursiveAction;

@Component
public class RecursiveParse extends RecursiveAction {
    private final SitesList sitesList;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    public RecursiveParse(SitesList sitesList, SiteRepository siteRepository, PageRepository pageRepository) {
        this.sitesList = sitesList;
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }

    @Override
    protected void compute() {
        for (Site site : sitesList.getSites()) {
            parseSite(site);
        }

    }

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
                page.setPath(cutUrl);
                page.setStatusCode(pageConnection.execute().statusCode());
                page.setContent(pageConnection.get().html());
                pageRepository.save(page);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            site.setLastError(exception.getMessage());
        }
    }
}