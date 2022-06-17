package com.adair.core.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import com.adair.core.utils.BitmapUtil;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/20
 */
public class BitmapLruCache {

	private static final String File_PREFIX = "file:";
	private static final String RESOURCE_PREFIX = "resource:";
	private static final String ASSETS_PREFIX = "assets:";

	private static final String SIZE_SUFFIX = "_size";

	/** 资源管理器 */
	private Resources mResources;

	private final LruCache<String, Bitmap> mLruCache;

	public BitmapLruCache(Resources resources) {
		this.mResources = resources;
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
				return value.getByteCount();
			}

			@Nullable
			@Override
			protected Bitmap create(@NonNull String key) {
				return createBitmap(key);
			}

			@Override
			protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
				if (evicted && !oldValue.isRecycled()) {
					oldValue.recycle();
				}
			}
		};
	}


	//region 生成缓存Key
	public String getFileKey(String filePath) {
		return File_PREFIX + filePath;
	}

	public String getFileKey(String filePath, int reqWidth, int reqHeight) {
		return File_PREFIX + filePath + "_" + reqWidth + "x" + reqHeight + SIZE_SUFFIX;
	}

	public String getResourceKey(@DrawableRes int resId) {
		return RESOURCE_PREFIX + resId;
	}

	public String getResourceKey(@DrawableRes int resId, int reqWidth, int reqHeight) {
		return RESOURCE_PREFIX + resId + "_" + reqWidth + "x" + reqHeight + SIZE_SUFFIX;
	}

	public String getAssetsKey(String assetsPath) {
		return ASSETS_PREFIX + assetsPath;
	}

	public String getAssetsKey(String assetsPath, int reqWidth, int reqHeight) {
		return ASSETS_PREFIX + assetsPath + "_" + reqWidth + "x" + reqHeight + SIZE_SUFFIX;
	}
	//endregion


	//region 获取bitmap结果

	public Bitmap getBitmap(String key) {
		if (!checkAssetsKey(key) && !checkResourceKey(key) && !checkFileKey(key)) {
			throw new IllegalArgumentException("key is not real key");
		}
		return mLruCache.get(key);
	}

	public Bitmap getFileBitmap(String file) {
		return mLruCache.get(getFileKey(file));
	}

	public Bitmap getFileBitmap(String file, int reqWidth, int reqHeight) {
		return mLruCache.get(getFileKey(file, reqWidth, reqHeight));
	}

	public Bitmap getResourceBitmap(@DrawableRes int resId) {
		return mLruCache.get(getResourceKey(resId));
	}

	public Bitmap getResourceBitmap(@DrawableRes int resId, int reqWidth, int reqHeight) {
		return mLruCache.get(getResourceKey(resId, reqWidth, reqHeight));
	}

	public Bitmap getAssetsBitmap(String assetsFilePath) {
		if (checkAssetsKey(assetsFilePath)) {
			throw new IllegalArgumentException("assets key is error");
		}
		return mLruCache.get(getAssetsKey(assetsFilePath));
	}

	public Bitmap getAssetsBitmap(String assetsFilePath, int reqWidth, int reqHeight) {
		if (checkAssetsKey(assetsFilePath)) {
			throw new IllegalArgumentException("assets key is error");
		}
		return mLruCache.get(getAssetsKey(assetsFilePath, reqWidth, reqHeight));
	}
	//endregion

	/** 清空缓存 */
	public void clear() {
		mLruCache.evictAll();
	}


	private boolean checkFileKey(String key) {
		return key.startsWith(File_PREFIX);
	}

	private boolean checkResourceKey(String key) {
		return key.startsWith(RESOURCE_PREFIX);
	}

	private boolean checkAssetsKey(String key) {
		return key.startsWith(ASSETS_PREFIX);
	}

	private int[] getWidthAndHeightByKey(String key) {
		int[] result = new int[2];
		if (key.endsWith(SIZE_SUFFIX)) {
			String secondKey = key.substring(0, key.length() - SIZE_SUFFIX.length());
			int index = secondKey.lastIndexOf("_");
			String data = secondKey.substring(index + 1);

			int indexX = data.indexOf("x");
			int width = Integer.parseInt(data.substring(0, indexX));
			int height = Integer.parseInt(data.substring(indexX + 1));
			result[0] = width;
			result[1] = height;
		}
		return result;
	}


	/** 根据Key创建Bitmap */
	private Bitmap createBitmap(String key) {
		if (key.endsWith(SIZE_SUFFIX)) {
			int[] data = getWidthAndHeightByKey(key);
			String secondKey = key.substring(0, key.length() - SIZE_SUFFIX.length());

			int endIndex = secondKey.lastIndexOf("_");
			String pathString;
			if (secondKey.startsWith(File_PREFIX)) {
				pathString = secondKey.substring(File_PREFIX.length(), endIndex);
				return BitmapUtil.decodeFileBitmap(pathString, data[0], data[1]);
			} else if (secondKey.startsWith(RESOURCE_PREFIX)) {
				pathString = secondKey.substring(RESOURCE_PREFIX.length(), endIndex);
				int resId = Integer.parseInt(pathString);
				return BitmapUtil.decodeResourceBitmap(mResources, resId, data[0], data[1]);
			} else if (secondKey.startsWith(ASSETS_PREFIX)) {
				pathString = secondKey.substring(ASSETS_PREFIX.length(), endIndex);
				return BitmapUtil.decodeAssetsBitmap(mResources, pathString, data[0], data[1]);
			} else {
				return null;
			}
		} else {
			if (key.startsWith(File_PREFIX)) {
				String filePath = key.substring(File_PREFIX.length());
				return BitmapUtil.decodeFileBitmap(filePath);
			} else if (key.startsWith(RESOURCE_PREFIX)) {
				String idString = key.substring(RESOURCE_PREFIX.length());
				int id = Integer.parseInt(idString);
				return BitmapUtil.decodeResourceBitmap(mResources, id);
			} else if (key.startsWith(ASSETS_PREFIX)) {
				String filePath = key.substring(ASSETS_PREFIX.length());
				return BitmapUtil.decodeAssetsBitmap(mResources, filePath);
			} else {
				return null;
			}
		}
	}
}
