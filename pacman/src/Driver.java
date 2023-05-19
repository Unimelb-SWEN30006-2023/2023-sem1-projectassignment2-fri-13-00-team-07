import javax.swing.*;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test2.properties";

    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        try {
            if (args.length > 0) {
                new TorusVerseApp(args[0]);
            } else {
                new TorusVerseApp(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Cannot run", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
