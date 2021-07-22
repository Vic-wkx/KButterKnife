package com.library.kbutterknife

/**
 * @param variableName variable name of view
 * @param value view id
 */
data class InjectionPoint(val id: Int,
                          val fieldName: String? = null,
                          val methodName: String? = null,
                          val methodType: String? = null)