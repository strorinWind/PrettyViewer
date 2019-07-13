package ru.strorin.xlsviewer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.strorin.xlsviewer.network.FileLoader


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName.orEmpty()

    private lateinit var textView: TextView
    private val fileName = "FutureStudioIcon.xls"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
    }

    override fun onResume() {
        super.onResume()

        someFun()
    }

    private fun someFun() {
        val call = FileLoader.getInstance().file().downloadFileWithFixedUrl()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "server contacted and has file")

                    val parser = FileParser(this@MainActivity)
                    val writtenToDisk = parser.writeResponseBodyToDisk(response.body()!!, fileName)

                    Log.d(TAG, "file download was a success? $writtenToDisk")
                    textView.append(parser.readExcelFile(fileName))
                } else {
                    Log.d(TAG, "server contact failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "error")
            }
        })
    }

}
