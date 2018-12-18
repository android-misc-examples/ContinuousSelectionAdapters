package it.pgp.continuousselectionadapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivityGrid extends Activity {

    final List<AdapterItem> MOBILE_OS = new ArrayList<AdapterItem>(){{
        for (int i=0;i<6;i++) {
            add(new AdapterItem("Android"));
            add(new AdapterItem("iOS"));
            add(new AdapterItem("WindowsMobile"));
            add(new AdapterItem("Blackberry"));
        }
    }};

    public final CSCheckboxes csCheckboxes = new CSCheckboxes();

    private boolean active = false;
    public void switchDynLayout(View unused) {
        active = !active;
        if (active) {
            selectOnMoveLayout = new ContSelHandlingLayout(this,cslg, csCheckboxes,true);
            masterLayout.addView(selectOnMoveLayout);
        }
        else {
            masterLayout.removeView(selectOnMoveLayout);
            selectOnMoveLayout = null;
        }
    }

    public RelativeLayout masterLayout;
    public RelativeLayout selectOnMoveLayout;

//    public CSGridAdapter adapter;
    public AdapterLite adapter;
    public ContSelListenerGrid cslg;

    public GridView mainGridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        masterLayout = findViewById(R.id.masterLayout);
        mainGridview = findViewById(R.id.mainGridview);

        // LEGACY
//        adapter = new CSGridAdapter(this, R.layout.list_mobile, MOBILE_OS, mainGridview, csCheckboxes);
        // END LEGACY

        // NEW
        adapter = new AdapterLite(this,R.layout.list_mobile,MOBILE_OS);
        cslg = new ContSelListenerGrid(mainGridview,adapter,MOBILE_OS,csCheckboxes);
        // END NEW

        mainGridview.setAdapter(adapter);
        mainGridview.setOnItemClickListener((l, v, position, id) -> {
            try {
                if (active) {
                    adapter.toggleSelectOne(position);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
