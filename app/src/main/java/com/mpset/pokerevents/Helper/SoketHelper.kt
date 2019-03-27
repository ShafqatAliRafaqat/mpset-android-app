package com.mpset.pokerevents.Helper

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

class SoketHelper{
    companion object {
         var socket: Socket? = null
        init {
            try {
                socket = IO.socket("http://192.168.10.50:6800")
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }

        }

    }
}