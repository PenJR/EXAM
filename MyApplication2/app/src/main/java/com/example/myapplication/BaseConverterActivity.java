package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.math.BigInteger;

public class BaseConverterActivity extends Activity {

    private TextView resultDisplay;
    private String input = "";
    private Spinner baseSpinner;
    private Spinner navSpinner;

    // digit and letter buttons
    private Button[] digitBtns = new Button[10];
    private Button[] letterBtns = new Button[6];
    private Button btnClear, btnDel, btnConvert, btnDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_converter);

        // navigation spinner (top)
        setupNavigationSpinner();

        // UI refs
        resultDisplay = findViewById(R.id.resultDisplay);
        baseSpinner = findViewById(R.id.baseSpinner);

        // map digit buttons 0..9
        int[] digitIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int i = 0; i < 10; i++) {
            final int digit = i;
            digitBtns[i] = findViewById(digitIds[i]);
            digitBtns[i].setOnClickListener(v -> {
                input += String.valueOf(digit);
                updateInputDisplay();
            });
        }

        // map hex letter buttons A-F
        int[] letterIds = { R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF };
        final char[] letters = {'A','B','C','D','E','F'};
        for (int i = 0; i < 6; i++) {
            final String ch = String.valueOf(letters[i]);
            letterBtns[i] = findViewById(letterIds[i]);
            letterBtns[i].setOnClickListener(v -> {
                input += ch;
                updateInputDisplay();
            });
        }

        // other buttons
        btnDot = findViewById(R.id.btnDot);            // we will disable dot (not used)
        btnClear = findViewById(R.id.btnClear);
        btnDel = findViewById(R.id.btnDel);
        btnConvert = findViewById(R.id.btnConvert);

        // Dot not used for integer base conversions — disable to avoid confusion
        btnDot.setEnabled(false);
        btnDot.setAlpha(0.4f);

        btnClear.setOnClickListener(v -> {
            input = "";
            updateInputDisplay();
        });

        btnDel.setOnClickListener(v -> {
            if (input.length() > 0) {
                input = input.substring(0, input.length() - 1);
                updateInputDisplay();
            }
        });

        btnConvert.setOnClickListener(v -> convertValue());

        // base spinner setup
        String[] bases = {"Decimal", "Binary", "Octal", "Hexadecimal"};
        ArrayAdapter<String> baseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bases);
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baseSpinner.setAdapter(baseAdapter);
        baseSpinner.setSelection(0);

        // enable/disable keys depending on base
        baseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int radix = getRadixFromName(bases[pos]);
                updateKeypadForRadix(radix);
                // clear previous input when base changes (optional)
                input = "";
                updateInputDisplay();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // show initial empty state
        resultDisplay.setText("");
    }

    private void updateInputDisplay() {
        // show typed input in result area when waiting for Convert
        resultDisplay.setText(input);
    }

    private void convertValue() {
        if (input.trim().isEmpty()) {
            resultDisplay.setText("Enter a value to convert");
            return;
        }

        String selectedBase = (String) baseSpinner.getSelectedItem();
        int radix = getRadixFromName(selectedBase);

        try {
            // Parse using BigInteger to handle big numbers
            BigInteger value = new BigInteger(input, radix);

            String bin = value.toString(2);
            String oct = value.toString(8);
            String dec = value.toString(10);
            String hex = value.toString(16).toUpperCase();

            String out = "DEC: " + dec + "\nBIN: " + bin + "\nOCT: " + oct + "\nHEX: " + hex;
            resultDisplay.setText(out);

        } catch (NumberFormatException ex) {
            resultDisplay.setText("Invalid input for base " + selectedBase);
        }
    }

    private int getRadixFromName(String baseName) {
        switch (baseName) {
            case "Binary": return 2;
            case "Octal": return 8;
            case "Hexadecimal": return 16;
            default: return 10; // Decimal
        }
    }

    private void updateKeypadForRadix(int radix) {
        // enable digits 0..(min(9,radix-1))
        for (int i = 0; i <= 9; i++) {
            boolean enabled = (i < radix); // if radix>10 this allows 0-9
            digitBtns[i].setEnabled(enabled);
            digitBtns[i].setAlpha(enabled ? 1f : 0.4f);
        }

        // enable letters A..F only if radix > 10
        if (radix > 10) {
            int lettersAllowed = radix - 10; // e.g., hex -> 6 letters
            for (int i = 0; i < 6; i++) {
                boolean enabled = (i < lettersAllowed);
                letterBtns[i].setEnabled(enabled);
                letterBtns[i].setAlpha(enabled ? 1f : 0.4f);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                letterBtns[i].setEnabled(false);
                letterBtns[i].setAlpha(0.4f);
            }
        }
    }

    // Top navigation spinner
    private void setupNavigationSpinner() {
        navSpinner = findViewById(R.id.spinner);
        String[] choices = {"Basic Calculator", "Base Number Converter", "Unit Converter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navSpinner.setAdapter(adapter);
        navSpinner.setSelection(1); // this screen

        navSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    startActivity(new Intent(BaseConverterActivity.this, BasicCalculatorActivity.class));
                    finish();
                } else if (pos == 2) {
                    startActivity(new Intent(BaseConverterActivity.this, UnitConverterActivity.class));
                    finish();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
