package com.example.android.inventoryappstage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryappstage.data.ProductContract.ProductEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText nameProductEditText;
    private EditText priceProductEditText;
    private EditText quantityProductEditText;
    private EditText nameSupplierEditText;
    private EditText phoneSupplierEditText;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    private static final int PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    private boolean mProductHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mCurrentProductUri = getIntent().getData();
        ImageView orderImageView = findViewById(R.id.image_view_order);
        final ImageView upImageView = findViewById(R.id.image_view_up);
        final ImageView downImageView = findViewById(R.id.image_view_down);

        if (mCurrentProductUri == null) {
            setTitle(R.string.editor_activity_title_new_product);
            orderImageView.setVisibility(View.GONE);
            upImageView.setVisibility(View.GONE);
            downImageView.setVisibility(View.GONE);
            invalidateOptionsMenu();

        } else {
            setTitle(R.string.editor_activity_title_edit_product);
            orderImageView.setVisibility(View.VISIBLE);
            upImageView.setVisibility(View.VISIBLE);
            downImageView.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        }
        nameProductEditText = findViewById(R.id.edit_text_name);
        priceProductEditText = findViewById(R.id.edit_text_price);
        quantityProductEditText = findViewById(R.id.edit_text_quantity);
        nameSupplierEditText = findViewById(R.id.edit_text_name_supplie);
        phoneSupplierEditText = findViewById(R.id.edit_text_phone);

        nameProductEditText.setOnTouchListener(mTouchListener);
        priceProductEditText.setOnTouchListener(mTouchListener);
        quantityProductEditText.setOnTouchListener(mTouchListener);
        nameSupplierEditText.setOnTouchListener(mTouchListener);
        phoneSupplierEditText.setOnTouchListener(mTouchListener);


        orderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        upImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityString = quantityProductEditText.getText().toString().trim();
                int quantity = Integer.parseInt(quantityString);
                adjustProductQuantity(mCurrentProductUri, quantity, upImageView);
            }
        });
        downImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityString = quantityProductEditText.getText().toString().trim();
                int quantity = Integer.parseInt(quantityString);
                adjustProductQuantity(mCurrentProductUri, quantity, downImageView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validation()) {
                    saveProduct();
                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE};

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameProductColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int nameSupplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int phoneSupplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            String nameProduct = cursor.getString(nameProductColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String nameSupplier = cursor.getString(nameSupplierColumnIndex);
            String phoneSupplier = cursor.getString(phoneSupplierColumnIndex);

            nameProductEditText.setText(nameProduct);
            priceProductEditText.setText(Double.toString(price));
            quantityProductEditText.setText(Integer.toString(quantity));
            nameSupplierEditText.setText(nameSupplier);
            phoneSupplierEditText.setText(phoneSupplier);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameProductEditText.setText("");
        priceProductEditText.setText(String.valueOf(0));
        quantityProductEditText.setText(String.valueOf(0));
        nameSupplierEditText.setText("");
        phoneSupplierEditText.setText("");
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveProduct() {
        String nameString = nameProductEditText.getText().toString().trim();
        String priceString = priceProductEditText.getText().toString().trim();
        String quantityString = quantityProductEditText.getText().toString().trim();
        String nameSupplierString = nameSupplierEditText.getText().toString().trim();
        String phoneSupplierString = phoneSupplierEditText.getText().toString().trim();


        if (mCurrentProductUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(nameSupplierString) && TextUtils.isEmpty(phoneSupplierString)) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, nameSupplierString);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, phoneSupplierString);
        double price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Double.parseDouble(priceString);
        }
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validation() {
        String nameString = nameProductEditText.getText().toString().trim();
        String priceString = priceProductEditText.getText().toString().trim();
        String quantityString = quantityProductEditText.getText().toString().trim();
        String nameSupplierString = nameSupplierEditText.getText().toString().trim();
        String phoneSupplierString = phoneSupplierEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "Name product invalid!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "Price is negative!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, "Quantity is negative!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(nameSupplierString)) {
            Toast.makeText(this, "Name supplier invalid!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidPhone(phoneSupplierString)) {
            Toast.makeText(this, "Phone invalid!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidPhone(String phone) {
        String regexPhone = "\\(?\\+[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?";
        Pattern pattern = Pattern.compile(regexPhone);
        Matcher matcher = pattern.matcher(phone);

        return matcher.find();
    }

    private void makePhoneCall() {
        String phoneNumber = phoneSupplierEditText.getText().toString().trim();
        if (isValidPhone(phoneNumber)) {
            if (ContextCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditorActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
            }

        } else {
            Toast.makeText(this, "Phone invalid!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void adjustProductQuantity(Uri productUri, int currentQuantityInStock, View v) {
        if (v.getId() == R.id.image_view_up) {
            int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock + 1 : 0;

            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantityValue);
            int numRowsUpdated = getContentResolver().update(productUri, contentValues, null, null);
            if (numRowsUpdated > 0) {
                Toast.makeText(getApplicationContext(), R.string.up_quantity, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_product_in_stock, Toast.LENGTH_SHORT).show();

            }
        } else if (v.getId() == R.id.image_view_down) {
            int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

            if (currentQuantityInStock == 0) {
                Toast.makeText(getApplicationContext(), R.string.toast_out_of_stock_msg, Toast.LENGTH_SHORT).show();
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantityValue);
            int numRowsUpdated = getContentResolver().update(productUri, contentValues, null, null);
            if (numRowsUpdated > 0) {
                Toast.makeText(getApplicationContext(), R.string.down_quantity, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_product_in_stock, Toast.LENGTH_SHORT).show();

            }
        }

    }
}
