package com.adair.core2.base.function

/**
 * 功能管理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 10:21
 */
object FunctionsManager {

    private val activityFunctionClassList: MutableList<Class<in BaseActivityFunction>> = mutableListOf()
    private val fragmentFunctionClassList: MutableList<Class<in BaseFragmentFunction>> = arrayListOf()


    /** 添加activity公共的功能类路径，全路径 */
    fun addCommonActivityFunctionClassPath(className: Class<in BaseActivityFunction>) {
        activityFunctionClassList.add(className)
    }

    /** 添加fragment 功能类，全路径 */
    fun addFragmentActivityFunctionClassPath(className: Class<in BaseFragmentFunction>) {
        fragmentFunctionClassList.add(className)
    }


    fun createActivityFunctions(): MutableList<BaseActivityFunction> {
        val functions = mutableListOf<BaseActivityFunction>()

        for (classs in activityFunctionClassList) {
            var function: BaseActivityFunction? = null
            try {
                function = classs.newInstance() as BaseActivityFunction?
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            function?.run {
                functions.add(this)
            }
        }
        return functions
    }

    fun createFragmentFunctions(): MutableList<BaseFragmentFunction> {
        val functions = mutableListOf<BaseFragmentFunction>()
        for (functionClass in fragmentFunctionClassList) {
            var function: BaseFragmentFunction? = null
            try {
                function = functionClass.newInstance() as BaseFragmentFunction?
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            function?.run {
                functions.add(this)
            }
        }
        return functions
    }
}