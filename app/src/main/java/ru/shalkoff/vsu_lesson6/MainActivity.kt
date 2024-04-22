package ru.shalkoff.vsu_lesson6

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.shalkoff.vsu_lesson6.connection.Example1HttpURLConnection
import ru.shalkoff.vsu_lesson6.connection.Example2HttpURLConnection
import ru.shalkoff.vsu_lesson6.connection.Example3HttpURLConnection
import ru.shalkoff.vsu_lesson6.databinding.ActivityMainBinding
import ru.shalkoff.vsu_lesson6.okhttp.Example1OkHttp
import ru.shalkoff.vsu_lesson6.okhttp.Example2OkHttp
import ru.shalkoff.vsu_lesson6.retrofit.ExampleRetrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val example1HttpURLConnection = Example1HttpURLConnection()
    private val example2HttpURLConnection = Example2HttpURLConnection()
    private val example3HttpURLConnection = Example3HttpURLConnection()

    private val example1OkHttp = Example1OkHttp()
    private val example2OkHttp = Example2OkHttp()

    private val exampleRetrofit = ExampleRetrofit()

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
        with(binding) {
            example1Btn.setOnClickListener {
                example1HttpURLConnection.sendRequest(this@MainActivity)
            }
            example2Btn.setOnClickListener {
                example2HttpURLConnection.sendRequest(this@MainActivity)
            }
            example3Btn.setOnClickListener {
                example3HttpURLConnection.sendRequest(this@MainActivity)
            }
            example4Btn.setOnClickListener {
                example1OkHttp.sendRequest(this@MainActivity)
            }
            example5Btn.setOnClickListener {
                example2OkHttp.sendRequest(
                    onSuccess = { responseObject ->
                        runOnUiThread {
                            Log.d("LESSON6", responseObject.toString())
                            Toast.makeText(
                                this@MainActivity,
                                "#5 Запрос выполнился (OkHttp enqueue)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, onError = {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "#5 Запрос выполнился с ошибкой (OkHttp enqueue)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
            example6Btn.setOnClickListener {
                exampleRetrofit.sendRequest1(this@MainActivity)
            }
            example7Btn.setOnClickListener {
                exampleRetrofit.sendRequest2(this@MainActivity)
            }
            example8Btn.setOnClickListener {
                exampleRetrofit.sendRequest3(this@MainActivity)
            }
            example9Btn.setOnClickListener {
                exampleRetrofit.sendRequest4(this@MainActivity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        example3HttpURLConnection.clearDisposables()
        exampleRetrofit.clearDisposables()
    }
}