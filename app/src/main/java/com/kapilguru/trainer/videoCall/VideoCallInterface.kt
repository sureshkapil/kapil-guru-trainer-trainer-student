package com.kapilguru.trainer.videoCall

import android.content.Context

interface VideoCallInterface {
    fun launchVideoCall(context: Context, roomName: String, participantName: String, hostName: String)
    fun closeVideoCall()
    fun onVideoCallClosed()
}