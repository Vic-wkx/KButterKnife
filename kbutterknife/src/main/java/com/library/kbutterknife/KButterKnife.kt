package com.library.kbutterknife

import android.app.Activity

/**
 * Created by Kevin 2021-07-21
 */
object KButterKnife {
    fun inject(activity: Activity) {
        val injector = Class.forName(activity.javaClass.name + SUFFIX)
        val method = injector.getMethod("inject", activity.javaClass)
        method.invoke(injector.getDeclaredConstructor().newInstance(), activity)
    }
}