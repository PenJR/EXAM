package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

    private Spinner spinner;
    private boolean isFirstSelection = true; // prevent auto-trigger on load

    // Spinner choices including a placeholder at index 0
    private final String[] choices = {
            "Select Option",
            "Basic Calculator",
            "Base Number Converter",
            "Unit Converter"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);

        // Setup spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Show "Select Option" by default
        spinner.setSelection(0);

        // Handle spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // Skip automatic first trigger
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                switch (pos) {
                    case 1:
                        startActivity(new Intent(MainActivity.this, BasicCalculatorActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, BaseConverterActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, UnitConverterActivity.class));
                        break;
                    default:
                        // "Select Option" - do nothing
                        break;
                }

                // Reset spinner to default text after launching
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }
}
