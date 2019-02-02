package com.example.guestuser.damirkrkalicearthgoods.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class MyAsyncImageInflater extends AsyncTask<String, Void, Bitmap> {
    ImageView image;

    public MyAsyncImageInflater(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new URL(urldisplay).openStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            Bitmap decodedBitmap = BitmapFactory.decodeStream(in, null, options);

            bitmap = Bitmap.createScaledBitmap(decodedBitmap, 300, 300, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}
