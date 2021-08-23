package neha.test.vendakata.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import neha.test.vendakata.R;
import neha.test.vendakata.viewmodels.VendingMachineRepository;
import neha.test.vendakata.viewmodels.VendingMachineViewModel;

public class VendActivity extends AppCompatActivity
        implements View.OnClickListener, AlertDialog.OnClickListener {
    private AlertDialog dlgInsertCoins;

    private EditText dlgInsertCoinsInput;

    private VendingMachineViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_vend);

        viewModel = ViewModelProviders.of(this).get(VendingMachineViewModel.class);
        viewModel.init(VendingMachineRepository.getInstance());

        // Create the observers which update the UI
        viewModel.getVendingMachineDisplay().observe(this, vendingDisplay ->
                ((TextView) findViewById(R.id.vend_display)).setText(vendingDisplay));

        viewModel.getVendingMachineChangeDisplay().observe(this, vendingChangeDisplay ->
                ((TextView) findViewById(R.id.vend_btn_collect)).setText(vendingChangeDisplay));

        final TextView product1 = findViewById(R.id.vend_btn_purchase_1);
        final TextView product2 = findViewById(R.id.vend_btn_purchase_2);
        final TextView product3 = findViewById(R.id.vend_btn_purchase_3);
        viewModel.getVendingMachineProductDisplay(0).observe(this, product1::setText);
        viewModel.getVendingMachineProductDisplay(1).observe(this, product2::setText);
        viewModel.getVendingMachineProductDisplay(2).observe(this, product3::setText);

        // handlers
        findViewById(R.id.vend_btn_return).setOnClickListener(this);
        findViewById(R.id.vend_btn_insert).setOnClickListener(this);
        product1.setOnClickListener(this);
        product2.setOnClickListener(this);
        product3.setOnClickListener(this);
        findViewById(R.id.vend_btn_collect).setOnClickListener(this);

        // build the "Insert Coins" dialog;
        // kata requirements did not specify how and/or how easy it should be to input so, for simplicity, using a text input dialog
        this.dlgInsertCoinsInput = new EditText(this);
        this.dlgInsertCoinsInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.dlgInsertCoins = new AlertDialog.Builder(this)
                .setTitle(R.string.vend_dlg_insert_coin)
                .setMessage(R.string.vend_help_coin)
                .setView(this.dlgInsertCoinsInput)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vend_btn_collect:
                this.viewModel.collectCoins();
                break;
            case R.id.vend_btn_insert:
                this.dlgInsertCoins.show();
                // since display only needs updated after successful insert,
                // exit now since updateDisplay() is called in the click handler callback
                return;
            case R.id.vend_btn_purchase_1:
                this.viewModel.purchaseProduct(0);
                break;
            case R.id.vend_btn_purchase_2:
                this.viewModel.purchaseProduct(1);
                break;
            case R.id.vend_btn_purchase_3:
                this.viewModel.purchaseProduct(2);
                break;
            case R.id.vend_btn_return:
                this.viewModel.returnCoins();
                break;
        }
    }

    @Override
    public void onClick(@NonNull final DialogInterface dialog, final int which) {
        try {
            float usdValue = Float.parseFloat(this.dlgInsertCoinsInput.getText().toString());
            int coinValue = (int) Math.floor(usdValue * 100);

            if (this.viewModel.insertCoin(coinValue)) {
                // since dialog can be reused, reset text when successful Insert Coin was allowed
                this.dlgInsertCoinsInput.setText("");
            } else {
                // ...then tell the user it was not accepted
                Toast.makeText(
                        this,
                        R.string.vend_dlg_invalid,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (NumberFormatException exc) {
            Toast.makeText(
                    this,
                    R.string.vend_dlg_invalid,
                    Toast.LENGTH_SHORT)
                    .show();
        }
        dialog.dismiss();
    }
}
