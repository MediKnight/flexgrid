package de.bo.opentools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.beans.*;

import com.borland.jbuilder.cmt.*;
import com.borland.jbuilder.designer.*;
import com.borland.jbuilder.designer.ui.*;
import com.borland.jbuilder.designer.ui.opt.*;
import com.borland.primetime.actions.*;
import com.borland.primetime.ide.*;

import de.bo.swing.*;


public class FlexGridLayoutAssistant extends BasicLayoutAssistant implements ActionListener {

    public static UpdateAction ACTION_Constraints =
        new ConstraintsAction( "Constraints...", 'o',
                               "Edit constraints for selected items", null );
    public static UpdateAction ACTION_PreferredSize =
        new ConstraintsOperationAction( "Preferred size", 'r',
                                        "Layout out a preferred size", null,
                                        new PreferredSizeOperation() );

    /**
     * A reference to the design view in use. It is needed in the event handler
     * for the constraints dialog.
     */
    DesignView designView;

    /**
     * PropertyEditor for <code>FlexGridConstraints</code>
     */
    FlexGridLayoutConstraintEditor editor;


    public static void initOpenTool(byte majorVersion, byte minorVersion) {
        System.out.println( "Initializing FlexGridLayout-Assistant..." );

        UIDesigner.registerAssistant( FlexGridLayoutAssistant.class,
            "de.bo.swing.FlexGridLayout", true );
    }


    public PropertyEditor getPropertyEditor() {
        if( editor == null )
            editor = new FlexGridLayoutConstraintEditor();

        return editor;
    }


    public void prepareSelectComponent( ModelNode node, DesignView view ) {
        // Store a reference to the design viewer because we need in the
        // event handler for the constraints editor.
        designView = view;

        super.prepareSelectComponent( node, view );
    }


    public void prepareAddComponent( ModelNode node, Point p, Dimension dim ) {
        node.getModel().getComponentSource().getSourceFile().addImport( "de.bo.swing.*" );

        // Set the initial constraints to: preferred width, preferred height, centered.
        ((NodeAddCall) node.getConstraints()).setValue( new FlexGridConstraints( "C" ) );

        // The Z ordering is done here.
        super.prepareAddComponent( node, p, dim );
    }


    public String prepareMoveStatus( ModelNode node, ModelNode parent,
                                     Point location, SelectBoxes selectBoxes,
                                     Point mouseOffset ) {
        Container container = parent.getSubcomponent().getAsContainer();
        Insets i = container.getInsets();

        Rectangle zBounds = container.getBounds();
        zBounds.x = i.left;
        zBounds.y = i.top;
        zBounds.width -= i.left + i.right;
        zBounds.height -= i.top + i.bottom;

        // Warning: zBounds is modified through calcBestZ! It is not equivalent
        // to calculate the z order string inside the return statement at the
        // end of this method.
        String s = parent + " z: " + calcBestZ( node, parent, location, null, zBounds );

        Point nodeLocation = DesignView.componentAbsLocation( container );
        nodeLocation.x += zBounds.x;
        nodeLocation.y += zBounds.y;

        selectBoxes.show( 0, nodeLocation, new Dimension( zBounds.width, zBounds.height ), 2);
        return s;
    }


    public String getConstraintsType() {
        return "de.bo.swing.FlexGridConstraints";
    }


    public DesignView getDesignView() {
        return designView;
    }


    public void prepareActionGroup( ActionGroup aGroup ) {
        super.prepareActionGroup( aGroup );

        //
        // Register actions for modifying constraints.
        //
        ActionGroup group = new ActionGroup();
        group.add( ACTION_Constraints );
        group.add( ACTION_PreferredSize );

        aGroup.add( group );
    }


    /**
     * Update or create a constraints dialog.
     */
    public void editConstraints( TreePath[] path, ModelNode parent ) {
        CmtModelNode cmtNode = (CmtModelNode) path[0].getLastPathComponent();
        ModelNode node = (ModelNode) cmtNode;
        FlexGridConstraints constraints = (FlexGridConstraints)
            ((FlexGridConstraints) node.getConstraints().getValue()).clone();

        PropertyEditor editor = getPropertyEditor();
        editor.setValue( constraints );
        FlexGridLayoutCustomEditor customEditor =
            (FlexGridLayoutCustomEditor) editor.getCustomEditor();

        if( DesignView.isConstraintEditorShowing() ) {
            // The constraint editor is already showing. Just register as a
            // listener and return.
            DesignView.constraintDialog.addActionListener( this );
            return;
        }

        ButtonDialog dialog =
            new ButtonDialog( Browser.getActiveBrowser(), customEditor.toString(),
                              false, customEditor, null, new ButtonDescriptor[0] );

        dialog.setButtonSet( ButtonDialog.OK_CANCEL_APPLY );
        dialog.setCentered( true );  // Hmmm, does not really work.
        dialog.pack();
        dialog.addActionListener( this );
        DesignView.constraintDialog = dialog;
        dialog.show();
        dialog.toFront();
    }


