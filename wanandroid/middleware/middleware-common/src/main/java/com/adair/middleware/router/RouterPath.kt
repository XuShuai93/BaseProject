package com.adair.middleware.router

/**
 *
 * ARouter 相关Path
 *
 * @author xushuai
 * @date   2022/5/21-11:37
 * @email  466911254@qq.com
 */
class RouterPath {

    class MainRouter {
        companion object {
            private const val MAIN_GROUP = "/main"
            const val SPLASH_ACTIVITY = "$MAIN_GROUP/SplashActivity"

            const val MAIN_ACTIVITY = "$MAIN_GROUP/MainActivity"
        }
    }

}