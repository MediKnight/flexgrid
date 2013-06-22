package de.baltic_online.swing;

import java.awt.*;

/**
 * Constraint class for FlexGridLayout.
 *
 * @author sma@baltic-online.de
 * @version 1.0
 * @see FlexGridLayout
 */
public class FlexGridConstraints implements Cloneable {
    static float factor = Toolkit.getDefaultToolkit().getScreenResolution() / 9f;
    public int x;
    public int y;
    public int align = C;

    // Constraint values used for alignment
    public static final int  C = 0;
    public static final int  N = 1;
    public static final int  W = 2;
    public static final int  E = 3;
    public static final int  S = 4;
    public static final int NW = 5;
    public static final int NE = 6;
    public static final int SW = 7;
    public static final int SE = 8;

    // Constraint values determining the size a component
    public static final int FILL = -1;
    public static final int PREFERRED = 0;

    /**
     * Constuct a new Constaint object.
     *
     * @param x the horizontal constraint
     * @param y the vertical constraint
     * @param align the component's position in the cell
     */
    public FlexGridConstraints(int x, int y, int align) {
        this.x = x;
        this.y = y;
        this.align = align;
    }

    /**
     * Construct a new Constaint object based on the specified constraint
     * string.
     *
     * @param constraint a string specifying the constraints
     * @throws IllegalArgumentException if string is invalid
     */
    public FlexGridConstraints(String constraint) {
        if (constraint == null)
            return;
        Tokenizer st = new Tokenizer(constraint);
        String token = st.nextToken();
        try {
            if (token.length() > 0 && (Character.isDigit(token.charAt(0)) || token.charAt(0) == '-')) {
                x = Integer.parseInt(token);
                if (x > 0) x *= factor;
            }
            if (token.length() == 0 || Character.isDigit(token.charAt(0)) || token.charAt(0) == '-') {
                token = st.nextToken();
            }
            if (token.length() > 0 && (Character.isDigit(token.charAt(0)) || token.charAt(0) == '-')) {
                y = Integer.parseInt(token);
                if (y > 0) y *= factor * 2;
            }
            if (token.length() == 0 || Character.isDigit(token.charAt(0)) || token.charAt(0) == '-') {
                token = st.nextToken();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid number " + token);
        }
        if (token.length() > 0) {
            align = decodeAlign(token);
        }
    }

    /**
     * Decode an alignment string.
     *
     * @param the string containing the alignment specification
     * @exception IllegalArgumentException if the string does not specify
     * a valid alignment
     */
    private int decodeAlign(String s) {
        s = s.toLowerCase();
        if (s.equals("c"))
            return C;
        if (s.equals("n"))
            return N;
        if (s.equals("w"))
            return W;
        if (s.equals("e"))
            return E;
        if (s.equals("s"))
            return S;
        if (s.equals("nw"))
            return NW;
        if (s.equals("ne"))
            return NE;
        if (s.equals("sw"))
            return SW;
        if (s.equals("se"))
            return SE;
        throw new IllegalArgumentException("unknown alignment " + s);
    }

    public String toString() {
        return x + "," + y + ","
            + "C N W E S NWNESWSE".substring(align * 2, align * 2 + 2).trim();
    }

    private static class Tokenizer {
        private String source;
        private int last;

        Tokenizer(String source) {
            this.source = source;
            this.last = 0;
        }

        String nextToken() {
            if (last < 0)
                return "";
            int i = source.indexOf(',', last);
            String s;
            if (i < 0) {
                s = source.substring(last);
                last = -1;
            }
            else {
                s = source.substring(last, i);
                last = i + 1;
            }
            return s;
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch( Exception e ) {}

        return null;
    }
}