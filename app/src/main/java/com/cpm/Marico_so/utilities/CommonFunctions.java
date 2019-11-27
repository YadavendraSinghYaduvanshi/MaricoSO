package com.cpm.Marico_so.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.cpm.Marico_so.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

/**
 * Created by deepakp on 2/16/2017.
 */

public class CommonFunctions {

    public static String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        return formatter.format(m_cal.getTime());
    }

    public static void startAnncaCameraActivity(Context context, final String path, Fragment fragment, final boolean showGrid) {
        //final AnncaConfiguration.Builder dialogDemo = new AnncaConfiguration.Builder((Activity) context, CommonString.CAPTURE_MEDIA);
        final AnncaConfiguration.Builder dialogDemo;
        if (fragment == null) {
            dialogDemo = new AnncaConfiguration.Builder((Activity) context, CommonString.CAPTURE_MEDIA);
        } else {
            dialogDemo = new AnncaConfiguration.Builder(fragment, CommonString.CAPTURE_MEDIA);
        }
        dialogDemo.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);
        dialogDemo.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_horizontal_camera);
        dialog.setCancelable(false);
        if (dialog != null && (!dialog.isShowing())) {
            dialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                new Annca(dialogDemo.build()).launchCamera(path,showGrid);
            }
        }, 1000);
    }


    public static void setScaledImage(ImageView imageView, final String path) {
        final ImageView iv = imageView;
        ViewTreeObserver viewTreeObserver = iv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = iv.getMeasuredHeight();
                int imageViewWidth = iv.getMeasuredWidth();
                iv.setImageBitmap(decodeSampledBitmapFromPath(path, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }

    private static Bitmap decodeSampledBitmapFromPath(String path,
                                                      int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static String getCurrentTimeHHMMSS() {
        Calendar m_cal = Calendar.getInstance();
        return m_cal.get(Calendar.HOUR_OF_DAY) + ""
                + m_cal.get(Calendar.MINUTE) + "" + m_cal.get(Calendar.SECOND);
    }


   public static Bitmap convertBitmap(String path) {
       Bitmap bitmap = null;
       BitmapFactory.Options ourOptions = new BitmapFactory.Options();
       ourOptions.inDither = false;
       ourOptions.inPurgeable = true;
       ourOptions.inInputShareable = true;
       ourOptions.inTempStorage = new byte[32 * 1024];
       File file = new File(path);
       FileInputStream fs = null;
       try {
           fs = new FileInputStream(file);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       try {
           if (fs != null) {
               bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, ourOptions);
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (fs != null) {
               try {
                   fs.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
       return bitmap;
   }

}
