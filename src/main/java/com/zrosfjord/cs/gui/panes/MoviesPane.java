package com.zrosfjord.cs.gui.panes;

import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.files.SaveState;
import com.zrosfjord.cs.gui.CinemaSystemGUI;
import com.zrosfjord.cs.gui.displays.MovieDisplay;
import com.zrosfjord.cs.search.Search;
import com.zrosfjord.cs.search.SearchTermException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MoviesPane {

    private JPanel panel;
    private JScrollPane scrollPane;

    private CinemaSystemGUI gui;
    private SaveState saveState;
    private Theater theater;

    public MoviesPane(CinemaSystemGUI gui, SaveState saveState, Theater theater) {
        this.gui = gui;
        this.saveState = saveState;
        this.theater = theater;
    }

    private void createUIComponents() {
        JPanel moviesPanel = new JPanel();
        moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS));
        moviesPanel.setBackground(Color.WHITE);
        moviesPanel.add(Box.createVerticalStrut(10));

        for (Movie movie : saveState.getRegistry()) {
            List<Search.Result> results = null;
            try {
                results = theater.search("movie{name:" + movie.getName() + "}");
            } catch (SearchTermException e) {
                e.printStackTrace();
            }

            MovieDisplay movieDisplay = new MovieDisplay(movie, results, new Dimension(CinemaSystemGUI.WIDTH * 2 / 3, 100));

            moviesPanel.add(movieDisplay.getPanel());
            moviesPanel.add(Box.createVerticalStrut(10));
        }

        scrollPane = new JScrollPane(moviesPanel);
    }

    public JPanel getPanel() {
        return panel;
    }
}
