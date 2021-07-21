package com.example.butterknife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.library.kbutterknife.BindView
import com.library.kbutterknife.KButterKnife

/**
 * Created by Kevin 2021-07-21
 */
class MainActivity : AppCompatActivity() {
    @BindView(R.id.tv)
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KButterKnife.inject(this)
        tv.text = "Hello KButterKnife!"
    }
}