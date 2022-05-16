package org.remdev.flog

/**
 *  Created by Alexandr Salin on 16.05.22
 */
interface Flog {
    fun log(priority: Int, tag: String?, message: String?, t: Throwable?)
}