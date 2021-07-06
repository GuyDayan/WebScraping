import java.util.Map;

public interface RobotMethods {

    void importLinks();

    String[] separateWords (String link) throws Exception;

    String importMainTitle(String link) throws Exception;

    void insertToMap (String[]words);


}
