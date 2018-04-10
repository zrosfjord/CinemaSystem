package com.zrosfjord.cs.gui.displays;

import com.zrosfjord.cs.gui.CinemaSystemGUI;

import javax.swing.*;
import java.awt.*;

public class ItemDisplay {

    private JPanel panel;

    private JButton delButton;
    private JButton selectBtn;
    private JLabel titleLbl;
    private JLabel infoLbl;

    public ItemDisplay(String title, String info, Color c) {
        this(title, info, c, new Dimension(CinemaSystemGUI.WIDTH * 2 / 3, 100));
    }

    public ItemDisplay(String title, String info, Color c, Dimension d) {
        titleLbl.setText(title);
        infoLbl.setText(info);

        panel.setPreferredSize(d);
        panel.setMinimumSize(d);
        panel.setMaximumSize(d);

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createMatteBorder(5, 12, 5, 5, c));
    }

    public JButton getDelButton() {
        return delButton;
    }

    public JButton getSelectBtn() {
        return selectBtn;
    }

    public JPanel getPanel() {
        return panel;
    }
}
