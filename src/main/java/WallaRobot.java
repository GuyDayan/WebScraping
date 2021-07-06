import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallaRobot extends BaseRobot implements RobotMethods{
    Document website;
    private Map<String, Integer> wallaMap = new HashMap<>();
    private ArrayList<String> wallaLinks;

    public WallaRobot() throws Exception{
        super("https://www.walla.co.il/");
            this.website = (Jsoup.connect(super.getRootWebsiteUrl()).get());
        this.wallaLinks = new ArrayList<>();
        importLinks();

    }


    public Map<String, Integer> getWordsStatistics() throws Exception {
        for (String currentArticle : this.wallaLinks){
            String[] articleWords = separateWords(currentArticle);
            insertToMap(articleWords);
        }

        return this.wallaMap;
    }

    public int countInArticlesTitles(String text) {
        int count=0;
        String title;
        Element mainTitle = website.getElementsByClass("with-roof").get(0);
        title = mainTitle.getElementsByTag("h2").text();
        if (title.contains(text))
            count++;
        Element subTitles = website.getElementsByClass("main-taste").get(0);
        for (Element smallTeasers : subTitles.getElementsByTag("a")) {
            title = smallTeasers.getElementsByTag("h3").text();
            if (title.contains(text)){
                count++;
            }
        }

        return count;
    }

    public String getLongestArticleTitle() throws Exception {
        int largest = 0;
        String largestArticleTitle = "";
        for (String currentArticle : this.wallaLinks) {
            int sumWords = separateWords(currentArticle).length;
            if (sumWords > largest) {
                largest = sumWords;
                largestArticleTitle =importMainTitle(currentArticle);
            }
        }

        return "The title of the longest article is : " +largestArticleTitle + "  with --" +largest + "-- words";
    }


    @Override
    public void importLinks() {
        Elements mainTitles = website.getElementsByClass("with-roof");
        for (Element element : mainTitles) {
            wallaLinks.add(element.child(0).attributes().get("href"));
        }

        Element newsTitles = website.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
        for (Element smallTeasers : newsTitles.getElementsByTag("a")) {
            wallaLinks.add(smallTeasers.attributes().get("href"));
        }

    }

    @Override
    public String[] separateWords(String link) throws Exception {
        String words = "";
        Document web = Jsoup.connect(link).get();
        Element element = web.getElementsByClass("item-main-content").get(0).tagName("header");
        words+= element.getElementsByTag("h1").text()+
                element.getElementsByTag("p").text();

        String articleBody = web.getElementsByClass("article-content").get(0).text();
        words+= articleBody;
        String[] wordsSpilt = words.split(" ");

        return wordsSpilt;
    }

    @Override
    public String importMainTitle(String link) throws Exception {
        Document article = Jsoup.connect(link).get();
        Element element = article.getElementsByClass("item-main-content").get(0).tagName("header");
        return  element.getElementsByTag("h1").text();
    }

    @Override
    public void insertToMap(String[] words) {
        for (String word : words) {
            if (this.wallaMap.containsKey(word)){
                wallaMap.put(word, wallaMap.get(word) + 1);
            }else
                wallaMap.put(word , 1);
        }

    }
}
