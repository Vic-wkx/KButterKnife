package com.example.butterknife

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.library.kbutterknife.KButterKnife
import com.library.kbutterknife.OnClick

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        KButterKnife.inject(this)
    }

    @OnClick([R.id.btnToast1, R.id.btnToast2])
    fun toast(button: Button) {
        when (button.id) {
            R.id.btnToast1 -> Toast.makeText(this, "Toast 1", Toast.LENGTH_SHORT).show()
            R.id.btnToast2 -> Toast.makeText(this, "Toast 2", Toast.LENGTH_SHORT).show()
        }
    }
}