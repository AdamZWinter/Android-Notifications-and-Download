package edu.greenriver.sdev450.downloadimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    EditText editText_URL;
    Button button_DownloadImage;
    ImageView imageView;
    ProgressDialog progressDialog;
    Button buttonNotify;
    String channelId = "MyNotification";
    int notificationId = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_URL = findViewById(R.id.editTextURL);
        button_DownloadImage = findViewById(R.id.buttonDownloadImage);
        imageView = findViewById(R.id.imageView);
        buttonNotify = findViewById(R.id.button2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap>  //params, progress, result
    {
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Downloading Image...");
            progressDialog.setMessage("Please wait.");
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            //Bitmap bitmap;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            //set bitmap to imageView
            imageView.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    }

    public void onButtonClick(View view){
        String input_url = editText_URL.getText().toString();
        new DownloadImage().execute(input_url);
    }

    public void notifyMe(View view){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle("My notification title");
        builder.setContentText("This is the notification text.");
        builder.setSmallIcon(R.drawable.baseline_agriculture_24);
        builder.setLargeIcon(bitmap);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(notificationId++, builder.build());
    }

}