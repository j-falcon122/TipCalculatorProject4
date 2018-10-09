package com.example.jordanfalcon.tipcalculatorproject4;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener {

    //declare your variables for the widgets
    private EditText editTextBillAmount;
    private TextView textViewBillAmount;
    private SeekBar tipSeekBar;
    private TextView textViewPercent;
    private TextView textViewTipAmount;
    private TextView textViewTipAmountLabel;
    private TextView textViewTotalAmount;
    private TextView textViewTotalAmountLabel;
    private TextView textViewPerPersonAmount;
    private TextView textViewPerPersonAmountLabel;
    private Spinner tipSpinner;
    private TextView tipSpinnerLabel;
    private RadioGroup tipRadioGroup;
    private TextView tipRadioLabel;


    //declare the variables for the calculations
    private double billAmount = 0.0;
    private double percent = .15;
    private boolean roundTip = false;
    private boolean roundTotal = false;
    private int persons = 1;
    private double perPersonAmount = 0.0;

    //set the number formats to be used for the $ amounts , and % amounts
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add Listeners to Widgets
        editTextBillAmount = (EditText)findViewById(R.id.editText_BillAmount);//uncomment this line
        editTextBillAmount.addTextChangedListener((TextWatcher) this);//uncomment this line

        textViewBillAmount = (TextView)findViewById(R.id.textView_BillAmount);

        // Seekbar
        tipSeekBar = (SeekBar)findViewById(R.id.tipSeekBar);
        textViewPercent = (TextView)findViewById(R.id.textView_tipSeekBarLabel);

        // Tip amount
        textViewTipAmountLabel = (TextView)findViewById(R.id.textView_TipAmountLabel);
        textViewTipAmount = (TextView)findViewById(R.id.textView_TipAmount);

        // Total Amount
        textViewTotalAmount = (TextView)findViewById(R.id.textView_TotalAmount);
        textViewTotalAmountLabel = (TextView)findViewById(R.id.textView_TotalAmountLabel);

        // Per Person Amount
        textViewPerPersonAmount = (TextView)findViewById(R.id.textView_PerPersonAmount);
        textViewPerPersonAmountLabel = (TextView)findViewById(R.id.textView_PerPersonAmountLabel);


        // Radio
        tipRadioGroup = (RadioGroup)findViewById(R.id.tipRadioGroup);
        tipRadioLabel = (TextView)findViewById(R.id.tipRadioLabel);

        // Spinner
        tipSpinnerLabel = (TextView)findViewById(R.id.tipSpinnerLabel);
        Spinner tipSpinner = (Spinner) findViewById(R.id.tipSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipSpinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipSpinner.setAdapter(adapter);
        tipSpinner.setOnItemSelectedListener(this);

        tipSeekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tipShare:
                shareCurrentItem();
                return true;
                case R.id.tipInfo:
                    AlertDialog.Builder alertDialog =
                        new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("INFO");
                    alertDialog.setMessage("Spinner is used to split the total among friends");
                    alertDialog.show();

                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareCurrentItem() {
        String messageBody = String.format("Hey the total per person is "+perPersonAmount+"!");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"));

        intent.putExtra("sms_body", messageBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /*
    Note:   int i, int i1, and int i2
            represent start, before, count respectively
            The charSequence is converted to a String and parsed to a double for you
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("MainActivity", "inside onTextChanged method: charSequence= "+charSequence);
        //surround risky calculations with try catch (what if billAmount is 0 ?
        //charSequence is converted to a String and parsed to a double for you
        billAmount = Double.parseDouble(charSequence.toString()) / 100;
        Log.d("MainActivity", "Bill Amount = "+billAmount);

        //setText on the textView
        textViewBillAmount.setText(currencyFormat.format(billAmount));
        //perform tip and total calculation and update UI by calling calculate
        calculate();//uncomment this line
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        Log.d("MainActivity","Progress Amount = "+progress);
        percent = (progress * .01); //calculate percent based on seeker value
        Log.d("MainActivity","Percent Amount = "+percent);
        textViewPercent.setText(percentFormat.format(progress));
        calculate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        persons = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        Log.d("Persons selected", "Person(s) selected: "+persons);
        calculate();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        persons = 1;
        calculate();
    }

    // calculate and display tip and total amounts
    private void calculate() {
        double tip, total;

       // format percent and display in percentTextView
      textViewPercent.setText(percentFormat.format(percent));

       // calculate the tip and total
        if (roundTip == true) {
            tip = Math.ceil(billAmount * percent);
            total = billAmount + tip;
        } else if (roundTotal == true) {
            tip = billAmount * percent;
            total = Math.ceil(billAmount + tip);
        } else {
            tip = billAmount * percent;
            total = billAmount + tip;
        }
        perPersonAmount = total / persons;

        textViewTipAmount.setText(currencyFormat.format(tip));
        textViewTotalAmount.setText(currencyFormat.format(total));
        textViewPerPersonAmount.setText(currencyFormat.format(perPersonAmount));
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rounding_no:
                if (checked)
                    // Do nothing
                    roundTotal = false;
                    roundTip = false;
                    calculate();
                    break;
            case R.id.rounding_tip:
                if (checked)
                    // rounding_tip
                    roundTotal = false;
                    roundTip = true;
                    calculate();
                    break;
            case R.id.rounding_total:
                if (checked)
                    // rounding_total
                    roundTip = false;
                    roundTotal = true;
                    calculate();
                    break;
        }
    }
}
