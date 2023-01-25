package searchengine.services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.models.PageEntity;
import searchengine.models.SiteEntity;
import searchengine.models.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
@Component
public class RecursiveParser extends RecursiveAction {
    @Autowired
    private SitesList sitesList;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageRepository pageRepository;

    private List<Site> sites;
    public RecursiveParser(List<Site> sites) {
        this.sites = sites;
    }
    @Override
    protected void compute() {
        sites = sitesList.getSites();
        if (sites.size() < 2) {
            sites.forEach(site -> {
                try {
                    parseSite(site.getUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        RecursiveParser firstPart = new RecursiveParser(sites.subList(0, sites.size() / 2));
        RecursiveParser secondPart = new RecursiveParser(sites.subList(sites.size() / 2, sites.size()));
        ForkJoinTask.invokeAll(firstPart, secondPart);
    }

    private void parseSite(String url) throws IOException {
        SiteEntity site = new SiteEntity();
        Connection connection = Jsoup.connect(url)
                .maxBodySize(0)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("https://www.google.com");
        Document document = connection.get();
        site.setName(document.title());
        site.setUrl(url);
        site.setStatusTime(LocalDateTime.now());
        site.setStatus(Status.INDEXING);
        site.setLastError("");
        siteRepository.save(site);
        Elements links = document.select("a[href~=^" + url + "|^/[a-z]]");
        for (Element link : links) {
            String cutPath = link.attributes().get("href");
            String fullPath = cutPath.matches("^" + url + ".*") ? cutPath : url + cutPath;
            fullPath += fullPath.matches(".+/$") ? "" : "/";
            PageEntity page = new PageEntity();
            page.setSite(site);
            page.setPath(cutPath);
            page.setStatusCode(Jsoup.connect(fullPath).execute().statusCode());
            page.setContent(Jsoup.connect(fullPath).get().html());
            pageRepository.save(page);
        }
    }
}
