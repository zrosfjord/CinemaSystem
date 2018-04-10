package com.zrosfjord.cs.gui.displays;

import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.search.Search;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MovieDisplay {

    private JPanel panel;
    private JLabel titleLbl;
    private JLabel formatLbl;
    private JLabel ratingLbl;
    private JLabel showLbl;
    private JPanel showPanel;

    public MovieDisplay(Movie m, Dimension d) {
        this(m, null, d);
    }

    public MovieDisplay(Movie m, List<Search.Result> results, Dimension d) {
        titleLbl.setText(m.getName());
        formatLbl.setText("Format: " + m.getFormat());
        ratingLbl.setText("Rating: " + m.getRating());

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.setMaximumSize(d);
        panel.setMinimumSize(d);
        panel.setMaximumSize(d);

        Color c = new Color(122, 192, 216);
        panel.setBorder(BorderFactory.createMatteBorder(5, 12, 5, 5, c));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String str = results.stream().map(e -> e.getItem().getStartTime().format(dtf)).collect(Collectors.joining(", "));
        showLbl.setText(str);
    }

    public JPanel getPanel() {
        return panel;
    }

    public JPanel getShowPanel() {
        return showPanel;
    }
}
