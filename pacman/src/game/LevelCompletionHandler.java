package game;

import java.lang.ref.WeakReference;

public interface LevelCompletionHandler {

    Game getGame();

    void hander(Game game);

}
