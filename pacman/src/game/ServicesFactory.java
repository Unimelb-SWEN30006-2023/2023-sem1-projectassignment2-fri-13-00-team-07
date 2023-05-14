package game;

import java.util.Properties;

public class ServicesFactory {
    private static ServicesFactory instance;
    public static ServicesFactory getInstance() {
        if (instance == null) {
            instance = new ServicesFactory();
        }
        return instance;
    }

    public SettingManager getSettingManager(PacManGameGrid grid) {
        if (grid instanceof PacManMap)
            return new MapSettingManager(grid);
        else
            return new PropertySettingManager(grid);
    }

    public PropertyReader getPropertyReader(Properties properties) {
        return new PropertyReader(properties);
    }
}
