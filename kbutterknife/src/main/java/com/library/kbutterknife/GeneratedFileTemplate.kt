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

const val INJECTION = "        activity.%s = activity.findViewById(%s)"