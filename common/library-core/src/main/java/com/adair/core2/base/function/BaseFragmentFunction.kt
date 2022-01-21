package com.adair.core2.base.function

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks

/**
 * fragment 生命周期功能
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 10:09
 */
class BaseFragmentFunction {
    fun onAttached(manager: FragmentManager, fragment: Fragment, context: Context) {}

    fun onCreated(manager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {}

    fun onViewCreated(manager: FragmentManager, fragment: Fragment, iew: View, savedInstanceState: Bundle?) {}

    fun onStarted(manager: FragmentManager, fragment: Fragment) {}

    fun onResumed(manager: FragmentManager, fragment: Fragment) {}

    fun onPaused(manager: FragmentManager, fragment: Fragment) {}

    fun onStopped(manager: FragmentManager, fragment: Fragment) {}

    fun onViewDestroyed(manager: FragmentManager, fragment: Fragment) {}

    fun onDestroyed(manager: FragmentManager, fragment: Fragment) {}

    fun onDetached(manager: FragmentManager, fragment: Fragment) {}

    fun onSaveInstanceStated(manager: FragmentManager, fragment: Fragment, outState: Bundle) {}

    fun setUserVisibleHinted(manager: FragmentManager, fragment: Fragment, isVisibleToUser: Boolean) {}
}