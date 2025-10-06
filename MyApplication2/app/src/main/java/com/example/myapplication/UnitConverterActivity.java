package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;

public class UnitConverterActivity extends Activity {

    EditText inputValue;
    Spinner categorySpinner, unitSpinnerFrom, unitSpinnerTo, mainSpinner;
    TextView result;
    Button convertBtn, swapBtn;

    HashMap<String, String[]> unitCategories = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        setupMainSpinner();

        // Initialize views
        inputValue = findViewById(R.id.inputValue);
        categorySpinner = findViewById(R.id.categorySpinner);
        unitSpinnerFrom = findViewById(R.id.unitSpinnerFrom);
        unitSpinnerTo = findViewById(R.id.unitSpinnerTo);
        result = findViewById(R.id.resultUnit);
        convertBtn = findViewById(R.id.btnConvertUnit);
        swapBtn = findViewById(R.id.btnSwap);

        // Define 3 categories and their units
        unitCategories.put("Weight", new String[]{"Grams", "Kilograms", "Pounds"});
        unitCategories.put("Temperature", new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        unitCategories.put("Speed", new String[]{"Meters per second", "Kilometers per hour", "Miles per hour"});

        // Populate category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unitCategories.keySet().toArray(new String[0]));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Update From/To spinners when category changes
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categorySpinner.getSelectedItem().toString();
                updateUnitSpinners(category);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 🔁 Swap Button logic
        swapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fromPos = unitSpinnerFrom.getSelectedItemPosition();
                int toPos = unitSpinnerTo.getSelectedItemPosition();
                unitSpinnerFrom.setSelection(toPos);
                unitSpinnerTo.setSelection(fromPos);
            }
        });

        // Convert Button
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }

    private void setupMainSpinner() {
        mainSpinner = findViewById(R.id.spinner);
        String[] choices = {"Basic Calculator", "Base Number Converter", "Unit Converter"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSpinner.setAdapter(adapter);
        mainSpinner.setSelection(2); // current screen

        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    startActivity(new Intent(UnitConverterActivity.this, BasicCalculatorActivity.class));
                    finish();
                } else if (pos == 1) {
                    startActivity(new Intent(UnitConverterActivity.this, BaseConverterActivity.class));
                    finish();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateUnitSpinners(String category) {
        String[] units = unitCategories.get(category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinnerFrom.setAdapter(adapter);
        unitSpinnerTo.setAdapter(adapter);
    }

    private void convertUnits() {
        String inputStr = inputValue.getText().toString();
        if (inputStr.isEmpty()) return;

        double input = Double.parseDouble(inputStr);
        String category = categorySpinner.getSelectedItem().toString();
        String from = unitSpinnerFrom.getSelectedItem().toString();
        String to = unitSpinnerTo.getSelectedItem().toString();
        double output = 0;

        switch (category) {
            case "Weight":
                if (from.equals("Grams") && to.equals("Kilograms")) output = input / 1000;
                else if (from.equals("Kilograms") && to.equals("Grams")) output = input * 1000;
                else if (from.equals("Kilograms") && to.equals("Pounds")) output = input * 2.205;
                else if (from.equals("Pounds") && to.equals("Kilograms")) output = input / 2.205;
                else output = input;
                break;

            case "Temperature":
                if (from.equals("Celsius") && to.equals("Fahrenheit")) output = (input * 9/5) + 32;
                else if (from.equals("Fahrenheit") && to.equals("Celsius")) output = (input - 32) * 5/9;
                else if (from.equals("Celsius") && to.equals("Kelvin")) output = input + 273.15;
                else if (from.equals("Kelvin") && to.equals("Celsius")) output = input - 273.15;
                else if (from.equals("Fahrenheit") && to.equals("Kelvin")) output = (input - 32) * 5/9 + 273.15;
                else if (from.equals("Kelvin") && to.equals("Fahrenheit")) output = (input - 273.15) * 9/5 + 32;
                else output = input;
                break;

            case "Speed":
                if (from.equals("Meters per second") && to.equals("Kilometers per hour")) output = input * 3.6;
                else if (from.equals("Kilometers per hour") && to.equals("Meters per second")) output = input / 3.6;
                else if (from.equals("Miles per hour") && to.equals("Kilometers per hour")) output = input * 1.609;
                else if (from.equals("Kilometers per hour") && to.equals("Miles per hour")) output = input / 1.609;
                else output = input;
                break;
        }

        result.setText(input + " " + from + " = " + output + " " + to);
    }
}
