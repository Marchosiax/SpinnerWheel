package com.marchosiax.spinnerwheel

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.marchosiax.spinner.SpinnerWheel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerWheel.addPies(
            SpinnerWheel.Pie("0", "1000 امتیاز", Color.parseColor("#546E7A")),
            SpinnerWheel.Pie("1", "2000 امتیاز", Color.parseColor("#6D4C41")),
            SpinnerWheel.Pie("2", "5000 امتیاز", Color.parseColor("#FB8C00")),
            SpinnerWheel.Pie("3", "1000 امتیاز", Color.parseColor("#C0CA33")),
            SpinnerWheel.Pie("4", "10000 امتیاز", Color.parseColor("#43A047")),
            SpinnerWheel.Pie("5", "100000 امتیاز", Color.parseColor("#00897B")),
            SpinnerWheel.Pie("6", "20000 امتیاز", Color.parseColor("#039BE5")),
            SpinnerWheel.Pie("7", "10000 امتیاز", Color.parseColor("#3949AB")),
            SpinnerWheel.Pie("8", "50000 امتیاز", Color.parseColor("#8E24AA")),
            SpinnerWheel.Pie("9", "100000 امتیاز", Color.parseColor("#5E35B1")),
            SpinnerWheel.Pie("10", "5000 امتیاز", Color.parseColor("#D81B60")),
            SpinnerWheel.Pie("11", "100000 امتیاز", Color.parseColor("#e53935"))
        )

        spinnerWheel.wheel.setTextProperties(ResourcesCompat.getFont(this, R.font.iransansmobile))

        btnSpin.setOnClickListener {
            spinnerWheel.spin("2")
        }
    }
}