/*
 * @(#)$Id$
 *
 * (C)2000 Baltic Online Computer GmbH
 */
package de.baltic_online.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.*;

/**
 * A simple tiny layout manager aligning components in a grid with the
 * following constraints
 * <pre>x, y, align</pre>
 * where x denots horizontal layout, y vertical layout and align the
 * components's alignment within the grid cell. The x and y values are integers.
 * -1 means space filling, 0 means the component's preferred size and a number
 * larger then 0 is a logical size based on the screen resolution.
 * The alignment can be C, N, E, W, S, NW... for the center of the cell, the
 * upper, left, right, lower edge and so on.
 *
 * @author sma@baltic-online.de
 * @version 1.0
 * @see LayoutManager2
 * @see LayoutManager
 */
public class FlexGridLayout implements LayoutManager2 {

    /**
     * Number of grid rows. Can be zero; then, the number is calculated
     * based on the number of columns.
     */
    private int rows;

    /**
     * Number of grid columns. Can be zero; then, the number is calculated
     * based on the number of rows.
     */
    private int cols;

    /**
     * Gap between the columns.
     */
    private int hgap;

    /**
     * Gap between the rows.
     */
    private int vgap;

    private Hashtable constraints = new Hashtable(10);

    /**
     * Create a new <code>FlexGridLayout</code> with one row and an arbitrary
     * number of columns, setting both <code>hgap</code> and <code>vgap</code>
     * to 0.
     */
    public FlexGridLayout() {
        this(1, 0, 0, 0);
    }

    /**
     * Create a new <code>FlexGridLayout</code> with the specified number of
     * rows and columns, setting both <code>hgap</code> and <code>vgap</code>
     * to 0.
     *
     * @param rows the number of rows for the new <code>FlexGridLayout</code>
     * @param cols the number of columns for the new <code>FlexGridLayout</code>
     */
    public FlexGridLayout(int rows, int cols) {
        this(rows, cols, 0, 0);
    }

