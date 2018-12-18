package it.pgp.continuousselectionadapters.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import it.pgp.continuousselectionadapters.AdapterItem;
import it.pgp.continuousselectionadapters.R;

public class MainActivityLegacy extends Activity {

    final List<AdapterItem> MOBILE_OS = new ArrayList<AdapterItem>(){{
        for (int i=0;i<6;i++) {
            add(new AdapterItem("Android"));
            add(new AdapterItem("iOS"));
            add(new AdapterItem("WindowsMobile"));
            add(new AdapterItem("Blackberry"));
        }
    }};

    public RelativeLayout mainLayout;
    public RelativeLayout selectOnMoveLayout;
    public CheckBox toggleSelectMode;
    public CheckBox invertedSelection;
    public CheckBox stickySelection;

    public CSListAdapterLegacy adapter;
    public ListView mainListview;

    private static final RelativeLayout.LayoutParams dynParams = new RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
    static {
        dynParams.addRule(RelativeLayout.ALIGN_PARENT_END);
    }

    private boolean active = false;
    private void switchDynLayout() {
        active = !active;
        if (active) {
            selectOnMoveLayout = new RelativeLayout(this);
            selectOnMoveLayout.setLayoutParams(dynParams);
            selectOnMoveLayout.setBackgroundResource(R.color.transparentBlue);
            selectOnMoveLayout.setOnTouchListener(adapter.listener);
            mainLayout.addView(selectOnMoveLayout);
        }
        else {
            mainLayout.removeView(selectOnMoveLayout);
            selectOnMoveLayout = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_legacy);
        mainLayout = findViewById(R.id.mainLayout);
        mainListview = findViewById(R.id.mainListview);
        invertedSelection = findViewById(R.id.invertSelection);
        stickySelection = findViewById(R.id.stickySelection);
        adapter = new CSListAdapterLegacy(this,R.layout.list_mobile,MOBILE_OS,mainListview,invertedSelection,stickySelection);
        mainListview.setAdapter(adapter);

        toggleSelectMode = findViewById(R.id.toggleSelectMode);
        toggleSelectMode.setOnClickListener(v->switchDynLayout());

        mainListview.setOnItemClickListener((l, v, position, id) -> {
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