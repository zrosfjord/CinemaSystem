package com.zrosfjord.cs.gui.panes;

import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.files.SaveState;
import com.zrosfjord.cs.gui.CinemaSystemGUI;
import com.zrosfjord.cs.gui.displays.ItemDisplay;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TheatersPane {

    private CinemaSystemGUI gui;
    private SaveState saveState;

    private JPanel panel;
    private JScrollPane scrollPane;

    public TheatersPane(CinemaSystemGUI gui, final SaveState saveState) {
        this.gui = gui;
        this.saveState = saveState;
    }

    private void createUIComponents() {
        JPanel theatersPanel = new JPanel();
        theatersPanel.setLayout(new BoxLayout(theatersPanel, BoxLayout.Y_AXIS));
        theatersPanel.setBackground(Color.WHITE);
        theatersPanel.add(Box.createVerticalStrut(10));

        Color green = new Color(76, 220, 156);

        for (Theater theater : saveState.getCompany().getTheaters()) {
            ItemDisplay item = new ItemDisplay(theater.getName(), theater.getLocation(), green);
            JPanel iPanel = item.getPanel();

            item.getSelectBtn().addActionListener(a -> {
                gui.update(CinemaSystemGUI.PaneState.MOVIES, saveState, theater);
            });

            item.getDelButton().setText("Modify");

            item.getDelButton().addActionListener(a -> {
                // TODO add screen scheduling pane
            });

            theatersPanel.add(iPanel);
            theatersPanel.add(Box.createVerticalStrut(10));
        }

        JButton addBtn = new JButton(" + ");
        addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        addBtn.setBorder(BorderFactory.createLineBorder(green, 3));
        addBtn.setBackground(Color.WHITE);

        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        addBtn.addActionListener(a -> {
            String name = JOptionPane.showInputDialog("Theater Name:");
            String address = JOptionPane.showInputDialog("Address:");

            if (name == null || address == null)
                return;

            saveState.getCompany().createNewTheater(name, address);
            try {
                saveState.recordState();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            gui.update(CinemaSystemGUI.PaneState.THEATERS, saveState);
        });

        theatersPanel.add(addBtn);
        scrollPane = new JScrollPane(theatersPanel);
    }

    public JPanel getPanel() {
        return panel;
    }

}
