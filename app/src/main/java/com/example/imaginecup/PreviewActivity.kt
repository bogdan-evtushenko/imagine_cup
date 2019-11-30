package com.example.imaginecup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_preview.*
import android.view.animation.AnimationUtils
import kotlinx.coroutines.* 

class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        // Log.d("myTag", "Hello world")

        butOK.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.move)
            rocket.startAnimation(animation)

            CoroutineScope(context = Dispatchers.IO).launch {
                delay(500)
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@PreviewActivity, MainActivity::class.java))
                }
            }
        }
    }
}
