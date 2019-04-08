package com.devstories.anipointcompany.android.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import com.devstories.anipointcompany.android.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileAsyncTask extends AsyncTask<String, String, Uri> {

    private Context context = null;
    private DownloadedListener downloadedListener = null;

    private ProgressDialog progressDialog;

    public DownloadFileAsyncTask(Context context, DownloadedListener downloadedListener) {
        this.context = context;
        this.downloadedListener = downloadedListener;
    }

    /**
     * Before starting background thread
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Starting download");

        progressDialog = new ProgressDialog(context, R.style.CustomProgressBar);
        progressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large);
        progressDialog.show();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected Uri doInBackground(String... f_url) {
        int count;

        Uri imageUri = null;

        try {
            String root = Environment.getExternalStorageDirectory().toString();

            System.out.println("Downloading");

            URL url = new URL(f_url[0]);

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File photo = File.createTempFile(
                    String.valueOf(System.currentTimeMillis()), /* prefix */
                    ".png", /* suffix */
                    storageDir      /* directory */
            );

            String cameraPath = photo.getAbsolutePath();
            //imageUri = Uri.fromFile(photo);
            imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", photo);


            OutputStream output = new FileOutputStream(photo);
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return imageUri;
    }



    /**
     * After completing background task
     * **/
    @Override
    protected void onPostExecute(Uri file_url) {
        System.out.println("Downloaded");

        if(this.progressDialog != null) {
            progressDialog.dismiss();
        }

        if(this.downloadedListener != null) {
            this.downloadedListener.downloaded(file_url);
        }
    }

    public interface DownloadedListener {
        void downloaded(Uri uri);
    }
}
