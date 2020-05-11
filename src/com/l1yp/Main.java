package com.l1yp;

import com.l1yp.frame.TreeForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        TreeForm treeForm = new TreeForm();
        // treeForm.pack();
        treeForm.setVisible(true);
    }

}
