package com.example.butterknife

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.library.kbutterknife.BindView
import com.library.kbutterknife.KButterKnife
import com.library.kbutterknife.OnClick

/**
 * Created by Kevin 2021-07-21
 */
class MainActivity : AppCompatActivity() {
    @BindView(R.id.btnBindView)
    lateinit var btnBindView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KButterKnife.inject(this)
        btnBindView.text = "Hello KButterKnife!"
    }

    @OnClick([R.id.btnStart])
    fun startSecondActivity() {
        startActivity(Intent(this, SecondActivity::class.java))
    }
}