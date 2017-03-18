package com.kobayashi.rxprojectkt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by ryo on 2017/03/19.
 *
 * 起動画面
 */
class BootActivity : AppCompatActivity() {

    private var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot)
    }

    override fun onResume() {
        super.onResume()

        disposable = Completable.timer(2, TimeUnit.SECONDS, Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Boot", "Thread : " + Thread.currentThread().getName())
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            })
    }

    override fun onPause() {
        if(!disposable!!.isDisposed) {
            disposable!!.dispose()
        }
        super.onPause()
    }
}