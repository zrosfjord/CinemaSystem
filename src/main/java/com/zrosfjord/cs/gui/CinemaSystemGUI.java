package com.zrosfjord.cs.gui;

import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.files.SaveState;
import com.zrosfjord.cs.gui.panes.CompaniesPane;
import com.zrosfjord.cs.gui.panes.MoviesPane;
import com.zrosfjord.cs.gui.panes.TheatersPane;

import javax.swing.*;
import java.awt.*;

public class CinemaSystemGUI {

    public static final int WIDTH = 1000, HEIGHT = 600;

    private JFrame frame;
    private JPanel panel;

    private PaneState state;

    public CinemaSystemGUI() {
        frame = new JFrame("Movie GUI");
        panel = new JPanel();

        state = PaneState.COMPANIES;
    }

    public void createAndShow() {
        Dimension d = new Dimension(WIDTH, HEIGHT);
        frame.setMinimumSize(d);
        frame.setPreferredSize(d);
        frame.setMaximumSize(d);

        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        update(state);
    }

    public void update(PaneState s, Object... o) {
        state = s;

        frame.remove(panel);

        switch (state) {
            case COMPANIES:
                panel = new CompaniesPane(this).getPanel();
                break;
            case THEATERS:
                panel = new TheatersPane(this, (SaveState) o[0]).getPanel();
                break;
            case MOVIES:
                panel = new MoviesPane(this, (SaveState) o[0], (Theater) o[1]).getPanel();
                break;
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public PaneState getPaneState() {
        return state;
    }

    public enum PaneState {
        COMPANIES, THEATERS, MOVIES, LOGIN
    }

}
