package com.kapilguru.trainer.videoCall

import android.content.Context
import org.jitsi.meet.sdk.EntryActivity

object VideoCallInterfaceImplementation : VideoCallInterface {

    override fun launchVideoCall(context: Context, roomName: String, participantName: String, hostName: String) {
        try {
            EntryActivity.launch(context, /*roomName*/"DEMO1642666018870DL15610", participantName, "kapilguru", "Kapil@123") //trainer
        } catch (exception: Exception) {

        }
    }

    override fun closeVideoCall() {
        TODO("Not yet implemented")
    }

    override fun onVideoCallClosed() {
        TODO("Not yet implemented")
    }

}