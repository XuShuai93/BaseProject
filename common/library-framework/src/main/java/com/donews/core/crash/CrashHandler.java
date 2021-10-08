package com.donews.core.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * App Crash日志记录处理工具
 * <p>
 * created at 2018/6/11 16:28
 *
 * @author XuShuai
 * @version v1.0
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	private static final String FILE_SEP = File.separator;
	private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	/**
	 * 默认的保存日志文件文件夹名称
	 */
	private static final String DEFAULT_PARENT_DIR_NAME = "CrashHandler";

	/**
	 * 默认的Crash处理方式
	 */
	private static Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;

	/**
	 * Crash日志文件头信息，包含发生Crash设备的基本信息
	 */
	private static String sCrashHead;

	/**
	 * 默认Crash日志文件保存地址路径
	 */
	private static String sDefaultDirPath;

	/**
	 * Crash日志文件保存地址路径
	 */
	private static String sDirPath;

	/**
	 * 线程池,使用newSingleThreadExecutor,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去
	 */
	private static ExecutorService sExecutor;

	/**
	 * Context 上下文对象
	 */
	@SuppressLint("StaticFieldLeak")
	private static Context sContext;

	@SuppressLint("StaticFieldLeak")
	private static CrashHandler INSTANCE;

	private CrashHandler() {

	}

	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (CrashHandler.class) {
				if (INSTANCE == null) {
					synchronized (CrashHandler.class) {
						INSTANCE = new CrashHandler();
					}
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 当文件路径的文件不存在时，创建
	 * <p>
	 * created at 2018/6/13 10:56
	 *
	 * @param filePath 文件路径
	 *
	 * @return boolean true文件路径存在，false，文件不存在
	 */
	private static boolean createOrExistsFile(final String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return file.isFile();
		}
		if (!createOrExistsDir(file.getParentFile())) {
			return false;
		}
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 创建文件夹
	 * <p>
	 * created at 2018/6/13 10:57
	 *
	 * @param file 文件
	 *
	 * @return boolean
	 */
	private static boolean createOrExistsDir(final File file) {
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * 判断String是否为空或者全为空格
	 *
	 * @param s String字符串
	 *
	 * @return true字符串为空，false字符串不为空
	 */
	private static boolean isSpace(final String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * Crash初始化，初始化文件保存地址信息,使用默认的文件保存路径
	 *
	 * @param context 上下文对象
	 */
	public void init(Context context) {
		init(context.getApplicationContext(), "");
	}

	/**
	 * Crash初始化，初始化文件保存地址信息
	 * <p>
	 * created at 2018/6/13 10:52
	 *
	 * @param context  上下文对象
	 * @param crashDir 保存日志文件路径
	 */
	public void init(Context context, String crashDir) {
		sContext = context;
		DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();
		sCrashHead = createLogHead(context);

		if (isSpace(crashDir)) {
			sDirPath = null;
		} else {
			sDirPath = crashDir.endsWith(FILE_SEP) ? crashDir : crashDir + FILE_SEP;
		}
		//初始化错误日志保存位置，当外置存储卡可用，放在外置存储里面，否则存在内置数据里面
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File file = context.getExternalFilesDir(DEFAULT_PARENT_DIR_NAME);
			if (file != null) {
				sDefaultDirPath = file.getAbsolutePath().endsWith(FILE_SEP) ? file.getAbsolutePath() : file.getAbsolutePath() + FILE_SEP;
			} else {
				sDefaultDirPath = context.getFilesDir().getAbsolutePath() + FILE_SEP + DEFAULT_PARENT_DIR_NAME + FILE_SEP;
			}
		} else {
			sDefaultDirPath = context.getFilesDir().getAbsolutePath() + FILE_SEP + DEFAULT_PARENT_DIR_NAME + FILE_SEP;
		}
//		Logger.d(sDefaultDirPath);
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 处理Crash的方法,保存Crash的信息
	 *
	 * @param t 发生的线程
	 * @param e 错误信息
	 */
	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull final Throwable e) {
		Date date = new Date(System.currentTimeMillis());
		String fileName = mDateFormat.format(date) + ".txt";
		final String fullPath = (sDirPath == null ? sDefaultDirPath : sDirPath) + fileName;
		if (!createOrExistsFile(fullPath)) {
			return;
		}
		if (sExecutor == null) {
			sExecutor = Executors.newSingleThreadExecutor();
		}
		if (sCrashHead == null) {
			sCrashHead = createLogHead(sContext);
		}
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try (PrintWriter pw = new PrintWriter(new FileWriter(fullPath, false))) {
					pw.write(sCrashHead);
					e.printStackTrace(pw);
					Throwable cause = e.getCause();
					while (cause != null) {
						cause.printStackTrace(pw);
						cause = cause.getCause();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					Logger.e(e1, "save crash info is error!!");
				}
			}
		});
		Logger.e(e, "App Crash\n" + sCrashHead);
		if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
			DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * 创建日志文件头部信息：包含手机设备信息
	 * <p>
	 * created at 2018/6/13 10:56
	 *
	 * @param context 上下文对象
	 *
	 * @return java.lang.String
	 */
	private String createLogHead(Context context) {

		int versionCode = 0;
		String versionName = null;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if (pi != null) {
				versionName = pi.versionName;
				versionCode = pi.versionCode;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			versionName = "获取版本信息错误";
			versionCode = -1;
		}

		String[] abis;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			abis = Build.SUPPORTED_ABIS;
		} else {
			abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
		}
		StringBuilder abiStr = new StringBuilder();
		for (String abi : abis) {
			abiStr.append(abi);
			abiStr.append(',');
		}
		String cpuAbi = abiStr.toString();
		return "************Crash Log Head************\n" +
				"Device Manufacturer : " + Build.MANUFACTURER + "\n" +//设备生产商
				"Device Model        : " + Build.MODEL + "\n" +//手机型号
				"Android Version     : " + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT + "\n" +//SDK版本
				"CPU ABI             : " + cpuAbi + "\n" +  //cpu架构
				"App versionCode     : " + versionCode + "\n" +//App 版本号
				"App VersionName     : " + versionName + "\n" +//App 版本名
				"************Crash Log Head************\n\n";
	}
}