    /**
     * Create a new <code>FlexGridLayout</code> with the specified number of
     * rows and columns and the specified values for <code>hgap</code> and
     * <code>hgap</code>
     *
     * @param rows the number of rows for the new <code>FlexGridLayout</code>
     * @param cols the number of columns for the new <code>FlexGridLayout</code>
     * @param hgap the horizontal gap for the new <code>FlexGridLayout</code>
     * @param vgap the vertical gap for the new <code>FlexGridLayout</code>
     */
    public FlexGridLayout(int rows, int cols, int hgap, int vgap) {
        if (rows == 0 && cols == 0)
    	    throw new IllegalArgumentException("rows and cols cannot both be zero");

        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /**
     * Sets the number of rows in this layout to the specified value.
     *
     * @param rows  the number of rows in this layout.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Returns the number of grid rows. Can be zero.
     */
    public int getRows() {
        return rows;
    }


    /**
     * Sets the number of columns in this layout to the specified value.
     *
     * @param rows  the number of columns in this layout.
     */
    public void setColumns(int columns) {
        this.cols = columns;
    }

    /**
     * Returns the number of grid columns. Can be zero.
     */
    public int getColumns() {
        return cols;
    }


    /**
     *
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Returns the gap between two columns.
     */
    public int getHgap() {
        return hgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
     * Returns the gap between two rows.
     */
    public int getVgap() {
        return vgap;
    }


    /**
     *
     */
    public void setConstraints( Component comp, FlexGridConstraints c ) {
        constraints.put( comp, c );
    }


    /**
     * Adds the specified component to the layout, using the specified
     * constraint object. Ignored by this layout manager.
     *
     * @param name the component name
     * @param comp the component to be added
     *
     * @see LayoutManager#addLayoutComponent
     */
    public void addLayoutComponent(String name, Component comp) {
        // ignored
    }

    /**
     * Remove the specified component from the layout.
     *
     * @param comp the component to be removed
     *
     * @see LayoutManager#removeLayoutComponent
     */
    public void removeLayoutComponent(Component comp) {
        // for removed components, we can remove the associated constraint
        constraints.remove(comp);
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     *
     * @param comp the component to be added
     * @param constraint where/how the component is added to the layout.
     *
     * @see LayoutManager2#addLayoutComponent
     */
    public void addLayoutComponent(Component comp, Object constraint) {
        // associate the constraint with specified component
        if (!(constraint instanceof FlexGridConstraints))
            constraint = new FlexGridConstraints((String)constraint);
        constraints.put(comp, constraint);
    }

    /**
     * Invalidates the layout, indicating the if the layout manager has cached
     * information, it should be discarded.
     *
     * @param target the target container
     *
     * @see LayoutManager2#invalidateLayout
     */
    public void invalidateLayout(Container target) {
        // ignored - we don't have any cache to clear
    }

    /**
     * Lays out the container in the specified panel.
     *
     * @param target the component which needs to be laid out
     *
     * @see LayoutManager#layoutContainer
     */
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            int ncomponents = target.getComponentCount();
            if (ncomponents == 0)
                return;

            int widths[] = new int[ncomponents];
            int heights[] = new int[ncomponents];
            int nrows = rows;
            int ncols = cols;

            if (nrows > 0)
	        ncols = (ncomponents + nrows - 1) / nrows;
	    else
	        nrows = (ncomponents + ncols - 1) / ncols;

            // first pass - get the size of non-spacefilling components
            for (int i = 0; i < ncomponents; i++) {
                Component c = target.getComponent(i);
                if (!c.isVisible())
                    continue;

                FlexGridConstraints blc = (FlexGridConstraints) constraints.get(c);
                if (blc == null || blc.x == 0)
                    widths[i] = c.getPreferredSize().width;
                else if (blc.x > 0)
                    widths[i] = blc.x;
                else
                    widths[i] = -1;

                if (blc == null || blc.y == 0)
                    heights[i] = c.getPreferredSize().height;
                else if (blc.y > 0)
                    heights[i] = blc.y;
                else
                    heights[i] = -1;
            }

            Insets insets = target.getInsets();
            Dimension dt = target.getSize();
            dt.width -= insets.left + insets.right + ((ncols - 1) * hgap);
            dt.height -= insets.top + insets.bottom + ((nrows - 1) * vgap);

            /* WARNING!!
             *
             * This code fixes a problem with unreported insets when a JPanel is
             * embedded within a JViewport (used by JScrollPane).  The JPanel
             * does NOT report the insets created by the JScrollPane and the
             * dimensions used are therefore always too large for the
             * display area.
             *
             */
            if (target.getParent() != null) {
                if (target.getParent().getClass().getName().equals("javax.swing.JViewport"))
                    dt.width -= 4;
            }


            int minColWidth[] = new int[ncols];
            int minRowHeight[] = new int[nrows];
            boolean colSpace[] = new boolean[ncols];
            boolean rowSpace[] = new boolean[nrows];

            // second pass - store some general inforamtion that
            // is used later to size the components.
            int index = 0;
            for (int row = 0; row < nrows; row++) {
	        for (int column = 0; column < ncols; column++, index++) {
                    if (index >= ncomponents)
                        continue;

                    minColWidth[column] = Math.max(minColWidth[column], widths[index]);
                    minRowHeight[row] = Math.max(minRowHeight[row], heights[index]);

                    if (widths[index] < 0)
                        colSpace[column] = true;
                    if (heights[index] < 0)
                         rowSpace[row] = true;
                }
	        }

            // third pass - allocate available width to components that
            // require some spacefilling.
            int noColFill = 0;
            for (int i = 0; i < ncols; i++) {
                if (colSpace[i] == true)
                    noColFill++;
            }

            if (noColFill > 0) {

                index = 0;
                for (int row = 0; row < nrows; row++) {
                    int nofill = 0;
                    int tempColFill = 0;
                    int usedWidth = 0;
                    for (int i = 0; i < ncols; i++) {
                        usedWidth += minColWidth[i];
                    }

                    int widthFill = (dt.width - usedWidth) / noColFill;

                    for (int column = 0; column < ncols; column++, index++) {
                        if(index >= ncomponents)
                            continue;

                        if (widths[index] < 0) {
                            if(minColWidth[column] > widthFill && tempColFill > 1) {
                                widths[index] = minColWidth[column];

                                tempColFill = noColFill - (++nofill);
                                if (tempColFill < 1)
                                    tempColFill = 1;

                                widthFill = (dt.width - usedWidth) / tempColFill;
                            } else {
                                widths[index] = minColWidth[column] + widthFill;
                                minColWidth[column] = widths[index];
                            }
                        }
                    }
                }
            }

            // fourth pass - allocate available height to components that
            // require some spacefilling.
            int noRowFill = 0;
            for (int i = 0; i < nrows; i++) {
                if (rowSpace[i] == true)
                    noRowFill++;
            }

            if (noRowFill > 0) {
                int minUsedHeight = 0;
                for (int i = 0; i < nrows; i++) {
                    minUsedHeight += minRowHeight[i];
                }

                int heightFill = (dt.height - minUsedHeight) / noRowFill;

                index = 0;
                for (int row = 0; row < nrows; row++) {

                    int startHeight = minRowHeight[row];
                    for (int column = 0; column < ncols; column++, index++) {
                        if(index >= ncomponents)
                            continue;

                        if(heights[index] < 0)  {
                            heights[index] = heightFill + startHeight;
                            minRowHeight[row] = heights[index];
                        }
                    }
                }
            }

            // fifth pass - now layout the components in the correct size
            // grids - these are the max for that row and that column.
            index = 0;
            int y = insets.top;
            for (int row = 0, x = insets.left; row < nrows; row++) {
                for (int column = 0; column < ncols; x += minColWidth[column] + hgap, column++, index++) {
                    if (index >= ncomponents)
                        continue;

                    Component c = target.getComponent(index);
                    if (!c.isVisible())
                        continue;

                    FlexGridConstraints blc = (FlexGridConstraints) constraints.get(c);
                    int X = x;
                    int Y = y;
                    switch (blc.align) {
                        case FlexGridConstraints.C:
                            X = x + (minColWidth[column] - widths[index]) / 2;
                            Y = y + (minRowHeight[row] - heights[index]) / 2;
                            break;
                        case FlexGridConstraints.N:
                            X = x + (minColWidth[column] - widths[index]) / 2;
                            Y = y;
                            break;
                        case FlexGridConstraints.E:
                            X = x + minColWidth[column] - widths[index];
                            Y = y + (minRowHeight[row] - heights[index]) / 2;
                            break;
                        case FlexGridConstraints.W:
                            X = x;
                            Y = y + (minRowHeight[row] - heights[index]) / 2;
                            break;
                        case FlexGridConstraints.S:
                            X = x + (minColWidth[column] - widths[index]) / 2;
                            Y = y + (minRowHeight[row] - heights[index]);
                            break;
                        case FlexGridConstraints.NW:
                            X = x;
                            Y = y;
                            break;
                        case FlexGridConstraints.NE:
                            X = x + minColWidth[column] - widths[index];
                            Y = y;
                            break;
                        case FlexGridConstraints.SW:
                            X = x;
                            Y = y + (minRowHeight[row] - heights[index]);
                            break;
                        case FlexGridConstraints.SE:
                            X = x + minColWidth[column] - widths[index];
                            Y = y + (minRowHeight[row] - heights[index]);
                            break;
                        default:
                            X = x;
                            Y = y;
                    }
                    c.setBounds(X, Y, widths[index], heights[index]);
                }
                x = insets.left;
//                y += minRowHeight[row] + hgap;
                y += minRowHeight[row] + vgap;
            }
        }
    }

    /**
     * Calculates the minimum size dimensions for the specified  panel given
     * the components in the specified parent container.
     *
     * @param target the component to be laid out
     *
     * @see LayoutManager#minimumLayoutSize
     */
    public Dimension minimumLayoutSize(Container target) {
        return preferredLayoutSize(target); // shortcut
    }

    /**
     * Calculates the preferred size dimensions for the specified panel given
     * the components in the specified parent container.
     *
     * @param target the component to be laid out
     *
     * @see LayoutManager#preferredLayoutSize
     */
    public Dimension preferredLayoutSize(Container target) {
        Dimension d = new Dimension();

        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
	    int ncomponents = target.getComponentCount();
	    int nrows = rows;
            int ncols = cols;

            if (nrows > 0)
	        ncols = (ncomponents + nrows - 1) / nrows;
	    else
    	        nrows = (ncomponents + ncols - 1) / ncols;

	    int minColWidth[] = new int[ncols];
            int minRowHeight[] = new int[nrows];
            int index = 0;

            for (int row = 0; row < nrows; row++) {
                for (int column = 0; column < ncols; column++, index++) {
                    if(index >= ncomponents)
                        continue;

                    Component c = target.getComponent(index);
                    if (!c.isVisible())
                        continue;

                    FlexGridConstraints blc = (FlexGridConstraints)constraints.get(c);

                    if (blc == null || blc.x <= 0)
                        d.width = Math.max(minColWidth[column], c.getPreferredSize().width);
                    else
                        d.width = Math.max(minColWidth[column], blc.x);

                    if (blc == null || blc.y <= 0)
                        d.height = Math.max(minRowHeight[row], c.getPreferredSize().height);
                    else
                        d.height = Math.max(minRowHeight[row], blc.y);

                    minColWidth[column] = Math.max(minColWidth[column], d.width);
                    minRowHeight[row] = Math.max(minRowHeight[row], d.height);

                }
            }

            int totalWidth = 0;
            for (int i = 0; i < ncols; i++)
                totalWidth += minColWidth[i];

            int totalHeight = 0;
            for (int i = 0; i < nrows; i++)
                totalHeight += minRowHeight[i];

            d.width = insets.left + insets.right + totalWidth + (ncols-1)*hgap;
            d.height = insets.top + insets.bottom + totalHeight + (nrows-1)*vgap;

            /* WARNING!!
             *
             * This code fixes a problem with unreported insets when a JPanel is
             * embedded within a JViewport (used by JScrollPane).  The JPanel
             * does NOT report the insets created by the JScrollPane and the
             * dimensions used are therefore always too large for the
             * display area.
             *
             */
            if (target.getParent() != null) {
                if (target.getParent().getClass().getName().equals("javax.swing.JViewport"))
                    d.width += 4;
            }
        }

        return d;
    }

