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
    @Bind(R.id.tap_orientation_info)
    TextView tapOrientationInfo;

    @Bind(R.id.rotation_speed_bt)
    View rotation_speedBt;
    @Bind(R.id.rotation_speed_tv)
    TextView rotation_speedTv;
    @Bind(R.id.rotation_center_offset_bt)
    View centerOffsetBt;
    @Bind(R.id.rotation_center_offset_tv)
    TextView centerOffsetTv;
    @Bind(R.id.volume_bt)
    View volumeBt;
    @Bind(R.id.volume_tv)
    TextView volumeTv;
    @Bind(R.id.vibration_duration_bt)
    View vibrationDurationBt;
    @Bind(R.id.vibration_duration_tv)
    TextView vibrationDurationTv;

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
                currentOrientationInfo.setText(String.valueOf(orientation));
            }

            @Override
            public void onTapOrientationChanged(double orientation) {
                tapOrientationInfo.setText(String.valueOf(orientation));
            }

        });

        rotation_speedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.rotation_speed))
                        .items(getDPLists(0))
                        .itemsCallbackSingleChoice((int) rotatingButton.getRotation_speed(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                rotatingButton.setRotation_speed((double) which);
                                rotation_speedTv.setText("" + which + "%");
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
                        .items(getDPLists2(0))
                        .itemsCallbackSingleChoice(dpFromPx(rotatingButton.getOffsetCenter()), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                rotatingButton.setOffsetCenter(pxFromDp(which));
                                centerOffsetTv.setText("" + which + "dp");
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        volumeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.volume))
                        .items(getVolumeLists())
                        .itemsCallbackSingleChoice(rotatingButton.getEventVolume(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                rotatingButton.setEventVolume(which);
                                volumeTv.setText("" + which);
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        vibrationDurationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.vibration_duration))
                        .items(getVibrationLists())
                        .itemsCallbackSingleChoice(rotatingButton.getVibrationDuration(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                rotatingButton.setVibrationDuration(which);
                                vibrationDurationTv.setText(which + " Milliseconds");
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });
    }

    private String[] getVibrationLists() {
        String[] names = new String[101];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + i + " Milliseconds";
        return names;
    }

    private String[] getDPLists(int start) {
        String[] names = new String[41];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + (start + i) + "dp";
        return names;
    }

    private String[] getDPLists2(int start) {
        String[] names = new String[51];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + (start + i) + "dp";
        return names;
    }

    private String[] getVolumeLists() {
        String[] names = new String[101];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + i;
        return names;
    }

    private int dpFromPx(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private int pxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
