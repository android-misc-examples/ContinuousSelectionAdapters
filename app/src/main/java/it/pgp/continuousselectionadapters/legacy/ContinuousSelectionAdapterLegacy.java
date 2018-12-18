package it.pgp.continuousselectionadapters.legacy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ContinuousSelectionAdapterLegacy extends ArrayAdapter {

    protected final AbsListView lv; // indicate if select mode is active and needed to auto scroll, to be used with selection mode toggle performed separately (like in XFiles)
    protected final List<? extends Checkable> objects;
    protected final LayoutInflater inflater;

    public boolean active = false;

    protected final Checkable invSelCheckable;
    protected final Checkable stickyCheckable;

    protected boolean destCheckStatus = true;
    protected final Set<Integer> selectionBeforeStart = new HashSet<>();
    protected void fillSelectionBeforeStart() {
        selectionBeforeStart.clear();
        if (!getStickySel()) {
            setAllSelection(getInvSel());
        }
        else {
            for (int ii=0;ii<objects.size();ii++) {
                if (objects.get(ii).isChecked() == destCheckStatus)
                    selectionBeforeStart.add(ii);
            }
        }
    }

    public ContinuousSelectionAdapterLegacy(@NonNull Context context, int resource, List<? extends Checkable> objects, AbsListView lv, Checkable invSelCheckable, Checkable stickyCheckable) {
        super(context, resource, objects);
        this.objects = objects;
        this.lv = lv;
        this.invSelCheckable = invSelCheckable;
        this.stickyCheckable = stickyCheckable;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AbsListView getLv() {
        return lv;
    }

    // this is used by the listener
    public void startSelectMode(int startPos) {
        if (active) {
            Log.e("MOTION", "Repeated ACTION_DOWN events (multitouch?), ignoring...");
            return;
        }
        if (startPos < 0 || startPos >= objects.size()) return;
        active = true;
    }

    abstract void ongoingSelectMode(int position);
    abstract void endSelectMode(int endPos);

    public final View.OnTouchListener listener = (v, ev) -> {
        Log.e("Motion", "Default: " + ev.getActionMasked() + " " + ev.getX() + " " + ev.getY());
        try {
            int ptoPos = getLv().pointToPosition((int) ev.getX(), (int) ev.getY());
            switch(ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("MotionSTART", "Start select mode");
                    startSelectMode(ptoPos);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("MotionEND", "End select mode");
                    endSelectMode(ptoPos);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("MotionCONTINUE", "Ongoing select mode");
                    ongoingSelectMode(ptoPos);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    };

    public void toggleSelectOne(int position) {
        objects.get(position).toggle();
        notifyDataSetChanged();
    }

    public void setAllSelection(boolean checked) {
        for (Checkable i : objects)
            i.setChecked(checked);
        notifyDataSetChanged();
    }

    public boolean getInvSel() {
        return invSelCheckable.isChecked();
    }

    public boolean getStickySel() {
        return stickyCheckable.isChecked();
    }
}
