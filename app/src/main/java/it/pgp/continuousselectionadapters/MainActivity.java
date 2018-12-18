package it.pgp.continuousselectionadapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    final List<AdapterItem> MOBILE_OS = new ArrayList<AdapterItem>(){{
        for (int i=0;i<6;i++) {
            add(new AdapterItem("Android"));
            add(new AdapterItem("iOS"));
            add(new AdapterItem("WindowsMobile"));
            add(new AdapterItem("Blackberry"));
        }
    }};

    public final CSCheckboxes csCheckboxes = new CSCheckboxes();

    public RelativeLayout masterLayout;
    public RelativeLayout selectOnMoveLayout;

//    public CSListAdapter adapter;
    public AdapterLite adapter;
    public ContSelListenerList csll;

    public ListView mainListview;

    private boolean active = false;
    public void switchDynLayout(View unused) {
        active = !active;
        if (active) {
//            selectOnMoveLayout = new ContSelHandlingLayout(this,adapter.listener, csCheckboxes,false);
            selectOnMoveLayout = new ContSelHandlingLayout(this,csll, csCheckboxes,false);


            masterLayout.addView(selectOnMoveLayout);
        }
        else {
            masterLayout.removeView(selectOnMoveLayout);
            selectOnMoveLayout = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        masterLayout = findViewById(R.id.masterLayout);
        mainListview = findViewById(R.id.mainListview);

        // LEGACY
//        adapter = new CSListAdapter(this,R.layout.list_mobile,MOBILE_OS,mainListview,csCheckboxes);
        // END LEGACY

        // NEW
        adapter = new AdapterLite(this,R.layout.list_mobile,MOBILE_OS);
        csll = new ContSelListenerList(mainListview,adapter,MOBILE_OS,csCheckboxes);
        // END NEW

        mainListview.setAdapter(adapter);

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