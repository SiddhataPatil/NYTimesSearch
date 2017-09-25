package com.codepath.siddhatapatil.nytimessearch.Activity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.siddhatapatil.nytimessearch.Model.Filter;
import com.codepath.siddhatapatil.nytimessearch.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by siddhatapatil on 9/24/17.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etBeginDate;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Filter filter;
    HashSet<String> flSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);

        dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);

        // Get value from intent;
        filter = getIntent().getParcelableExtra("filter");

        setupViews();

        setDateTimeField();

        setFilterFields(filter);

        addListenerOnSpinnerItemSelection();
    }

    private void setFilterFields(Filter filter) {

        if (!TextUtils.isEmpty(filter.getBeginDate())) {
            etBeginDate.setText(filter.getBeginDate());
        }

        CheckBox cbArts = (CheckBox) findViewById(R.id.checkbox_arts);
        CheckBox cbFashion = (CheckBox) findViewById(R.id.checkbox_fashion_style);
        CheckBox cbSports = (CheckBox) findViewById(R.id.checkbox_sports);
        ArrayList<String> deskVals = filter.getFl();
        for (String value : deskVals) {
            switch (value) {
                case "Arts" :
                    flSet.add("Arts");
                    cbArts.setChecked(true);
                    break;
                case "Fashion & Style" :
                    flSet.add("Fashion & Style");
                    cbFashion.setChecked(true);
                    break;
                case "Sports" :
                    flSet.add("Sports");
                    cbSports.setChecked(true);
                    break;
                default: break;
            }
        }
    }

    private void addListenerOnSpinnerItemSelection() {
        Spinner spinner1 = (Spinner) findViewById(R.id.dropDown);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.setSort(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setupViews() {
        etBeginDate = (EditText) findViewById(R.id.etBeginDate);
        etBeginDate.setInputType(InputType.TYPE_NULL);
        etBeginDate.requestFocus();

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Setting");
    }

    private void setDateTimeField() {
        etBeginDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
                filter.setBeginDate(dateFormat.format(newDate.getTime()));
                etBeginDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Hide search and filter options menu
        MenuItem options = menu.findItem(R.id.miFilter);
        options.setVisible(false);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == etBeginDate) {
            datePickerDialog.show();
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_arts:
                if (checked)
                    flSet.add("Arts");
                else
                    flSet.remove("Arts");
                break;
            case R.id.checkbox_fashion_style:
                if (checked)
                    flSet.add("Fashion & Style");
                else
                    flSet.remove("Fashion & Style");
                break;
            case R.id.checkbox_sports:
                if (checked)
                    flSet.add("Sports");
                else
                    flSet.remove("Sports");
                break;
        }

        ArrayList<String> list = new ArrayList<String>(flSet);
        filter.setFl(list);
    }

    public void onSubmit(View v) {
        Intent data = new Intent();
        data.putExtra("filter", filter);
        data.putExtra("code", 200);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
