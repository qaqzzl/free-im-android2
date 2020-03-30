package com.qaqzz.framework.helper;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.qaqzz.framework.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * FileName: FileHelper
 * Founder: LiuGuiLin
 * Profile: 关于文件的帮助类
 */
public class FileHelper {

    //相机
    public static final int CAMEAR_REQUEST_CODE = 1004;
    //相册
    public static final int ALBUM_REQUEST_CODE = 1005;
    //音乐
    public static final int MUSIC_REQUEST_CODE = 1006;
    //视频
    public static final int VIDEO_REQUEST_CODE = 1007;

    //裁剪结果
    public static final int CAMERA_CROP_RESULT = 1008;

    private static volatile FileHelper mInstnce = null;

    private File tempFile = null;
    private Uri imageUri;
    private SimpleDateFormat simpleDateFormat;

    //裁剪文件
    private String cropPath;

    private FileHelper() {
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    }

    public static FileHelper getInstance() {
        if (mInstnce == null) {
            synchronized (FileHelper.class) {
                if (mInstnce == null) {
                    mInstnce = new FileHelper();
                }
            }
        }
        return mInstnce;
    }

    public File getTempFile() {
        return tempFile;
    }

    public String getCropPath() {
        return cropPath;
    }

    /**
     * 相机
     * 如果头像上传，可以支持裁剪，自行增加
     *
     * @param mActivity
     */
    public void toCamera(Activity mActivity) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String fileName = simpleDateFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");
            //兼容Android N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(tempFile);
            } else {
                //利用FileProvider
                imageUri = FileProvider.getUriForFile(mActivity,
                        mActivity.getPackageName() + ".fileprovider", tempFile);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(intent, CAMEAR_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(mActivity, "无法打开相机", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 跳转到相册
     *
     * @param mActivity
     */
    public void toAlbum(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 跳转音乐
     *
     * @param mActivity
     */
    public void toMusic(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        mActivity.startActivityForResult(intent, MUSIC_REQUEST_CODE);
    }

    /**
     * 跳转视频
     *
     * @param mActivity
     */
    public void toVideo(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        mActivity.startActivityForResult(intent, VIDEO_REQUEST_CODE);
    }


    /**
     * 通过Uri去系统查询真实地址
     *
     * @param uri
     */
    public String getRealPathFromURI(Context mContext, Uri uri) {
        String realPath = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(mContext, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    realPath = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPath;
    }

    /**
     * 获取网络视频第一帧
     *
     * @param videoUrl
     * @return
     */
    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }


    /**
     * 获取视频的缩略图
     * @param videoPath 视频的路径
     * @return Bitmap
     */
    //如果指定的视频的宽高都大于了MICRO_KIND的大小，那么你就使用MINI_KIND就可以了
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        return  media.getFrameAtTime();
    }

    //  Bitmap to FILE
    public static File BitmapToFile(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 20) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩 友盟缩略图要求不大于18kb
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        Log.d("=-=-=-=-=-", "compressImage: " + file);
        // recycleBitmap(bitmap);
        return file;
    }

    /**
     * 裁剪
     *
     * @param mActivity
     * @param file
     */
    public void startPhotoZoom(Activity mActivity, File file) {
        LogUtils.i("startPhotoZoom" + file.getPath());
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mActivity, "com.qaqzz.free_im.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        if (uri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        //intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //单独存储裁剪文件，解决手机兼容性问题
        cropPath = Environment.getExternalStorageDirectory().getPath() + "/" + "meet.jpg";
        Uri mUriTempFile = Uri.parse("file://" + "/" + cropPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriTempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mActivity.startActivityForResult(intent, CAMERA_CROP_RESULT);
    }


    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param fileName 文件名称
     * @param bitmap   图片
     * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
     */
    public String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    /**
     * 保存文件
     *
     * @param c
     * @param filePath
     * @param fileName
     * @param bitmap
     * @return
     */
    public String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    /**
     * Bitmap转Byte
     *
     * @param bm
     * @return
     */
    private byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 保存文件
     *
     * @param c
     * @param filePath
     * @param fileName
     * @param bytes
     * @return
     */
    private String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/Meet/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 保存Bitmap到相册
     *
     * @param mContext
     * @param mBitmap
     */
    public boolean saveBitmapToAlbum(Context mContext, Bitmap mBitmap) {
        //根布局
        File rootPath = new File(Environment.getExternalStorageDirectory() + "/Meet/");

        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        File file = new File(rootPath, System.currentTimeMillis() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            //自带的保存方法
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
            updateAlnum(mContext, file.getPath());
            return true;
        } catch (IOException e) {
            LogUtils.i("e:" + e.toString());
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 刷新图库
     */
    private void updateAlnum(Context mContext, String path) {
        /**
         * 存在兼容性的问题
         */

        try {
            //通过广播刷新图库
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));

            //通过数据库的方式插入
            ContentValues values = new ContentValues(4);
            values.put(MediaStore.Video.Media.TITLE, "");
            values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Video.Media.DATA, path);
            values.put(MediaStore.Video.Media.DURATION, 0);
            mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




}
