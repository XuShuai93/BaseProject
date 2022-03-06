package com.adair.httpv2

/**
 *
 * 静态属性配置
 *
 * @author xushuai
 * @date   2021/11/10-22:33
 * @email  466911254@qq.com
 */

//连接超时
const val TIMEOUT_CONNECT: Long = 15L
//读取超时
const val TIMEOUT_READ = 30L
//写入超时
const val TIMEOUT_WRITE = 30L
//缓存最大占用
const val CACHE_SIZE = 50L * 1024L * 1024L //缓存大小50M

/**
 * 当接口需要其他url时，配置header, value 为 新的url路径
 */
const val NEW_URL_HEADER = "newUrl"
const val LOG_TAG = "library_network"