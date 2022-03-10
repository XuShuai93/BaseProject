package com.adair.utils.crash

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess

/**
 * 崩溃日志收集处理器,单例模式
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/27 15:27
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    /**默认的保存日志文件文件夹名称*/
    private const val DEFAULT_PARENT_DIR_NAME = "CrashHandler"

    private const val TAG = "CrashHandler"

    /** 文件路径分隔符,"/"和"\"，屏蔽系统差异 */
    private val FILE_SEP: String = File.separator

    /** 日期时间格式化 */
    private val mDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    /**默认的Crash处理方式*/
    private var mDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    /**Crash日志文件头信息，包含发生Crash设备的基本信息*/
    private var mCrashHead: String = ""

    /**默认Crash日志文件保存地址路径*/
    private var mDefaultDirPath: String? = null

    /**Crash日志文件保存地址路径*/
    private var mDirPath: String? = null

    /**线程池,使用newSingleThreadExecutor,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去*/
    private var mExecutor: ExecutorService? = null

    /**Context 上下文对象，使用弱引用*/
    private var mContext: WeakReference<Context>? = null

    @JvmStatic
    fun init(application: Application, crashSaveDir: String = "") {
        mContext = WeakReference(application)
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        mCrashHead = createLogHead(application)

        mDirPath = if (crashSaveDir.isBlank()) {
            null
        } else {
            if (crashSaveDir.endsWith(FILE_SEP)) crashSaveDir else crashSaveDir + FILE_SEP
        }

        //初始化错误日志保存位置，当外置存储卡可用，放在外置存储里面，否则存在内置数据里面
        mDefaultDirPath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File? = application.getExternalFilesDir(DEFAULT_PARENT_DIR_NAME)
            file?.let {
                if (it.absolutePath.endsWith(FILE_SEP)) it.absolutePath else it.absolutePath + FILE_SEP
            } ?: kotlin.run {
                application.filesDir.absolutePath + FILE_SEP + DEFAULT_PARENT_DIR_NAME + FILE_SEP
            }
        } else {
            application.filesDir.absolutePath + FILE_SEP + DEFAULT_PARENT_DIR_NAME + FILE_SEP
        }

        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, e: Throwable) {
        val date = Date(System.currentTimeMillis())
        val fileName: String = mDateFormat.format(date) + ".txt"
        val fullPath = (if (mDirPath == null) mDefaultDirPath else mDirPath) + fileName
        if (!createOrExistsFile(fullPath)) {
            return
        }
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadExecutor()
        }

        if (mCrashHead.isBlank() && mContext != null && mContext!!.get() != null) {
            mCrashHead = createLogHead(mContext!!.get()!!)
        }
        mExecutor!!.execute {
            try {
                PrintWriter(FileWriter(fullPath, false)).use { pw ->
                    pw.write(mCrashHead)
                    e.printStackTrace(pw)
                    var cause = e.cause
                    while (cause != null) {
                        cause.printStackTrace(pw)
                        cause = cause.cause
                    }
                }
            } catch (e1: IOException) {
                e1.printStackTrace()
                Log.e(TAG, "save crash info is error!!", e1)
            }
        }
        Log.e(TAG, "App Crash \n $mCrashHead".trimIndent())
        mDefaultUncaughtExceptionHandler?.uncaughtException(t, e) ?: kotlin.run {
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }

    /**
     * 创建日志文件头部信息：包含手机设备信息
     * @param context 上下文对象
     * @return java.lang.String
     */
    private fun createLogHead(context: Context): String {
        var versionCode = 0
        var versionName: String? = null
        try {
            val pi = context.packageManager.getPackageInfo(context.packageName, 0)
            if (pi != null) {
                versionName = pi.versionName
                versionCode = pi.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            versionName = "获取版本信息错误"
            versionCode = -1
        }
        val abis: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
        }
        val abiStr = StringBuilder()
        for (abi in abis) {
            abiStr.append(abi)
            abiStr.append(',')
        }
        val cpuAbi = abiStr.toString()
        return """
             ************Crash Log Head************
             Device Manufacturer : ${Build.MANUFACTURER}
             Device Model        : ${Build.MODEL}
             Android Version     : ${Build.VERSION.RELEASE}_${Build.VERSION.SDK_INT}
             CPU ABI             : $cpuAbi
             App versionCode     : $versionCode
             App VersionName     : $versionName
             ************Crash Log Head************
            
             """.trimIndent()
    }

    /**
     * 当文件路径的文件不存在时，创建
     * *
     * @param filePath 文件路径
     *
     * @return boolean true文件路径存在，false，文件不存在
     */
    private fun createOrExistsFile(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) {
            return file.isFile
        }
        return if (!createOrExistsDir(file.parentFile)) {
            false
        } else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 创建文件夹
     *
     *
     * created at 2018/6/13 10:57
     *
     * @param file 文件
     *
     * @return boolean
     */
    private fun createOrExistsDir(file: File?): Boolean {
        return file?.let {
            if (it.exists()) it.isDirectory else it.mkdirs()
        } ?: kotlin.run {
            false
        }
    }
}