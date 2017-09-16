package com.mingkaic.rotorbutton.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.mingkaic.rotorbutton.RotorButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends ActionBarActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rotor_button)
    RotorButton rotatingButton;
    @Bind(R.id.current_orientation_info)
    TextView currentOrientationInfo;

    @Bind(R.id.rotation_speed_bt)
    View rotation_speedBt;
    @Bind(R.id.rotation_speed_tv)
    TextView rotation_speedTv;
    @Bind(R.id.rotation_center_offset_bt)
    View centerOffsetBt;
    @Bind(R.id.rotation_center_offset_tv)
    TextView centerOffsetTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        rotatingButton.setOnOrientationChangedListener(new RotorButton.OnOrientationChangedListener() {

            @Override
            public void onOrientationChanged(int action, double orientation) {
                currentOrientationInfo.setText(String.valueOf(orientation) + "˚");
            }

        });

        rotation_speedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.rotation_speed))
                .items(getDegLists())
                .itemsCallbackSingleChoice((int) rotatingButton.getRotation_speed() - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                        rotatingButton.setRotation_speed((double) (which + 1));
                        rotation_speedTv.setText("" + (which + 1) + "˚");
                        return true;
                    }
                })
                .positiveText(getString(R.string.choose))
                .show();
            }
        });

        centerOffsetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.center_offset))
                .items(getDPLists())
                .itemsCallbackSingleChoice(dpFromPx(rotatingButton.getOffsetCenter()) - 20, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                        rotatingButton.setOffsetCenter(pxFromDp(which + 20));
                        centerOffsetTv.setText("" + (which + 20) + "dp");
                        return true;
                    }
                })
                .positiveText(getString(R.string.choose))
                .show();
            }
        });
    }

    private String[] getDegLists() {
        String[] names = new String[20];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + (i + 1) + "˚";
        return names;
    }

    private String[] getDPLists() {
        String[] names = new String[51];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + (i + 20) + "dp";
        return names;
    }

    private int dpFromPx(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private int pxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
