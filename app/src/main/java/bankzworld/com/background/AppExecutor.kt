package bankzworld.com.background

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutor(val diskIO: Executor, val mainThread: Executor, val networkIO: Executor) {


    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            mainThreadHandler.post(runnable)
        }
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: AppExecutor? = null

        fun getsInstance(): AppExecutor {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = AppExecutor(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        MainThreadExecutor()
                    )
                }
            }
            return sInstance!!
        }
    }
}
