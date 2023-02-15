package com.leeseungyun1020.toysearcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.leeseungyun1020.searcher.utilities.Type
import com.leeseungyun1020.searcher.utilities.start
import com.leeseungyun1020.toysearcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        var type = Type.values().first()
        binding.typeSpinner.apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                Type.values()
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    type = Type.values()[position]
                    when(type) {
                        Type.NAVER -> {
                            binding.idEditText.setText(BuildConfig.NAVER_CLIENT_ID)
                            binding.pwEditText.setText(BuildConfig.NAVER_CLIENT_PW)
                        }
                        Type.DAUM -> {
                            binding.idEditText.setText("")
                            binding.pwEditText.setText(BuildConfig.KAKAO_REST_API_KEY)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            setSelection(0)
        }
        binding.startButton.setOnClickListener {
            val id = binding.idEditText.text.toString()
            val pw = binding.pwEditText.text.toString()
            if (pw.isNotEmpty()) {
                start(type, id, pw)
                finish()
            } else {
                Toast.makeText(this, R.string.error_input, Toast.LENGTH_LONG).show()
            }
        }
        setContentView(binding.root)
    }
}