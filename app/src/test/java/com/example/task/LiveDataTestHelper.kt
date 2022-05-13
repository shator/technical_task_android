package com.example.task

import androidx.lifecycle.LiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataTestHelper {
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        liveData.observeForever { o ->
            data[0] = o
            latch.countDown()
        }
        latch.await(1, TimeUnit.SECONDS)
        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}