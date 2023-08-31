package com.facebook.imageloading_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    List<String> imageUrls;
    int currentImageIndex = 0;
    ImageView imgGlide;
    ImageButton leftButton;
    ImageButton rightButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgGlide = findViewById(R.id.imgGlide);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

        try {
            InputStream inputStream = getAssets().open("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonData = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject(jsonData);

            int id = jsonObject.getInt("id");
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");
            double price = jsonObject.getDouble("price");
            double discountPercentage = jsonObject.getDouble("discountPercentage");
            double rating = jsonObject.getDouble("rating");
            int stock = jsonObject.getInt("stock");
            String brand = jsonObject.getString("brand");
            String category = jsonObject.getString("category");
            String thumbnailUrl = jsonObject.getString("thumbnail");
            JSONArray imagesArray = jsonObject.getJSONArray("images");

            // Now you have all the extracted data, you can display it in your views
            // Call a method to populate your views with this data
            populateViews(id, title, description, price, discountPercentage, rating, stock, brand, category, thumbnailUrl, imagesArray);

            // Close the streams
            reader.close();
            inputStream.close();
        } catch (JSONException |IOException e) {
            e.printStackTrace();
        }
    }

    private void populateViews(int id, String title, String description, double price, double discountPercentage, double rating, int stock, String brand, String category, String thumbnailUrl, JSONArray imagesArray) {
        // Find views by their IDs
        ImageView thumbnailImageView = findViewById(R.id.thumbnailImageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView discountTextView = findViewById(R.id.discountTextView);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView stockTextView = findViewById(R.id.stockTextView);
        TextView brandTextView = findViewById(R.id.brandTextView);
        TextView categoryTextView = findViewById(R.id.categoryTextView);

        // Set the extracted data to the views

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priceTextView.setText("Price: $" + price);
        discountTextView.setText("Discount: " + discountPercentage + "% off");
        ratingTextView.setText("Rating: " + rating);
        stockTextView.setText("Stock: " + stock);
        brandTextView.setText("Brand: " + brand);
        categoryTextView.setText("Category: " + category);

        Glide.with(this)
                .load(thumbnailUrl)
                .into(thumbnailImageView);


         imageUrls = new ArrayList<>();
        for (int i = 0; i < imagesArray.length(); i++) {
            imageUrls.add(imagesArray.optString(i));
        }

        loadImage(currentImageIndex);
        updateButtonVisibility();

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the previous image in the list
                if (currentImageIndex > 0) {
                    currentImageIndex--;
                    loadImage(currentImageIndex);
                    updateButtonVisibility();
                }
            }
        });

       rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the next image in the list
                if (currentImageIndex < imageUrls.size() - 1) {
                    currentImageIndex++;
                    loadImage(currentImageIndex);
                    updateButtonVisibility();
                }
            }
        });
    }
    private void loadImage(int index) {
        Glide.with(this)
                .load(imageUrls.get(index))
                .placeholder(R.drawable.ic_launcher_background) // Add your placeholder image resource
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache all versions of the image
                .error(R.drawable.ic_launcher_foreground) // Add your error image resource
                .into(imgGlide);
    }
    private void updateButtonVisibility() {
        if (currentImageIndex == 0) {
            leftButton.setVisibility(View.GONE);
            rightButton.setVisibility(View.VISIBLE);
        } else if (currentImageIndex == imageUrls.size() - 1) {
            leftButton.setVisibility(View.VISIBLE);
          rightButton.setVisibility(View.GONE);
        } else {
           leftButton.setVisibility(View.VISIBLE);
           rightButton.setVisibility(View.VISIBLE);
        }
    }

}