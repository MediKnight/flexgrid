package de.bo.opentools;

import java.beans.*;
import java.awt.*;
import de.bo.swing.*;

public class FlexGridLayoutConstraintEditor implements PropertyEditor {
    FlexGridConstraints constraints;
    FlexGridLayoutCustomEditor editor;
    PropertyChangeListener listener;

    public static final int[] ALIGNMENTS = {
        FlexGridConstraints.C,
        FlexGridConstraints.N,
        FlexGridConstraints.NE,
        FlexGridConstraints.E,
        FlexGridConstraints.SE,
        FlexGridConstraints.S,
        FlexGridConstraints.SW,
        FlexGridConstraints.W,
        FlexGridConstraints.NW
    };

    public static final String[] ALIGNMENTS_CODE = {
        "FlexGridConstraints.C",
        "FlexGridConstraints.N",
        "FlexGridConstraints.NE",
        "FlexGridConstraints.E",
        "FlexGridConstraints.SE",
        "FlexGridConstraints.S",
        "FlexGridConstraints.SW",
        "FlexGridConstraints.W",
        "FlexGridConstraints.NW"
    };

    public static int getAlignIndex( int align ) {
        for( int i = 0; i < ALIGNMENTS.length; i++ ) {
            if( ALIGNMENTS[i] == align )
                return i;
        }

        return 0;
    }


    public void addPropertyChangeListener( PropertyChangeListener l ) {
        System.err.println("We have a property change listener!");
        listener = l;
    }

    public void removePropertyChangeListener( PropertyChangeListener l ) {
        System.err.println("We lost our property change listener!");
        listener = null;
    }

    public boolean supportsCustomEditor() { return true; }

    public Component getCustomEditor() {
        if( editor == null ) {
            editor = new FlexGridLayoutCustomEditor( constraints );
        } else {
            editor.update( constraints );
        }

        return editor;
    }


    public void setAsText( String s ) {
        constraints = new FlexGridConstraints( s );
    }

    public String getAsText() {
        if( constraints == null ) {
            return null;
        } else {
            return constraints.toString();
        }
    }


    public String getJavaInitializationString() {
        System.err.println( "ConstraintEditor: getJavaInitializationString" );

        if( constraints != null ) {
            return "new FlexGridConstraints(" +
                constraints.x + ", " + constraints.y + ", " +
                ALIGNMENTS_CODE[getAlignIndex(constraints.align)] + ")";
        } else {
            return null;
        }
    }


    public Object getValue() {
        // If the editor is open, fetch the constraints from it.
        if( editor != null )
            constraints = (FlexGridConstraints) editor.getValue();

        //
        // It is necessary to return a clone of the constraints because
        // otherwise the JBuilder code generation won't be triggered.
        // A suitable equals() method in the constraints class should do the
        // same.
        //
        return constraints.clone();
    }

    public void setValue( Object o ) {
        if( o == null ) {
            constraints = new FlexGridConstraints( 0, 0, FlexGridConstraints.C );
        } else {
            constraints = (FlexGridConstraints) o;
        }

        // If an editor is in place, notify it about the changed constraints.
        if( editor != null ) {
            editor.update( constraints );
        }
    }

    public String[] getTags() { return null; }
    public boolean isPaintable() { return false; }
    public void paintValue( Graphics g, Rectangle r ) {}
}