package com.library.kbutterknife

/**
 * eg.
 *  package com.example.butterknife
 *
 *  class MainActivity_ViewInjector() {
 *      fun inject(activity: MainActivity) {
 *          activity.tv = activity.findViewById(R.id.tv)
 *      }
 *  }
 */
const val TEMPLATE = """package %s
                
class %s() {
    fun inject(activity: %s) {
%s
    }
}"""

const val FIELD_INJECTION = "        activity.%s = activity.findViewById(%s)"
const val METHOD_INJECTION = "        activity.findViewById<android.view.View>(%s).setOnClickListener { activity.%s(%s) }"