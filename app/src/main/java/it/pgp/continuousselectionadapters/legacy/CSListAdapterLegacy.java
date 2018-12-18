package it.pgp.continuousselectionadapters.legacy;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.pgp.continuousselectionadapters.AdapterItem;
import it.pgp.continuousselectionadapters.R;

public class CSListAdapterLegacy extends ContinuousSelectionAdapterLegacy {

    // for select-on-move mode
    private int idx_2 = -1; // before last selected index
    private int idx_1 = -1; // last selected index

    private boolean initialDestCheckStatus;

    private boolean atLeastOneMoveAfterDown = false;

    private int startPos;

    @Override
    public void startSelectMode(int startPos) {
        super.startSelectMode(startPos);
        this.startPos = startPos;
        destCheckStatus = initialDestCheckStatus = !getInvSel();
        fillSelectionBeforeStart();

        objects.get(startPos).setChecked(destCheckStatus);
        notifyDataSetChanged();
    }

    @Override
    public void ongoingSelectMode(int position) {
        Log.d("POSITION", "Current: "+position+" Previous: "+idx_1+" Old: "+idx_2);
        if (position < 0 || position >= objects.size() || position == idx_1) return;
        Log.e("SelectModeContinue", "SelectModeContinue");

        atLeastOneMoveAfterDown = true;

        if (position == startPos && idx_1 >= 0) {
            destCheckStatus = initialDestCheckStatus;
            if (!selectionBeforeStart.contains(idx_1)) {
                objects.get(idx_1).setChecked(!destCheckStatus);
            }
            if (idx_2 >= 0 && !selectionBeforeStart.contains(idx_2)) {
                objects.get(idx_2).setChecked(!destCheckStatus);
            }
            idx_1 = -1;
            idx_2 = -1;
            return;
        }

        if (position != idx_1) {
            if (position == idx_2) { // direction inverted, deselect previous
                destCheckStatus = !destCheckStatus;
                if (!selectionBeforeStart.contains(idx_1)) {
                    objects.get(idx_1).setChecked(destCheckStatus);
                }
                if (!selectionBeforeStart.contains(position)) {
                    objects.get(position).setChecked(destCheckStatus);
                }
            }
            else {
                if (!selectionBeforeStart.contains(position)) {
                    objects.get(position).setChecked(destCheckStatus);
                }
            }
            notifyDataSetChanged();

            if (idx_1 < 0) {
                idx_1 = idx_2 = position;
            }
            else {
                idx_2 = idx_1;
                idx_1 = position;
            }
        }

        if (position >= lv.getLastVisiblePosition()) {
            lv.smoothScrollToPosition(position+1);
        }
        else if (position <= lv.getFirstVisiblePosition()) {
            lv.smoothScrollToPosition(position-1);
        }
    }

    @Override
    public void endSelectMode(int endPos) {
        // Log.e("SelectModeEnd", "SelectModeEnd");
        if (!active) {
            // Log.e("MOTION", "Repeated ACTION_UP events (multitouch?), ignoring...");
            return;
        }
        if (startPos == endPos && !atLeastOneMoveAfterDown) {
            if (!selectionBeforeStart.contains(startPos)) {
                objects.get(startPos).setChecked(!initialDestCheckStatus);
            }
        }

        selectionBeforeStart.clear();
        idx_1 = -1;
        idx_2 = -1;
        active = false;
        destCheckStatus = initialDestCheckStatus;
        atLeastOneMoveAfterDown = false;
        notifyDataSetChanged();
    }

    /////////////////////////////

    private static int getResByString(String s) {
        switch (s) {
            case "WindowsMobile":
                return R.drawable.windowsmobile_logo;
            case "iOS":
                return R.drawable.ios_logo;
            case "Blackberry":
                return R.drawable.blackberry_logo;
            default:
                return R.drawable.android_logo;
        }
    }

    public static class AdapterItemViewHolder {
        TextView name;
        ImageView imageView;

        AdapterItemViewHolder(TextView name, ImageView imageView) {
            this.name = name;
            this.imageView = imageView;
        }
    }

    CSListAdapterLegacy(Context context,
                        int itemLayoutRes,
                        List<AdapterItem> objects,
                        AbsListView lv,
                        Checkable checkBoxInvSel,
                        Checkable checkBoxStickySel) {
        super(context, itemLayoutRes, objects, lv, checkBoxInvSel, checkBoxStickySel);
    }



    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        AdapterItem item = (AdapterItem) getItem(position);
        TextView name;
        ImageView imageView;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_mobile, null);

            name = convertView.findViewById(R.id.label);
            imageView = convertView.findViewById(R.id.logo);

            convertView.setTag(new AdapterItemViewHolder(name,imageView));
        }
        else {
            AdapterItemViewHolder viewHolder = (AdapterItemViewHolder) convertView.getTag();
            name = viewHolder.name;
            imageView = viewHolder.imageView;
        }

        convertView.setBackgroundColor(item.isChecked()? 0x9934B5E4: Color.TRANSPARENT);

        name.setText(item.filename);

        imageView.setImageResource(getResByString(item.filename));

        return convertView;
    }
}