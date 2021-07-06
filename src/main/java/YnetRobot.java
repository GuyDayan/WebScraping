import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YnetRobot extends BaseRobot {
    Document website;
    private Map<String, Integer> ynetMap = new HashMap<>();
    private ArrayList<String> ynetLinks;

    public YnetRobot() throws Exception {
        super("https://www.ynet.co.il/home/0,7340,L-8,00.html");
        this.website = (Jsoup.connect(super.getRootWebsiteUrl()).get());
        this.ynetLinks = new ArrayList<>();
        importLinks();
    }


    public Map<String, Integer> getWordsStatistics() throws Exception {
        for (String currentArticle : this.ynetLinks){
            String[] articleWords = separateWords(currentArticle);
            insertToMap(articleWords);
        }
        return this.ynetMap;
    }

    public int countInArticlesTitles(String text) {
        int count = 0;
        Element mainTitle = website.getElementsByClass("slotTitle").get(0);
        String textArticle = mainTitle.child(0).text();
        Element subTitles = website.getElementsByClass("YnetMultiStripComponenta oneRow multiRows").get(0);
        Elements newsTitles = website.getElementsByClass("slotsContent");
        if (textArticle.contains(text))
            count++;
        if (subTitles.getElementsByClass("slotView").text().contains(text))
            count++;
        for (Element element : newsTitles){
            if (element.getElementsByClass("withImagePreview").text().contains(text))
                count++;
            if (element.getElementsByClass("slotList").text().contains(text))
                count++;
        }

        return count;
    }

    public String getLongestArticleTitle() throws Exception {
        int largest = 0;
        String largestArticleTitle = "";
        for (String currentArticle : this.ynetLinks) {
            int sumWords = separateWords(currentArticle).length;
            if (sumWords > largest) {
                largest = sumWords;
                largestArticleTitle =importMainTitle(currentArticle);
            }
        }

        return "The title of the longest article is : " +largestArticleTitle + "  with --" +largest + "-- words";
    }

    public void importLinks(){
        String url="";
        Element mainArticle = website.getElementsByClass("slotTitle").get(0);
        this.ynetLinks.add(mainArticle.child(0).attributes().get("href")); // main article
        Element subArticles = website.getElementsByClass("YnetMultiStripComponenta oneRow multiRows").get(0);
        Elements newsTitles = website.getElementsByClass("slotsContent");

        for (Element element : subArticles.getElementsByClass("textDiv")){
            this.ynetLinks.add(element.getElementsByTag("a").attr("href"));
        }
        for (Element element : newsTitles) {
                for (Element element1 : element.getElementsByClass("withImagePreview")) {
                    url = element1.getElementsByTag("a").attr("href");
                    if (url.length()>0)  // check if empty link
                        this.ynetLinks.add(url);
                }
                for (Element element2 : element.getElementsByClass("slotList")) {
                    url=element2.getElementsByTag("a").attr("href");
                    if (url.length()>0) // check if empty link
                       this.ynetLinks.add(url);
                }

        }


    }

    public String[] separateWords(String link) throws Exception{
            String words = "";
            Document web = Jsoup.connect(link).get();
            words = web.getElementsByClass("mainTitle").text() +
                    web.getElementsByClass("subTitle").text() +
                    web.getElementsByClass("text_editor_paragraph rtl").text();
            String[] wordsSpilt = words.split(" ");


            return wordsSpilt;
    }

    public String importMainTitle(String link) throws Exception{
        Document article = Jsoup.connect(link).get();
        String title = article.getElementsByClass("mainTitleWrapper").text();
        return title;
    }

    public void insertToMap(String[] words) {
        for (String word : words) {
            if (this.ynetMap.containsKey(word)){
                ynetMap.put(word, ynetMap.get(word) + 1);
        }else
            ynetMap.put(word , 1);
        }
    }
}
