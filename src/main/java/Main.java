
import java.util.Map;
import java.util.Scanner;

public class Main {

    private final MakoRobot makoRobot;
    private WallaRobot wallaRobot;
    private YnetRobot ynetRobot;
    private BaseRobot [] robots =new BaseRobot[3];

    public Main() throws Exception {
        this.makoRobot = new MakoRobot();
        this.ynetRobot = new YnetRobot();
        this.wallaRobot = new WallaRobot();
        this.robots[0] = this.makoRobot;
        this.robots[1] = this.ynetRobot;
        this.robots[2] = this.wallaRobot;
        mainGameLoop();

    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
    }


    public void mainGameLoop() throws Exception {
        Scanner scanner = new Scanner(System.in);
        int score=0;
        System.out.println("Choose your robot game");
        System.out.print("0.MakoRobot" + "\n" + "1.YnetRobot" + "\n" + "2.WallaRobot" + "\n");
        int option = scanner.nextInt();
        System.out.print("Please wait loading the data...");
        Map<String, Integer> gameMap = this.robots[option].getWordsStatistics();
        System.out.println("\n"+ "Welcome to guess game" + "\n" +"You have one clue from us--> ");
        String clue = this.robots[option].getLongestArticleTitle();
        System.out.println(clue);
        for (int i=0; i< 5 ; i++){
            System.out.println("---Enter your word guess---");
            if (i==0)
                scanner.nextLine();
           String word = scanner.nextLine();
           Integer count = gameMap.get(word);
           if (count==null)
               count = 0;
           System.out.println("you earn " + count + " points on your guess ");
           score+= count;
        }

        System.out.println("--Round-2--" +"\n" + "--Please enter your title guess--");
        String title = scanner.nextLine();
        System.out.println("Guess how much times the title appearance (numbers)");
        int guessApps = scanner.nextInt();
        int countApps = this.robots[option].countInArticlesTitles(title);
        if (Math.abs(countApps-guessApps) <= 2)
            score+=250;
        System.out.println("Your final score is " +score);



    }

}
