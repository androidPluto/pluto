package com.pluto.plugins.network.internal.bandwidth.core

import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory

class BandwidthLimitSocketFactory : SocketFactory() {

    override fun createSocket(): Socket {
        return DelaySocket()
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return DelaySocket(host, port)
    }

    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket {
        return DelaySocket(host, port, localHost, localPort)
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return DelaySocket(host, port)
    }

    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket {
        return DelaySocket(address, port, localAddress, localPort)
    }

    class DelaySocket : Socket {
        constructor() : super()
        constructor(host: String?, port: Int) : super(host, port)
        constructor(address: InetAddress?, port: Int) : super(address, port)
        constructor(host: String?, port: Int, localAddr: InetAddress?, localPort: Int) : super(
            host,
            port,
            localAddr,
            localPort
        )

        constructor(
            address: InetAddress?,
            port: Int,
            localAddr: InetAddress?,
            localPort: Int
        ) : super(address, port, localAddr, localPort)

        override fun getInputStream(): InputStream {
            return ThrottledInputStream(super.getInputStream())
        }

        override fun getOutputStream(): OutputStream {
            return ThrottledOutputStream(super.getOutputStream())
        }
    }
}
