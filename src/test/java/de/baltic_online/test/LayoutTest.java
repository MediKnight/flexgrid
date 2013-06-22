package de.baltic_online.test;

import java.awt.*;
import javax.swing.*;
import de.baltic_online.swing.*;

public class LayoutTest extends JFrame {
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    JButton jButton4 = new JButton();
    JButton jButton5 = new JButton();
    FlexGridLayout flexGridLayout1 = new FlexGridLayout();

    public LayoutTest() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        LayoutTest layoutTest = new LayoutTest();
    }
    private void jbInit() throws Exception {
        jButton1.setText("jButton1");
        this.getContentPane().setLayout(flexGridLayout1);
        jButton2.setText("jButton2");
        jButton3.setText("jButton3");
        jButton4.setText("jButton4");
        jButton5.setText("jButton5");
        flexGridLayout1.setRows(0);
        flexGridLayout1.setColumns(4);
        flexGridLayout1.setHgap(10);
        flexGridLayout1.setVgap(10);
        this.getContentPane().add(jButton4, new FlexGridConstraints(0, 0, FlexGridConstraints.S));
        this.getContentPane().add(jButton5, new FlexGridConstraints(0, -1, FlexGridConstraints.C));
        this.getContentPane().add(jButton3, new FlexGridConstraints(0, 0, FlexGridConstraints.S));
        this.getContentPane().add(jButton1, new FlexGridConstraints(0, 0, FlexGridConstraints.C));
        this.getContentPane().add(jButton2, new FlexGridConstraints(0, 0, FlexGridConstraints.C));
    }
}