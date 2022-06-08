package com.kapilguru.trainer.network

class MyServiceHolder {
    var myService: ApiKapilTutorService? = null

    fun get(): ApiKapilTutorService? {
        return myService
    }

    fun set(myService: ApiKapilTutorService?) {
        this.myService = myService
    }
}