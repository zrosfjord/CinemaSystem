import com.zrosfjord.cs.gui.CinemaSystemGUI;

import javax.swing.*;

public class GUITester {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CinemaSystemGUI().createAndShow();
            }
        });
    }

}
