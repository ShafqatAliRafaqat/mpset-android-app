package com.mpset.pokerevents.Helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

object ImageHelperJava {
    //    public static String imgToBase64(String path){
    //
    //        String emptyImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+AMAAQIA/mGM61QAAAAASUVORK5CYII=";
    //
    //        File imagefile = new File(path);
    //        FileInputStream fis = null;
    //
    //        try {
    //            fis = new FileInputStream(imagefile);
    //        } catch (FileNotFoundException e) {
    //            return emptyImage;
    //        }
    //
    //        Bitmap bm = BitmapFactory.decodeStream(fis);
    //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
    //        byte[] b = baos.toByteArray();
    //        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
    //        //Base64.de
    //        return encImage;
    //
    //    }
    fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val b = baos.toByteArray()

        return Base64.encodeToString(b, Base64.DEFAULT)
    }

//    fun encodeImagePath(path: String): String {
//        val imagefile = File(path)
//        var fis: FileInputStream? = null
//        try {
//            fis = FileInputStream(imagefile)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//
//        val bm = BitmapFactory.decodeStream(fis)
//        val baos = ByteArrayOutputStream()
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val b = baos.toByteArray()
////Base64.de
//        return Base64.encodeToString(b, Base64.DEFAULT)
//
//    }
}
