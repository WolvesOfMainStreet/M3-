package org.homenet.dnoved1.woms.model;

/**
 * Interface for displayable objects. Defines methods for displaying in
 * different ways, so that views can determine which one to use based on
 * screen availability.
 */
public interface Displayable {

    /**
     * Returns a brief, one line summary of this object. Should be less than
     * 80 characters, so that it will fit on almost any screen.
     *
     * @return a one line string representation of this object.
     */
    String oneLineString();

    /**
     * Returns a multi-line string representation of this object.
     *
     * @return a multi-line representation of this object.
     */
    String[] multiLineString();
}
