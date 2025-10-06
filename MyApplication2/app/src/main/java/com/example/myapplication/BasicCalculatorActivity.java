package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class BasicCalculatorActivity extends Activity implements View.OnClickListener {

    TextView result;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        setupSpinner();

        result = findViewById(R.id.result);

        int[] btnIds = {
                R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
                R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,
                R.id.btnAdd,R.id.btnSub,R.id.btnMul,R.id.btnDiv,
                R.id.btnDot,R.id.btnClear,R.id.btnDel,R.id.btnEq,
                R.id.btnOpenParen, R.id.btnCloseParen // add buttons for ( and )
        };

        for(int id : btnIds){
            findViewById(id).setOnClickListener(this);
        }
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        String[] choices = {"Basic Calculator", "Base Number Converter", "Unit Converter"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 1) {
                    startActivity(new Intent(BasicCalculatorActivity.this, BaseConverterActivity.class));
                    finish();
                } else if (pos == 2) {
                    startActivity(new Intent(BasicCalculatorActivity.this, UnitConverterActivity.class));
                    finish();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        String text = b.getText().toString();

        switch(text){
            case "C":
                input = "";
                break;
            case "Del":
                if(input.length() > 0) input = input.substring(0, input.length()-1);
                break;
            case "=":
                String res = evaluate(input);
                input = res;
                break;
            default:
                input += text;
        }
        result.setText(input);
    }

    // Use JavaScript engine to evaluate with PEMDAS and parentheses
    private String evaluate(String expr) {
        try {
            expr = expr.replace("x", "*"); // handle your multiply symbol
            Expression e = new ExpressionBuilder(expr).build();
            double result = e.evaluate();
            return String.valueOf(result);
        } catch (Exception ex) {
            return "Error";
        }
    }
}
