package com.idiotleon.tutorialreactiveprogrammingwrxandroidnkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        initLayout()
    }

    private fun initLayout() {
        // sugar()
        noSugar()
    }

    private fun sugar() {
        RxView.clicks(btn_click)
            .map {
                incrementCounter1()
            }
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe {
                incrementCounter2()
            }
    }

    private fun noSugar() {
        val emitter = PublishSubject.create<View>()

        btn_click.setOnClickListener {
            emitter.onNext(it)
        }

        val observer = object : Observer<View> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                incrementCounter2()
            }

            override fun onNext(t: View) {
            }

            override fun onError(e: Throwable) {
            }
        }

        val consumer = object : Consumer<View> {
            override fun accept(t: View?) {
                incrementCounter2()
            }
        }

        emitter
            //.map { incrementCounter1() }
            .map(object : Function<View, View> {
                override fun apply(t: View): View {
                    incrementCounter1()
                    return t
                }
            })
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            // .subscribe {incrementCounter2()}
            //.subscribe(observer)
            .subscribe(consumer)
    }


    private fun incrementCounter1() {
        var newVal = tv_counter1.text.toString().toInt()
        newVal++
        tv_counter1.text = newVal.toString()
    }

    private fun incrementCounter2() {
        var newVal = tv_counter2.text.toString().toInt()
        newVal++
        tv_counter2.text = newVal.toString()
    }
}
