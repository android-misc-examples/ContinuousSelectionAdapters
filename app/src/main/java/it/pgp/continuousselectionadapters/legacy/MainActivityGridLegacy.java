package it.pgp.continuousselectionadapters.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import it.pgp.continuousselectionadapters.AdapterItem;
import it.pgp.continuousselectionadapters.Pair;
import it.pgp.continuousselectionadapters.R;

public class MainActivityGridLegacy extends Activity {

    final List<AdapterItem> MOBILE_OS = new ArrayList<AdapterItem>(){{
        for (int i=0;i<6;i++) {
            add(new AdapterItem("Android"));
            add(new AdapterItem("iOS"));
            add(new AdapterItem("WindowsMobile"));
            add(new AdapterItem("Blackberry"));
        }
    }};

    private static final RelativeLayout.LayoutParams dynParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private boolean active = false;
    private void switchDynLayout() {
        active = !active;
        if (active) {
            selectOnMoveLayout = new RelativeLayout(this);
            selectOnMoveLayout.setLayoutParams(dynParams);
            selectOnMoveLayout.setBackgroundResource(R.color.transparentBlue);
            selectOnMoveLayout.setOnTouchListener(adapter.listener);
            mainLayout.addView(selectOnMoveLayout);
            controlLayout.bringToFront();
        }
        else {
            mainLayout.removeView(selectOnMoveLayout);
            selectOnMoveLayout = null;
        }
    }

    public CSGridAdapterLegacy adapter;
    public GridView mainGridview;
    public RelativeLayout mainLayout;
    public RelativeLayout selectOnMoveLayout;
    public LinearLayout controlLayout;
    public Button toggleSelectMode;
    public CheckBox invertedSelection;
    public CheckBox stickySelection;

    static final int colsInRow = 3;

    private void debugPosGrid(int pos, int action) {
        final String[] actions = {"DOWN","UP","MOVE"};
        Pair<Integer,Integer> p = CSGridAdapterLegacy.intToPoint(pos,colsInRow);
        if (action < 0 || action >= actions.length) {
            Log.e("debugPosGrid","outofbounds: "+action);
            return;
        }
        Log.e("debugPosGrid", "action: "+actions[action]+" pos: "+p.i+","+p.j);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid_legacy);

        controlLayout = findViewById(R.id.controlLayout);

        mainLayout = findViewById(R.id.mainLayoutGrid);
        mainGridview = findViewById(R.id.mainGridview);

        invertedSelection = findViewById(R.id.invertSelection);
        stickySelection = findViewById(R.id.stickySelection);
        adapter = new CSGridAdapterLegacy(this, R.layout.list_mobile, MOBILE_OS, mainGridview,invertedSelection,stickySelection);
        mainGridview.setAdapter(adapter);
//        mainGridview.setOnTouchListener(adapter.listener);
        toggleSelectMode = findViewById(R.id.toggleSelectMode);
        toggleSelectMode.setOnClickListener(v->switchDynLayout());
        mainGridview.setOnItemClickListener((l, v, position, id) -> {
            try {
                if (active) {
                    adapter.toggleSelectOne(position);
//                    Log.e("TOGGLE", "position "+position);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