    /**
     * Returns the maximum size of this component.
     *
     * @param target the component to be laid out
     *
     * @see LayoutManager2#maximumLayoutSize
     */
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target); // shortcut
    }

    /**
     * Returns the alignment along the x axis.
     *
     * @param target the component to be laid out
     *
     * @see LayoutManager2#getLayoutAlignmentX
     */
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.
     *
     * @param target the component to be laid out
     *
     * @since 1.0
     * @see LayoutManager2#getLayoutAlignmentY
     */
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }


    static public void main(String[] args) {
        JFrame f = new JFrame("Test");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        final Container c = f.getContentPane();
        c.setLayout(new FlexGridLayout(2, 2, 10, 10));
        c.add(new JButton("1"), new FlexGridConstraints( 50, 50, FlexGridConstraints.C ) );
        c.add(new JButton("2"), new FlexGridConstraints( 50, 50, FlexGridConstraints.C));
        c.add(new JButton("3"), new FlexGridConstraints( 50, 50, FlexGridConstraints.C));
        final JButton a = new JButton("4");
        c.add(a, new FlexGridConstraints( 50, 50, FlexGridConstraints.C ));

        a.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                System.err.println("c.getSize(): " + c.getSize() );
                System.err.println("a.getPreferredSize(): " + a.getPreferredSize() );
            }
        });

        f.pack();
        f.show();
    }
}
