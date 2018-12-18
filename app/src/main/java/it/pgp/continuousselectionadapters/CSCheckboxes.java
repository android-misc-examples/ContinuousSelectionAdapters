package it.pgp.continuousselectionadapters;

import android.widget.CheckBox;

public class CSCheckboxes {
    public CheckBox continuousSelection; // governs the visibility of the other two checkboxes
    public CheckBox invertSelection;
    public CheckBox stickySelection;

    public CheckBox getContinuousSelection() {
        return continuousSelection;
    }

    public void setContinuousSelection(CheckBox continuousSelection) {
        this.continuousSelection = continuousSelection;
    }

    public CheckBox getInvertSelection() {
        return invertSelection;
    }

    public void setInvertSelection(CheckBox invertSelection) {
        this.invertSelection = invertSelection;
    }

    public CheckBox getStickySelection() {
        return stickySelection;
    }

    public void setStickySelection(CheckBox stickySelection) {
        this.stickySelection = stickySelection;
    }
}
