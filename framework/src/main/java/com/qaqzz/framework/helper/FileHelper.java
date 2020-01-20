package com.qaqzz.framework.helper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.qaqzz.framework.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
     * 裁剪
     *
     * @param mActivity
     * @param file
     */
    public void startPhotoZoom(Activity mActivity, File file) {
        LogUtils.i("startPhotoZoom" + file.getPath());
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mActivity, "com.imooc.meet.fileprovider", file);
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
}
