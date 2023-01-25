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
import searchengine.models.PageEntity;
import searchengine.models.SiteEntity;
import searchengine.models.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;


@Service
public class SiteIndexingServiceImp implements SiteIndexingService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    private final RecursiveParser recursiveParser;
    @Autowired
    public SiteIndexingServiceImp(SiteRepository siteRepository,
                                  PageRepository pageRepository,
                                  RecursiveParser recursiveParser) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.recursiveParser = recursiveParser;
    }
    @Override
    public void removeAllSites() {
        siteRepository.deleteAll();
    }

    @Override
    public void startIndexing() {
        ForkJoinPool.commonPool().invoke(recursiveParser);
    }

    @Override
    public void addSite(SiteEntity site) {
        siteRepository.save(site);
    }

//    private void parseSite(String url) throws IOException {
//        SiteEntity site = new SiteEntity();
//        Connection connection = Jsoup.connect(url)
//                .maxBodySize(0)
//                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                .referrer("https://www.google.com");
//        Document document = connection.get();
//        site.setName(document.title());
//        site.setUrl(url);
//        site.setStatusTime(LocalDateTime.now());
//        site.setStatus(Status.INDEXING);
//        site.setLastError("");
//        siteRepository.save(site);
//        Elements links = document.select("a[href~=^" + url + "|^/[a-z]]");
//        for (Element link : links) {
//            String cutPath = link.attributes().get("href");
//            String fullPath = cutPath.matches("^" + url + ".*") ? cutPath : url + cutPath;
//            fullPath += fullPath.matches(".+/$") ? "" : "/";
//            PageEntity page = new PageEntity();
//            page.setSite(site);
//            page.setPath(cutPath);
//            page.setStatusCode(Jsoup.connect(fullPath).execute().statusCode());
//            page.setContent(Jsoup.connect(fullPath).get().html());
//            pageRepository.save(page);
//        }
//    }
}
