package com.adair.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.adair.app.widget.RandomTextView

class MainActivity : AppCompatActivity() {

    private lateinit var mTextView: RandomTextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTextView = findViewById(R.id.rtv_text)
        mTextView.setSpeeds(2)
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            mTextView.start()
        }
    }
}