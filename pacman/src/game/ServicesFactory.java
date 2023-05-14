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

    public SettingManager getSettingManager(boolean useEditor) {
        return new SettingManager(useEditor);
    }

    public PropertyReader getPropertyReader(Properties properties) {
        return new PropertyReader(properties);
    }
}
