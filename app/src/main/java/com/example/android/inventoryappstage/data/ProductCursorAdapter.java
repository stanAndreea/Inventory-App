package com.example.android.inventoryappstage.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappstage.R;
import com.example.android.inventoryappstage.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.name_product);
        TextView priceView = view.findViewById(R.id.price_product);
        TextView quantityView = view.findViewById(R.id.quantity_product);
        ImageView shopImage = view.findViewById(R.id.shop_image_view);

        int nameColumn = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumn = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumn = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        final int productIdColumnIndex = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));

        String nameCurrent = cursor.getString(nameColumn);
        double priceCurrent = cursor.getDouble(priceColumn);
        final int quantityCurrent = cursor.getInt(quantityColumn);

        nameView.setText(nameCurrent);
        priceView.setText(String.valueOf(priceCurrent));
        quantityView.setText(String.valueOf(quantityCurrent));

        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productIdColumnIndex);
                adjustProductQuantity(context, productUri, quantityCurrent);
            }
        });
    }

    private void adjustProductQuantity(Context context, Uri productUri, int currentQuantityInStock) {

        int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

        if (currentQuantityInStock == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.toast_out_of_stock_msg, Toast.LENGTH_SHORT).show();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantityValue);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);
        if (numRowsUpdated > 0) {
            Toast.makeText(context.getApplicationContext(), R.string.buy_msg_confirm, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context.getApplicationContext(), R.string.no_product_in_stock, Toast.LENGTH_SHORT).show();

        }
    }
}