    /**
     * React to selection changes in the designer. We have to remove ourselves
     * as the action listener of the constraint editor. We are re-registered
     * through a editConstraints method call.
     */
    public void constraintEditorSelectionChanging( ModelNode node ) {
        DesignView.constraintDialog.removeActionListener( this );
    }


    /**
     * React to changes of a non-modal constraint editor usually invoked through
     * context actions.
     */
    public void actionPerformed( ActionEvent e ) {
        String command = e.getActionCommand();

        if( command.equals( "apply" ) || command.equals( "ok" ) ) {
            TreePath[] path =
                getDesignView().getDesignerViewer().getSelection().getSelectionPaths();

            CmtModelNode cmtNode = (CmtModelNode) path[0].getLastPathComponent();
            ModelNode node = (ModelNode) cmtNode;
            ModelNode parent = (ModelNode) node.getParent();
            FlexGridConstraints constraints =
                (FlexGridConstraints) ((FlexGridConstraints) editor.getValue()).clone();

            // Update the selected component's constraints.
            ((NodeAddCall) node.getConstraints()).setValue( constraints );

            // Update live components, i.e. change the layout constraints
            // of the selected component.
            FlexGridLayout layout = (FlexGridLayout) parent.getLiveLayout();
            layout.setConstraints( node.getLiveComponent(), constraints );
            parent.getLiveComponent().validate();

            // Tell the Designer that values have changed.
            getDesignView().getDesignerViewer().commit();
        }
    }
}


class ConstraintsAction extends UpdateAction {
    public ConstraintsAction( String name, char accelerator, String description, Icon icon ) {
        super( name, accelerator, description, icon );
    }

    public void actionPerformed( ActionEvent e ) {
        ContextActionAdapter adapter = (ContextActionAdapter) e.getSource();
        CmtModelNode cmtModelNode =
            (CmtModelNode) ((TreePath) adapter.subSelection.get(0)).getLastPathComponent();
        ModelNode node = (ModelNode) cmtModelNode.getParent();
        FlexGridLayoutAssistant assistant = (FlexGridLayoutAssistant) adapter.assistant;
        assistant.editConstraints(
            assistant.getDesignView().getDesignerViewer().getSelection().getSelectionPaths(),
            node );
    }
}


class ConstraintsOperationAction extends UpdateAction {
    ConstraintsOperation operation;

    public ConstraintsOperationAction( String name, char accelerator, String description,
                                       Icon icon, ConstraintsOperation operation ) {
        super( name, accelerator, description, icon );
        this.operation = operation;
    }

    public void actionPerformed( ActionEvent e ) {
        ContextActionAdapter adapter = (ContextActionAdapter) e.getSource();

        for( int i = 0; i < adapter.subSelection.size(); i++ ) {
            ModelNode node =
                (ModelNode) ((TreePath) adapter.subSelection.get(i)).getLastPathComponent();

            operation.processConstraints( node );
        }

        // Notify designer viewer about changes.
        adapter.view.getDesignerViewer().commit();
    }
}


abstract class ConstraintsOperation {
    public FlexGridConstraints cloneConstraints( ModelNode node ) {
        FlexGridConstraints c = (FlexGridConstraints) node.getConstraints().getValue();
        return (FlexGridConstraints) c.clone();
    }

    public void setConstraints( ModelNode node, FlexGridConstraints c ) {
        ((NodeAddCall) node.getConstraints()).setValue(c);
    }

    public abstract void processConstraints( ModelNode node );
}


class PreferredSizeOperation extends ConstraintsOperation {
    public void processConstraints( ModelNode node ) {
        FlexGridConstraints c = cloneConstraints( node );
        c.x = FlexGridConstraints.PREFERRED;
        c.y = FlexGridConstraints.PREFERRED;
        setConstraints( node, c );
    }
}