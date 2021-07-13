
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakoRobot extends BaseRobot {
    Document website;
    private Map<String, Integer> makoMap = new HashMap<>();
    private ArrayList<String> makoLinks;

    public MakoRobot()throws Exception  {
        super("https://www.mako.co.il/");
            this.website = (Jsoup.connect(super.getRootWebsiteUrl()).get());
        this.makoLinks = new ArrayList<>();
        importLinks();
    }

    public Map<String, Integer> getWordsStatistics() throws Exception {
        for (String currentArticle : this.makoLinks){
            String[] articleWords = separateWords(currentArticle);
            if (articleWords.length>0)
                insertToMap(articleWords);
        }

        return this.makoMap;
    }

    public int countInArticlesTitles(String text) {
        int count =0;
        Elements mainTitles = website.getElementsByClass("headline");
        Elements subTitles = website.getElementsByClass("element");
        for (Element element : mainTitles){
            if (element.text().contains(text))
                count++;
        }
        for (Element element1 : subTitles){
          String title =element1.getElementsByTag("h5").tagName("span").text();
          if (title.contains(text))
              count++;
        }
        return count;
    }

    public String getLongestArticleTitle() throws Exception {
        int largest = 0;
        String largestArticleTitle = "";
        for (String currentArticle : this.makoLinks) {
            int sumWords = 0;
            try {
                sumWords = separateWords(currentArticle).length;
            } catch (Exception e) {
            }
            if (sumWords > largest) {
                largest = sumWords;
                largestArticleTitle = importMainTitle(currentArticle);
            }
        }

        return "The title of the longest article is : " +largestArticleTitle + "  with --" +largest + "-- words";
    }
        public void importLinks() {
        String url = "";
        Element subTitles = website.getElementsByClass("mako_main_portlet_group_container_td side_bar_width").get(0);
        Elements subElements = subTitles.getElementsByClass("mako_main_portlet_container");
        Elements mainTitles = website.getElementsByClass("teasers");
        Elements mainLinks = mainTitles.get(0).getElementsByTag("h2");
        for (Element element : mainLinks) {
            url= getRootWebsiteUrl() + element.child(0).attributes().get("href");
            this.makoLinks.add(url);
        }
        for (Element element : subElements){
            if (element.childrenSize()==1){
                Elements elements = element.child(0).child(2).getElementsByClass("verticalOuter");
                if (elements.size()>0){
                    for (Element element1 : elements){
                        url=element1.child(0).getElementsByTag("a").attr("href");
                        if (!url.contains("https")){
                            url = getRootWebsiteUrl()+url;
                            this.makoLinks.add(url);
                        }
                    }
                }
            }
        }
    }
        public String[] separateWords (String link) throws Exception{
            String words = "";
            try {
                Document web = Jsoup.connect(link).get();
                Elements elements = web.getElementsByTag("header");
                for (Element element : elements) {
                    words += element.getElementsByTag("h1").text();
                    words += element.getElementsByTag("h2").text();
                }

                Elements body = web.getElementsByClass("article-body");
                for (Element element1 : body) {
                    words += element1.text();
                }
            } catch (IOException e) {
                System.out.print("");
            }

            String[] wordsSpilt = words.split(" ");
            return wordsSpilt;

        }
        public String importMainTitle (String link) throws Exception {
        String title="";
        Document article = Jsoup.connect(link).get();
        Elements elements = article.getElementsByTag("header");
        for (Element element : elements)
            title = element.getElementsByTag("h1").text();

        return title;
        }
        public void insertToMap (String[]words){
            for (String word : words) {
                if (this.makoMap.containsKey(word)) {
                    makoMap.put(word, makoMap.get(word) + 1);
                } else
                    makoMap.put(word, 1);
            }
        }


        }





