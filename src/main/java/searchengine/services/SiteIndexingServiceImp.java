package searchengine.services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.models.Page;
import searchengine.models.SiteEntity;
import searchengine.models.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class SiteIndexingServiceImp implements SiteIndexingService {

    @Autowired
    private final SiteRepository siteRepository;
    @Autowired
    private final PageRepository pageRepository;
    @Autowired
    private final SitesList sitesList;
    public SiteIndexingServiceImp(SiteRepository siteRepository,
                                  PageRepository pageRepository,
                                  SitesList sitesList) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.sitesList = sitesList;
    }
    @Override
    public void removeAllSites() {
        siteRepository.deleteAll();
    }

    @Override
    public void startIndexing() throws IOException {
        List<Site> sites = sitesList.getSites();
        Site site = sites.get(0);
        String siteUrl = site.getUrl();
        parseSite(siteUrl);
    }

    @Override
    public void addSite(SiteEntity site) {
        siteRepository.save(site);
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
        site.setStatusTime(new Date());
        site.setStatus(Status.INDEXING);
        site.setLastError("");
        siteRepository.save(site);
        Elements links = document.select("a[href~=^" + url + "|^/[a-z]]");
        for (Element link : links) {
            String cutPath = link.attributes().get("href");
            String fullPath = cutPath.matches("^" + url + ".*") ? cutPath : url + cutPath;
            fullPath += fullPath.matches(".+/$") ? "" : "/";
            Page page = new Page();
            page.setSite(site);
            page.setPath(cutPath);
            page.setStatusCode(Jsoup.connect(fullPath).execute().statusCode());
            page.setContent(Jsoup.connect(fullPath).get().html());
            pageRepository.save(page);
        }
    }
}
