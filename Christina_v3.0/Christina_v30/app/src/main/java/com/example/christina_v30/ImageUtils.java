package com.example.christina_v30;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageUtils {
    public static final int ALBUM_REQUEST_CODE = 913;
    public Uri ImageUri;
    private Uri TempUri;
    public File ImageFile;
    private Context context;

    public ImageUtils(Context context){
        super();
        this.context = context;
    }

    public void ByAlbum(){
        try {
            File picFileDir = new File(Environment.getExternalStorageDirectory(), "/icon");
            if(!picFileDir.exists()){
                picFileDir.mkdirs();
            }
            ImageFile = new File(picFileDir, SystemClock.currentThreadTimeMillis() + ".png");
            if(!ImageFile.exists()){
                ImageFile.createNewFile();
            }
            TempUri = Uri.fromFile(ImageFile);
            final Intent intent = cutImageByAlbumIntent();
            ((LoginCenter) context).startActivityForResult(intent, ALBUM_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Intent cutImageByAlbumIntent(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, TempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    public Bitmap decodeBitmap(){
        Bitmap bitmap = null;
        try {
            if(TempUri != null){
                ImageUri = TempUri;
                bitmap = BitmapFactory.decodeStream(context
                        .getContentResolver().openInputStream(ImageUri));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
