package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    ImageView imageView;
    TextView textView, price, desc;
    SharedPreferences sp;
    Button buyNow, addCart, removeCart, addWishList, removeWishlist;
    Checkout checkout;

    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = openOrCreateDatabase("Internship", MODE_PRIVATE, null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER(10),USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20),PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishListTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishListTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.product_detail_image);
        textView = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        desc = findViewById(R.id.product_detail_desc);

        buyNow = findViewById(R.id.product_detail_buy_now);
        addCart = findViewById(R.id.product_detail_add_to_cart);
        removeCart = findViewById(R.id.product_detail_remove_cart);
        addWishList = findViewById(R.id.product_detail_wishlist);
        removeWishlist = findViewById(R.id.product_detail_wishlist_remove);

        imageView.setImageResource(sp.getInt(ConstantSp.PRODUCT_IMAGE, 0));
        textView.setText(sp.getString(ConstantSp.PRODUCT_NAME, ""));
        price.setText(ConstantSp.PRICE_SYMBOL + sp.getString(ConstantSp.PRODUCT_PRICE, ""));
        desc.setText(sp.getString(ConstantSp.PRODUCT_DESC, ""));

        Checkout.preload(getApplicationContext());

        String selectCartQuery = "SELECT * FROM CART WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"' AND ORDERID = '0'";
        Cursor cursorCart = db.rawQuery(selectCartQuery, null);
        if(cursorCart.getCount()>0) {
            addCart.setVisibility(View.GONE);
            removeCart.setVisibility(View.VISIBLE);
        } else {
            addCart.setVisibility(View.VISIBLE);
            removeCart.setVisibility(View.GONE);
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectQuery = "SELECT * FROM CART WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"' AND ORDERID = '0'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                if(cursor.getCount()>0) {
                    new CommonMethod(ProductDetailActivity.this, "Product is already present in cart");
                } else {
                    int iQty = 1;
                    int iTotalPrice = iQty * Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, ""));

                    String insertQuery = "INSERT INTO CART VALUES(NULL, '0', '" + sp.getString(ConstantSp.ID, "") + "', '" + sp.getString(ConstantSp.PRODUCT_ID, "") + "', '" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "', '" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "', '" + sp.getString(ConstantSp.PRODUCT_DESC, "") + "', '" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "', '" + iQty + "', '" + iTotalPrice + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product added to the cart");
                    addCart.setVisibility(View.GONE);
                    removeCart.setVisibility(View.VISIBLE);
                }
            }
        });

        removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM CART WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"' AND ORDERID = '0'";
                db.execSQL(deleteQuery);
                new CommonMethod(ProductDetailActivity.this, "Product removed from cart");
                addCart.setVisibility(View.VISIBLE);
                removeCart.setVisibility(View.GONE);
            }
        });

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0) {
            addWishList.setVisibility(View.GONE);
            removeWishlist.setVisibility(View.VISIBLE);
        } else {
            addWishList.setVisibility(View.VISIBLE);
            removeWishlist.setVisibility(View.GONE);
        }

        removeWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM WISHLIST WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"'";
                db.execSQL(deleteQuery);
                new CommonMethod(ProductDetailActivity.this, "Product removed from wishlist");
                addWishList.setVisibility(View.VISIBLE);
                removeWishlist.setVisibility(View.GONE);
            }
        });

        addWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectQuery = "SELECT * FROM WISHLIST WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"' AND PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID, "")+"'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                if(cursor.getCount()>0) {
                    new CommonMethod(ProductDetailActivity.this, "Product is already present in wishlist");
                } else {

                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL, '" + sp.getString(ConstantSp.ID, "") + "', '" + sp.getString(ConstantSp.PRODUCT_ID, "") + "', '" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "', '" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "', '" + sp.getString(ConstantSp.PRODUCT_DESC, "") + "', '" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product added to the wishlist");
                    addWishList.setVisibility(View.GONE);
                    removeWishlist.setVisibility(View.VISIBLE);
                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_BW6Y7dbcgp5ez4");

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, ""))*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sp.getString(ConstantSp.EMAIL, ""));
            preFill.put("contact", sp.getString(ConstantSp.CONTACT, ""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{
            Toast.makeText(this, "Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData(), Toast.LENGTH_SHORT);
            Log.d("RESPONSE_SUCCESS","Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{

            Toast.makeText(this, "Payment Failed:\nPayment Data: "+paymentData.getData(), Toast.LENGTH_SHORT);
            Log.d("RESPONSE_FAIL","Payment Failed:\nPayment Data: "+paymentData.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}