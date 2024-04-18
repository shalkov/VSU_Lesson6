package ru.shalkoff.vsu_lesson6

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.shalkoff.vsu_lesson6.connection.Example1HttpURLConnection
import ru.shalkoff.vsu_lesson6.connection.Example2HttpURLConnection
import ru.shalkoff.vsu_lesson6.connection.Example3HttpURLConnection
import ru.shalkoff.vsu_lesson6.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val example1HttpURLConnection = Example1HttpURLConnection()
    private val example2HttpURLConnection = Example2HttpURLConnection()
    private val example3HttpURLConnection = Example3HttpURLConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initListeners()
    }

    private fun initListeners() {
        binding.example1Btn.setOnClickListener {
            example1HttpURLConnection.sendRequest(this)
        }
        binding.example2Btn.setOnClickListener {
            example2HttpURLConnection.sendRequest(this)
        }
        binding.example3Btn.setOnClickListener {
            example3HttpURLConnection.sendRequest(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        example3HttpURLConnection.clearDisposables()
    }
}