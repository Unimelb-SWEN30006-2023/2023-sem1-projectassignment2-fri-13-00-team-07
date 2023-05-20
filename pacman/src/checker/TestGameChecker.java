package checker;


/**
 * our tests for a GameChecker, not part of the functionality of this game
 */
public class TestGameChecker {

    public static void main(String[] args){
        GameChecker gameChecker = new GameChecker();
        String dir = "pacman/data/ultimateTest";

        if (gameChecker.checkGame(dir)) {
            for(String name: gameChecker.getValidMapFiles()){
                System.out.println(name);
            }
            System.out.println("test success");
        } else {
            System.out.println("Test fails");
        }
    }
}
