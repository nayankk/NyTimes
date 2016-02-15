package com.xc0ffee.nytimes.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.xc0ffee.nytimes.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsDialogFragement extends android.support.v4.app.DialogFragment {

    @Bind(R.id.etDatePicker) EditText mDateTv;
    @Bind(R.id.cbNewest) AppCompatCheckBox mCbNewest;
    @Bind(R.id.cbOldest) AppCompatCheckBox mCbOldest;
    @Bind(R.id.btnSave) Button mSaveBtn;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.cbArts) AppCompatCheckBox mCbArts;
    @Bind(R.id.cbFashion) AppCompatCheckBox mCbFashion;
    @Bind(R.id.cbSports) AppCompatCheckBox mCbSports;

    private String mDate;

    public SettingsDialogFragement() {
    }

    public static SettingsDialogFragement newInstance(String title) {
        SettingsDialogFragement fragement = new SettingsDialogFragement();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragement.setArguments(args);
        return fragement;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickDate();
            }
        });

        mCbNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbOldest.setChecked(false);
            }
        });

        mCbOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbNewest.setChecked(false);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        String title = getArguments().getString("title", "Filter your results");
        mToolbar.setTitle(title);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void onPickDate() {
        final Calendar newCalendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
                mDateTv.setText(formatter.format(newDate.getTime()));

                formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
                mDate = formatter.format(newDate.getTime());
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onSaveClicked() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("date", mDate);

        String sortOrder;
        if (mCbNewest.isChecked()) sortOrder = "newest";
        else sortOrder = "oldest";
        editor.putString("sortorder", sortOrder);

        List<String> deskList = new ArrayList<String>();
        if (mCbArts.isChecked()) deskList.add("arts");
        if (mCbFashion.isChecked()) deskList.add("fashion");
        if (mCbSports.isChecked()) deskList.add("sports");
        String deskListString = TextUtils.join(" ", deskList);
        editor.putString("desk", deskListString);

        editor.apply();
        dismiss();
    }
}
