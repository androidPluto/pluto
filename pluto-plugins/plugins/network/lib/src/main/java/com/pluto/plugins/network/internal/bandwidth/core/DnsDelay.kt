package com.pluto.plugins.network.internal.bandwidth.core

import java.net.InetAddress
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Dns

/**
 * Custom okhttp dns that adds network delay while finding the dns, by blocking the thread
 * */
class DnsDelay(var timeoutMilliSeconds: Long) : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return runBlocking {
            delay(timeoutMilliSeconds)
            Dns.SYSTEM.lookup(hostname)
        }
    }
}
