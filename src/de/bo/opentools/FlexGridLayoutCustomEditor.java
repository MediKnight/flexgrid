package de.bo.opentools;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.border.*;

import de.bo.swing.*;
import com.borland.jbcl.layout.*;

public class FlexGridLayoutCustomEditor extends JPanel {
    FlexGridConstraints constraints;

    ButtonGroup xButtonGroup = new ButtonGroup();
    ButtonGroup yButtonGroup = new ButtonGroup();
    ButtonGroup alignButtonGroup = new ButtonGroup();
    TitledBorder titledBorder1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    Component component1;
    Component component2;
    GridLayout gridLayout1 = new GridLayout();
    JPanel xyPanel = new JPanel();
    JPanel xPanel = new JPanel();
    JPanel xSizePanel = new JPanel();
    JPanel yPanel = new JPanel();
    JPanel ySizePanel = new JPanel();
    JRadioButton xPreferredRB = new JRadioButton();
    JRadioButton xFillRB = new JRadioButton();
    JRadioButton xExplicitRB = new JRadioButton();
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    JTextField xExplicitTF = new JTextField();
    BorderLayout borderLayout2 = new BorderLayout();
    JTextField yExplicitTF = new JTextField();
    JRadioButton yExplicitRB = new JRadioButton();
    JRadioButton yPreferredRB = new JRadioButton();
    JRadioButton yFillRB = new JRadioButton();
    BorderLayout borderLayout5 = new BorderLayout();
    VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
    VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
    JPanel alignmentPanel = new JPanel();
    GridLayout gridLayout2 = new GridLayout();
    JRadioButton nwRB = new JRadioButton();
    JRadioButton eRB = new JRadioButton();
    JRadioButton swRB = new JRadioButton();
    JRadioButton nRB = new JRadioButton();
    JRadioButton cRB = new JRadioButton();
    JRadioButton wRB = new JRadioButton();
    JRadioButton neRB = new JRadioButton();
    JRadioButton sRB = new JRadioButton();
    JRadioButton seRB = new JRadioButton();

    JRadioButton[] alignButtons = new JRadioButton[] {
        cRB, nRB, neRB, eRB, seRB, sRB, swRB, wRB, nwRB
    };


