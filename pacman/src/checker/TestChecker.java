package checker;


public class TestChecker {

    public static void main(String[] args){
        GameChecker gameChecker = GameChecker.getInstance();
        if(GameChecker.checkGame()){
            System.out.println("test success");
        }
        else{
            System.out.println("Test fails");
        }
    }
}
