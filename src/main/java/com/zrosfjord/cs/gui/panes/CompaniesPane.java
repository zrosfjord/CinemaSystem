package com.zrosfjord.cs.gui.panes;

import com.zrosfjord.cs.Company;
import com.zrosfjord.cs.files.SaveState;
import com.zrosfjord.cs.gui.CinemaSystemGUI;
import com.zrosfjord.cs.gui.displays.ItemDisplay;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CompaniesPane {

    private CinemaSystemGUI gui;

    private JPanel panel;
    private JScrollPane scrollPane;

    private Popup addPopup, delPopup;

    public CompaniesPane(CinemaSystemGUI gui) {
        this.gui = gui;
    }

    private void createUIComponents() {
        JPanel companiesPanel = new JPanel();
        companiesPanel.setLayout(new BoxLayout(companiesPanel, BoxLayout.Y_AXIS));
        companiesPanel.setBackground(Color.WHITE);
        companiesPanel.add(Box.createVerticalStrut(10));

        Color purple = new Color(150, 67, 138);

        for (String com : SaveState.getSaves()) {
            String name = com.replaceFirst("\\.cts", "");

            ItemDisplay item = new ItemDisplay(name, "", purple);
            JPanel iPanel = item.getPanel();

            item.getSelectBtn().addActionListener(a -> {
                SaveState ss = null;

                try {
                    ss = SaveState.getState(name);
                    ss.getCompany().getWatcher().start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                gui.update(CinemaSystemGUI.PaneState.THEATERS, ss);
            });

            item.getDelButton().addActionListener(a -> {
                SaveState.delete(name);
                gui.update(CinemaSystemGUI.PaneState.COMPANIES);
            });

            companiesPanel.add(iPanel);
            companiesPanel.add(Box.createVerticalStrut(10));
        }

        JButton addBtn = new JButton(" + ");
        addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        addBtn.setBorder(BorderFactory.createLineBorder(purple, 3));
        addBtn.setBackground(Color.WHITE);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        addBtn.addActionListener(a -> {
            String name = JOptionPane.showInputDialog("New Company Name:");

            Company c = new Company(name);

            SaveState s = new SaveState(c);
            try {
                s.recordState();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            gui.update(CinemaSystemGUI.PaneState.COMPANIES);
        });

        companiesPanel.add(addBtn);
        companiesPanel.add(Box.createVerticalStrut(10));

        scrollPane = new JScrollPane(companiesPanel);
    }

    public JPanel getPanel() {
        return panel;
    }

}
