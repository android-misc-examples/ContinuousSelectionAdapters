package it.pgp.continuousselectionadapters;

import android.widget.Checkable;

import java.io.Serializable;

public class AdapterItem implements Serializable, Checkable {
    public String filename;
    private boolean checked = false;

    public AdapterItem(String filename) {
        this.filename = filename;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return filename;
    }
}
