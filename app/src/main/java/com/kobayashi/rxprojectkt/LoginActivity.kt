package com.kobayashi.rxprojectkt

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kobayashi.rxprojectkt.databinding.ActivityLoginBinding
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class LoginActivity : AppCompatActivity() {

    var _disposables : CompositeDisposable? = null
    var _binding : ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun onResume() {
        super.onResume()
        setupObservables()
    }

    override fun onPause() {
        releaseObservables()
        super.onPause()
    }

    fun setupObservables() {
        _disposables = CompositeDisposable()

        val login = RxView.clicks(_binding!!.login)
            .doOnNext {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .subscribe()

        val inputUserName = RxTextView.textChanges(_binding!!.userName)
            .map({ it.isNotEmpty() })
        val inputPassword = RxTextView.textChanges(_binding!!.password)
            .map({ it.isNotEmpty() })

        val validateLogin = Observable
            .combineLatest(inputUserName, inputPassword,
                BiFunction { v1:Boolean, v2:Boolean -> (v1 && v2) })
            .subscribe(RxView.enabled(_binding!!.login))

        _disposables!!.addAll(login, validateLogin)
    }

    fun releaseObservables() {
        if(!_disposables!!.isDisposed) {
            _disposables!!.dispose()
        }
    }
}
