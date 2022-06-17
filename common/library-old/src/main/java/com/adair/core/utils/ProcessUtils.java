package com.adair.core.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/8/6 15:54
 */
public class ProcessUtils {


	/**
	 * 判断当前进程是否是主进程
	 *
	 * @return true 是主进程,false,不是主进程
	 */
	public static boolean isMainProcess(Context context) {
		String processName = getProcessName(android.os.Process.myPid());
		if (processName == null) {
			return false;
		}
		return processName.equals(context.getPackageName());
	}


	/**
	 * 获取当前进程名
	 */
	public static String getProcessName(int pid) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
			String processName = reader.readLine();
			if (!TextUtils.isEmpty(processName)) {
				processName = processName.trim();
			}
			return processName;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return null;
	}

}
