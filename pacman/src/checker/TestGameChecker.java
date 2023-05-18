package checker;


public class TestGameChecker {

    public static void main(String[] args){
        GameChecker gameChecker = GameChecker.getInstance();
        String dir = "pacman/data/wrongSequenceMapFolder_GameCheckerTest";

        if(gameChecker.checkGame(dir)){
            System.out.println("test success");
        }
        else{
            System.out.println("Test fails");
        }
    }
}
