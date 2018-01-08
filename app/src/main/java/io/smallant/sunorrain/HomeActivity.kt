package io.smallant.sunorrain

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.smallant.sunorrain.ui.MapsActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        text_hello_world.setOnClickListener { startActivity(Intent(this, MapsActivity::class.java)) }
    }
}
