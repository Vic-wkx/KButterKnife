package com.library.kbutterknife

/**
 * @param id view id
 * @param fieldName variable name of view when using [BindView]
 * @param methodName method name when using [OnClick]
 * @param methodType the type of method parameter when using [OnClick]
 */
data class InjectionPoint(val id: Int,
                          val fieldName: String? = null,
                          val methodName: String? = null,
                          val methodType: String? = null)