    public FlexGridLayoutCustomEditor( FlexGridConstraints constraints ) {
        this.constraints = constraints;
        xExplicitTF.setEnabled( false );
        yExplicitTF.setEnabled( false );
        update( this.constraints );

        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        xExplicitRB.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                xExplicitTF.setEnabled( xExplicitRB.isSelected() );
            }
        });

        yExplicitRB.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                yExplicitTF.setEnabled( yExplicitRB.isSelected() );
            }
        });
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(BorderFactory.createTitledBorder("X Constraints"));
        titledBorder2 = new TitledBorder(BorderFactory.createTitledBorder("Y Constraints"));
        titledBorder3 = new TitledBorder(BorderFactory.createTitledBorder("Alignment"));
        component1 = Box.createHorizontalStrut(16);
        component2 = Box.createHorizontalStrut(16);
        this.setLayout(verticalFlowLayout3);
        xyPanel.setLayout(gridLayout1);
        gridLayout1.setColumns(2);
        xPanel.setBorder(titledBorder1);
        xPanel.setLayout(verticalFlowLayout1);
        yPanel.setBorder(titledBorder2);
        yPanel.setLayout(verticalFlowLayout2);
        xPreferredRB.setText("Preferred size");
        xFillRB.setText("Fill space");
        xExplicitRB.setText("Specify size units");
        xSizePanel.setLayout(borderLayout2);
        ySizePanel.setLayout(borderLayout5);
        yExplicitRB.setText("Specify size units");
        yPreferredRB.setText("Preferred size");
        yFillRB.setText("Fill space");
        alignmentPanel.setBorder(titledBorder3);
        alignmentPanel.setLayout(gridLayout2);
        gridLayout2.setRows(3);
        gridLayout2.setColumns(3);
        nwRB.setText("NW");
        eRB.setText("E");
        swRB.setText("SW");
        nRB.setText("N");
        cRB.setText("Center");
        wRB.setText("W");
        neRB.setText("NE");
        sRB.setText("S");
        seRB.setText("SE");
        this.add(xyPanel, null);
        xyPanel.add(xPanel, null);
        xPanel.add(xPreferredRB, null);
        xPanel.add(xFillRB, null);
        xPanel.add(xExplicitRB, null);
        xPanel.add(xSizePanel, null);
        xButtonGroup.add(xPreferredRB);
        xButtonGroup.add(xFillRB);
        xButtonGroup.add(xExplicitRB);
        xSizePanel.add(component1, BorderLayout.WEST);
        xSizePanel.add(xExplicitTF, BorderLayout.CENTER);
        xyPanel.add(yPanel, null);
        yPanel.add(yPreferredRB, null);
        yPanel.add(yFillRB, null);
        yPanel.add(yExplicitRB, null);
        yPanel.add(ySizePanel, null);
        yButtonGroup.add(yPreferredRB);
        yButtonGroup.add(yFillRB);
        yButtonGroup.add(yExplicitRB);
        ySizePanel.add(component2, BorderLayout.WEST);
        ySizePanel.add(yExplicitTF, BorderLayout.CENTER);
        this.add(alignmentPanel, null);
        alignmentPanel.add(nwRB, null);
        alignmentPanel.add(nRB, null);
        alignmentPanel.add(neRB, null);
        alignmentPanel.add(wRB, null);
        alignmentPanel.add(cRB, null);
        alignmentPanel.add(eRB, null);
        alignmentPanel.add(swRB, null);
        alignmentPanel.add(sRB, null);
        alignmentPanel.add(seRB, null);
        alignButtonGroup.add(nwRB);
        alignButtonGroup.add(nRB);
        alignButtonGroup.add(neRB);
        alignButtonGroup.add(eRB);
        alignButtonGroup.add(cRB);
        alignButtonGroup.add(wRB);
        alignButtonGroup.add(swRB);
        alignButtonGroup.add(sRB);
        alignButtonGroup.add(seRB);
    }

    int alignIndexFor( int align ) {
        for( int i = 0; i < FlexGridLayoutConstraintEditor.ALIGNMENTS.length; i++ ) {
            if( FlexGridLayoutConstraintEditor.ALIGNMENTS[i] == align )
                return i;
        }

        return FlexGridLayoutConstraintEditor.ALIGNMENTS[0]; // centered.
    }

    public void update( FlexGridConstraints o ) {
        if( o == null ) {
            constraints = new FlexGridConstraints( "2,2,NE" );
        } else {
            constraints = o;
        }

        alignButtons[FlexGridLayoutConstraintEditor.getAlignIndex(constraints.align)].setSelected( true );

        switch( constraints.x ) {
            case 0:
                xPreferredRB.setSelected( true );
                break;
            case -1:
                xFillRB.setSelected( true );
                break;
            default:
                xExplicitRB.setSelected( true );
                xExplicitTF.setText( String.valueOf( constraints.x ) );
                break;
        }

        switch( constraints.y ) {
            case 0:
                yPreferredRB.setSelected( true );
                break;
            case -1:
                yFillRB.setSelected( true );
                break;
            default:
                yExplicitRB.setSelected( true );
                yExplicitTF.setText( String.valueOf( constraints.y ) );
                break;
        }
    }


    public Object getValue() {
        if( xPreferredRB.isSelected() ) {
            constraints.x = FlexGridConstraints.PREFERRED;
        } else if( xFillRB.isSelected() ) {
            constraints.x = FlexGridConstraints.FILL;
        } else {
            try {
                constraints.x = Integer.valueOf( xExplicitTF.getText() ).intValue();
            } catch( NumberFormatException e ) {
                constraints.x = FlexGridConstraints.PREFERRED;
            }
        }

        if( yPreferredRB.isSelected() ) {
            constraints.y = FlexGridConstraints.PREFERRED;
        } else if( yFillRB.isSelected() ) {
            constraints.y = FlexGridConstraints.FILL;
        } else {
            try {
                constraints.y = Integer.valueOf( yExplicitTF.getText() ).intValue();
            } catch( NumberFormatException e ) {
                constraints.y = FlexGridConstraints.PREFERRED;
            }
        }

        for( int i = 0; i < alignButtons.length; i++ ) {
            if( alignButtons[i].isSelected() ) {
                constraints.align = FlexGridLayoutConstraintEditor.ALIGNMENTS[i];
                break;
            }
        }

        return constraints;
    }


    /**
     *
     */
    public static void main( String[] args ) {
        JFrame f = new JFrame( "Custom Editor Test" );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize( 500, 400 );

        Container pane = f.getContentPane();
        pane.setLayout( new BorderLayout() );
        final FlexGridLayoutCustomEditor editor = new FlexGridLayoutCustomEditor( null );
        pane.add( editor, BorderLayout.CENTER );
        JButton testButton = new JButton( "Test" );
        pane.add( testButton, BorderLayout.SOUTH );
        testButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                System.err.println( "Value: " + editor.getValue() );
            }
        });

        f.show();
    }
}