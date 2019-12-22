package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.EDFAnnotation;
import com.example.eeganalysistoolkit.model.EDFParser;
import com.example.eeganalysistoolkit.model.EDFParserResult;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class TestEDFActivity extends AppCompatActivity {

    private static final int PERMISSION_READ_WRITE_REQUEST_CODE = 155 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_edf);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select EDF File"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.i("mkkk",""+filePath);
            Log.i("mkkk",""+data.getDataString());
            Log.i("mkkk",""+getFilesDir());
            String[] split = data.getDataString().split(":");
            Log.i("mkkk",Environment.getExternalStorageState()+split[1]);
            try {
                test(Environment.getExternalStorageDirectory()+split[1]+".edf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_READ_WRITE_REQUEST_CODE : {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //yeee permission granted
                    showFileChooser();
                }
            }
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this ,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                        Manifest.permission.READ_EXTERNAL_STORAGE} ,
                PERMISSION_READ_WRITE_REQUEST_CODE);
    }



    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static EDFParserResult result = null;
    private void test(String filee) throws IOException {


        File  file = new File(filee);


        InputStream is = null;
        FileOutputStream fos = null;
        InputStream format = null;
        try
        {
            is = new FileInputStream(file);
            result = EDFParser.parseEDF(is);
            is.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }




    }


    private static void writeHeaderData(OutputStream os, String pattern) throws IOException
    {
        String message = MessageFormat.format(pattern, result.getHeader().getIdCode().trim(), result.getHeader()
                .getSubjectID().trim(), result.getHeader().getRecordingID().trim(), result.getHeader().getStartDate()
                .trim(), result.getHeader().getStartTime().trim(), result.getHeader().getBytesInHeader(), result
                .getHeader().getFormatVersion().trim(), result.getHeader().getNumberOfRecords(), result.getHeader()
                .getDurationOfRecords(), result.getHeader().getNumberOfChannels());
        os.write(message.getBytes("UTF-8"));
    }

    private static void writeChannelData(OutputStream os, String pattern, int i) throws IOException
    {
        String message = MessageFormat.format(pattern, result.getHeader().getChannelLabels()[i].trim(), result
                .getHeader().getTransducerTypes()[i].trim(), result.getHeader().getDimensions()[i].trim(), result
                .getHeader().getMinInUnits()[i], result.getHeader().getMaxInUnits()[i], result.getHeader()
                .getDigitalMin()[i], result.getHeader().getDigitalMax()[i], result.getHeader().getPrefilterings()[i]
                .trim(), result.getHeader().getNumberOfSamples()[i], new String(result.getHeader().getReserveds()[i])
                .trim());
        os.write(message.getBytes("UTF-8"));
    }

    private static String getPattern(InputStream is)
    {
        StringBuilder str = new StringBuilder();
        Scanner scn = null;
        try
        {
            scn = new Scanner(is);
            while (scn.hasNextLine())
                str.append(scn.nextLine()).append("\n");
        } finally
        {
            close(scn);
        }
        return str.toString();
    }

    private static final void close(Closeable c)
    {
        try
        {
            c.close();
        } catch (Exception e)
        {
            // do nothing
        }
    }

}
