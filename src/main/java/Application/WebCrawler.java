package Application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class WebCrawler {
    private HashSet<String> urlLinks;

    public WebCrawler() {
        urlLinks = new HashSet<String>();

        File file = new File("ScrapedWebsite");
        file.mkdirs();
        System.out.println("Created ScrapedWebsite folder, will now commence scraping!");
        System.out.println("-");
    }

    public void getPageLinks(String URL) {

        String substringLink = URL.substring(26);

        System.out.println("-");
        System.out.println("-Crawler crawling for new href-");


        if (!urlLinks.contains(substringLink)) {
            System.out.println("Crawler found: " + substringLink);
            try {
                if (urlLinks.add(substringLink)) {
                    System.out.println(substringLink);
                    WebScraper.scrape(substringLink);
                }

                Document doc = Jsoup.connect(URL).ignoreContentType(true).get();
                Elements availableImgsOnPage = doc.select("img[src]");
                Elements availableCssOnPage = doc.select("link[href]");
                Elements availableScriptOnPage = doc.select("script[src]");
                Elements availableLinksOnPage = doc.select("a[href]");
                for (Element ele1 : availableImgsOnPage) {
                    getPageLinks(ele1.attr("abs:src"));
                    for (Element ele2 : availableCssOnPage) {
                        getPageLinks(ele2.attr("abs:href"));
                        for (Element ele3 : availableScriptOnPage) {
                            getPageLinks(ele3.attr("abs:src"));
                            for (Element ele4 : availableLinksOnPage) {
                                getPageLinks(ele4.attr("abs:href"));
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        } else {
            System.out.println("Crawler ignored duplicate.");

        }
    }
}


