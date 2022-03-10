package com.adair.utils

import android.content.Context
import android.os.Process
import java.io.BufferedReader
import java.io.FileReader

/**
 * 进程信息相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/24 10:32
 */
class ProcessUtils private constructor() {

    companion object {

        /**
         * 获取进程 指定进程id的进程名称
         * @param pid Int
         * @return String
         */
        @JvmStatic
        fun getProcessName(pid: Int): String? {
            var reader: BufferedReader? = null

            try {
                reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
                var processName: String? = reader.readLine()
                if (!processName.isNullOrBlank()) {
                    processName = processName.trim()
                }
                return processName
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    reader?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }

        /**
         * 判断是否主进程
         * @param context Context 上下文对象
         * @return Boolean true 当前进程是主进程,false 当前进程是子进程
         */
        @JvmStatic
        fun isMainProcess(context: Context): Boolean {
            val processName = getProcessName(Process.myPid())
            return if (processName == null) {
                false
            } else {
                processName == context.packageName
            }
        }
    }
}