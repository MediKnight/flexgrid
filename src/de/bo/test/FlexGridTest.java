package de.bo.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.bo.swing.*;

public class FlexGridTest extends JFrame {

    public FlexGridTest() {
        final Container pane = getContentPane();
        pane.setLayout( new FlexGridLayout( 0, 26, 5, 0 ) );

        for( int i = 'A'; i <= 'Z'; i++ ) {
            pane.add( new JButton( String.valueOf( (char) i ) ),
                      new FlexGridConstraints( -1, -1, FlexGridConstraints.C ) );
        }


        System.err.println("preferred: " + pane.getLayout().preferredLayoutSize( pane ) );

        JButton button = (JButton) pane.getComponent( 0 );
        button.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                int max = 0;
                for( int i = 0; i < pane.getComponentCount(); i++ ) {
                    System.err.println("component " + i + ": " + pane.getComponent( i ).getSize() );
                    max = Math.max( max, pane.getComponent(i).getPreferredSize().width );
                }

                System.err.println("bounds: " + getBounds() );
                System.err.println("max: " + max );
            }
        });

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
    }


    public static void main(String[] args) {
        new FlexGridTest().show();
    }
}