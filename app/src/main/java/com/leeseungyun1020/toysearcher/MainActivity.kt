package com.leeseungyun1020.toysearcher

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            setSelection(0)
        }
        binding.startButton.setOnClickListener {
            start(type)
        }
        setContentView(binding.root)
    }
}