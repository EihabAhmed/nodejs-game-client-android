package com.bbk.gamenodejs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bbk.gamenodejs.databinding.ActivityUserNameBinding

class UserNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etUsername.doAfterTextChanged {
            val username = it.toString()
            binding.btnProceed.isEnabled = username.isNotEmpty()
        }
        binding.btnProceed.setOnClickListener {
            val username = binding.etUsername.text.toString()
            if (username.isNotEmpty()) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(GameActivity.USERNAME, username)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etUsername.requestFocus()
    }
}