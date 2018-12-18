package it.pgp.continuousselectionadapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CSGridAdapter extends ContSelAdapter {

    private static Pair<Integer,Integer> intToPoint(int h, int colsInRow) {
        if (h < 0) return new Pair<>(-1,-1); // convention for error-code return style
        return new Pair<>(h / colsInRow, h % colsInRow);
    }

    private static int pointToInt(Pair<Integer, Integer> p, int colsInRow) {
        if (p.i < 0 || p.j < 0) return -1; // convention for error-code return style
        return p.i*colsInRow + p.j;
    }

    private int colsInRow;

    // for select-on-move mode
    private final Pair<Integer,Integer> p_1 = new Pair<>(-1,-1); // last selected index

    private final Pair<Integer,Integer> startPos = new Pair<>(-1,-1);

    @Override
    public void startSelectMode(int startPos) {
        super.startSelectMode(startPos);
        colsInRow = ((GridView)getLv()).getNumColumns();
        Log.e("AAAAAAAAAAAAAAA", "colNum: "+colsInRow);
        this.startPos.set(intToPoint(startPos,colsInRow));
        destCheckStatus = !getInvSel();
        fillSelectionBeforeStart();

        objects.get(startPos).setChecked(destCheckStatus);
        notifyDataSetChanged();
    }

    // restart from initially selected items on each action move
    private void startFromInitialSelection() {
        for (int i=0;i<objects.size();i++)
            if (!selectionBeforeStart.contains(i))
                objects.get(i).setChecked(getInvSel());
    }

    @Override
    public void ongoingSelectMode(int position_) {
        if (position_ < 0 || position_ >= objects.size() || position_ == pointToInt(p_1,colsInRow)) return;

        Log.e("EEE", "Abs: "+position_+" p_1: "+p_1+"p_1_flat: "+pointToInt(p_1,colsInRow));

        atLeastOneMoveAfterDown = true;
        startFromInitialSelection();
        final Pair<Integer,Integer> position = intToPoint(position_,colsInRow);
        p_1.set(position);

        int minx,miny,maxx,maxy;
        minx = startPos.i<position.i?startPos.i:position.i;
        miny = startPos.j<position.j?startPos.j:position.j;
        maxx = startPos.i>position.i?startPos.i:position.i;
        maxy = startPos.j>position.j?startPos.j:position.j;

        boolean checked = !getInvSel();
        for (int i=minx;i<=maxx;i++)
            for (int j=miny;j<=maxy;j++)
                objects.get(i*colsInRow+j).setChecked(checked);
        notifyDataSetChanged();
    }

    @Override
    public void endSelectMode(int endPos) {
        // Log.e("SelectModeEnd", "SelectModeEnd");
        if (!active) {
            // Log.e("MOTION", "Repeated ACTION_UP events (multitouch?), ignoring...");
            return;
        }
        if (startPos.equals(intToPoint(endPos,colsInRow)) && !atLeastOneMoveAfterDown) {
            if (!selectionBeforeStart.contains(endPos)) {
                objects.get(endPos).setChecked(!destCheckStatus);
            }
        }

        selectionBeforeStart.clear();
        p_1.set(-1,-1);
        active = false;
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

    CSGridAdapter(Context context,
                  int itemLayoutRes,
                  List<AdapterItem> objects,
                  AbsListView lv,
                  CSCheckboxes csCheckboxes) {
        super(context, itemLayoutRes, objects, lv, csCheckboxes);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        AdapterItem item = (AdapterItem) getItem(position);
        TextView name;
        ImageView imageView;

        if(convertView == null) {
            convertView = inflater.inflate(itemLayoutRes, null);

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
