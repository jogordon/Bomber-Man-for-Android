package edu.udel.jogordon.gameframework;

import java.io.Serializable;

/**
 * Position is immutable. It stores a floating point value for
 * row and column but can safely store whole number values up
 * to 2^23 without loss of precision, which is plenty large
 * enough for grid based games that use whole number row/columns.
 * 
 * Serializable is an interface used later in DL4 but put here
 * now so that we don't have to replace this code.
 * 
 */
public final class Position implements Serializable {
    // serial ID for later when we need to persist the Position
    //  objects to a data store
    private static final long serialVersionUID = 1L;

    private float row;
    private float col;

    public Position(float row, float col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row as the containing grid row 
     * (i.e. the position's row as an int).
     * @return
     */
    public int getRow() {
        return (int)row;
    }

    /**
     * Returns the col as the containing grid column 
     * (i.e. the position's col as an int).
     * @return
     */
    public int getCol() {
        return (int)col;
    }
    
    public float getFloatRow() {
        return row;
    }
    
    public float getFloatCol() {
        return col;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(col);
        result = prime * result + Float.floatToIntBits(row);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        if (Float.floatToIntBits(col) != Float.floatToIntBits(other.col)) {
            return false;
        }
        if (Float.floatToIntBits(row) != Float.floatToIntBits(other.row)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Position [row=" + row + ", col=" + col + "]";
    }

    /**
     * Computes the distance "as the crow flies" between two Positions
     * in a cartesian coordinate system.
     * 
     * @param other
     * @return
     */
    public float distance(Position other) {
        float dx = other.col - col;
        float dy = other.row - row;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Computes the distance in grid spaces between two Positions as
     * would be required to "walk" along city blocks. This is also
     * known in game design literature as Manhattan block distance.
     * 
     * @param other
     * @return
     */
    public int blockDistance(Position other) {
        int dx = Math.abs(other.getCol() - getCol());
        int dy = Math.abs(other.getRow() - getRow());
        return dx + dy;
    }

}
