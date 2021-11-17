package com.adair.core.utils;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文件IO操作
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/20
 */
public class IOUtils {

    /**
     * 读取文件信息
     *
     * @param file        文件信息
     * @param charsetName 文件编码格式
     *
     * @return 文件内容
     *
     * @throws IOException 文件读取异常
     */
    public static String readFileToString(File file, String charsetName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charsetName);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    /**
     * 读取Assets文件信息
     *
     * @param assetManager assets文件管理
     * @param path         文件信息
     * @param charsetName  文件编码格式
     *
     * @return 文件内容
     *
     * @throws IOException 文件读取异常
     */
    public static String readAssetsFileToString(AssetManager assetManager, String path, String charsetName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(path), charsetName);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }
}
