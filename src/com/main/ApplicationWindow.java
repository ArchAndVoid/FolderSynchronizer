package com.main;

import javax.swing.*;
import java.io.File;

public class ApplicationWindow {
    private JButton runButton;
    private JPanel main;
    private JButton selectInitialFolderButton;
    private JTextArea console;
    private JButton selectTargetFolderButton;
    private JCheckBox checkBoxReplaceUnequal;
    private String initialPath;
    private String targetPath;

    public ApplicationWindow(final JFrame frame) {
        selectInitialFolderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                initialPath = file.getAbsolutePath();
                console.append("\nInitial Folder Selected: " + initialPath);
            }else{
                console.append("\nOpen command canceled");
            }
        });

        selectTargetFolderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                targetPath = file.getAbsolutePath();
                console.append("\nFolder Selected: " + targetPath);
            }else{
                console.append("\nOpen command canceled");
            }
        });

        runButton.addActionListener(e -> {
            if (targetPath == null || initialPath == null){
                console.append("\nBoth paths must be selected");
            }
            if (targetPath.equals(initialPath)){
                console.append("\nTarget and initial path must not be equal");
            }

            FolderSync sync = new FolderSync();
            try {
                console.append("\nFiles synced: "  +
                        sync.syncFolder(initialPath, targetPath, checkBoxReplaceUnequal.isSelected()));
            } catch (NullPointerException nullPointerException) {
                console.append("\nPlease select folders");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RecurrentSynchronizer");
        frame.setContentPane(new ApplicationWindow(frame).main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(560, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
