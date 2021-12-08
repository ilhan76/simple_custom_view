package com.kudashov.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customView = findViewById<SimpleCustomView>(R.id.custom_view)

        findViewById<Button>(R.id.btn_hex).setOnClickListener {
            customView.setFiguresColors(
                listOf(
                    "#e22b7a",
                    "#2babe2",
                    "#a9cc1f"
                )
            )
        }
        findViewById<Button>(R.id.btn_int).setOnClickListener {
            customView.setFiguresColors(
                listOf(
                    Color.YELLOW,
                    Color.RED,
                    Color.CYAN
                )
            )
        }
        findViewById<Button>(R.id.btn_empty).setOnClickListener {
            customView.setFiguresColors(listOf<String>())
        }
    }
}