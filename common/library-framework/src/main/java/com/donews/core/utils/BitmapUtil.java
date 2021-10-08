package com.donews.core.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2020/7/30
 */
public class BitmapUtil {

    /**
     * 从文件中加载图片
     *
     * @param filePath  文件地址
     * @param reqWidth  加载的图片宽度
     * @param reqHeight 加载的图片高度
     *
     * @return 加载结果
     */
    public static Bitmap decodeFileBitmap(String filePath, int reqWidth, int reqHeight) {
        Bitmap result = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            FileInputStream is = new FileInputStream(new File(filePath));
            result = BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);
            is.close();
            if (result == null) {
                result = BitmapFactory.decodeFile(filePath, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从文件中加载图片
     *
     * @param filePath 文件地址
     *
     * @return 加载结果
     */
    public static Bitmap decodeFileBitmap(String filePath) {
        Bitmap result = null;
        try {
            FileInputStream is = new FileInputStream(new File(filePath));
            result = BitmapFactory.decodeFileDescriptor(is.getFD());
            is.close();
            if (result == null) {
                result = BitmapFactory.decodeFile(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 加载资源图片
     *
     * @param resources 资源管理器
     * @param resId     资源图片id
     * @param reqWidth  需求宽度
     * @param reqHeight 需求高度
     *
     * @return 生成的Bitmap对象
     */
    public static Bitmap decodeResourceBitmap(Resources resources, int resId, int reqWidth, int reqHeight) {
        Bitmap result = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            result = BitmapFactory.decodeResource(resources, resId, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载资源图片
     *
     * @param resources 资源管理器
     * @param resId     资源图片id
     *
     * @return 生成的Bitmap对象
     */
    public static Bitmap decodeResourceBitmap(Resources resources, int resId) {
        Bitmap result = null;
        try {
            result = BitmapFactory.decodeResource(resources, resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 加载Assets文件夹下的图片
     *
     * @param resources  资源管理器
     * @param assetsFile assets文件夹下文件路径
     * @param reqWidth   需求宽度
     * @param reqHeight  需求高度
     *
     * @return 生成的Bitmap对象
     */
    public static Bitmap decodeAssetsBitmap(Resources resources, String assetsFile, int reqWidth, int reqHeight) {
        Bitmap result = null;
        AssetManager assetManager = resources.getAssets();
        try (InputStream inputStream = assetManager.open(assetsFile)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            inputStream.reset();
            result = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载Assets文件夹下的图片
     *
     * @param resources  资源管理器
     * @param assetsFile assets文件夹下文件路径
     *
     * @return 生成的Bitmap对象
     */
    public static Bitmap decodeAssetsBitmap(Resources resources, String assetsFile) {
        Bitmap result = null;
        AssetManager assetManager = resources.getAssets();
        try (InputStream inputStream = assetManager.open(assetsFile)) {
            inputStream.reset();
            result = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算inSimpleSize
     *
     * @param options 包含了测量图片信息的配置信息
     *
     * @return inSimpleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //计算最大的采样率，采样率为2的指数
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 释放bitmap内存
     *
     * @param bitmap 需要释放的bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 判断bitmap是否为null
     *
     * @param bitmap 目标图片
     *
     * @return true bitmap为null,false bitmap不为null
     */
    public static boolean isEmpty(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled() && bitmap.getWidth() != 0 && bitmap.getHeight() != 0;
    }

    /**
     * 判断bitmap是否不为null
     *
     * @param bitmap 目标图片
     *
     * @return true bitmap不为null,false bitmap为null
     */
    public static boolean isNotEmpty(Bitmap bitmap) {
        return !isEmpty(bitmap);
    }

    /**
     * 保存图片
     *
     * @param bitmap       图片对象信息
     * @param saveFilePath 保存路径
     *
     * @return true 保存成功
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String saveFilePath) {
        File file = new File(saveFilePath);
        if (file.exists() && !file.delete()) {
            Logger.d("delete %s is failed", saveFilePath);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(e, "save bitmap to %s is failed", saveFilePath);
        }
        return false;
    }

    /**
     * 压缩图片质量道指定大小,这里仅仅只是压缩在硬盘中的文件大小
     *
     * @param bitmap 图片
     * @param size   指定大小,单位 kb
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int size) {
        int byteLength = 1024;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            if (baos.toByteArray().length / byteLength <= size) {
                return bitmap;
            }
            int options = 95;
            while (baos.toByteArray().length / byteLength > size) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                baos.flush();
                options -= 5;
            }
            //把压缩后的数据baos存放到ByteArrayInputStream中
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            //把ByteArrayInputStream数据生成图片
            return BitmapFactory.decodeStream(isBm, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
