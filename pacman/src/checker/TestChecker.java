package checker;


public class TestChecker {

    public static void main(String[] args){
        GameChecker gameChecker = GameChecker.getInstance();
        if(gameChecker.checkGame()){
            System.out.println("test success");
        }
        else{
            System.out.println("Test fails");
        }
    }
}
