package ru.strorin.xlsviewer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.strorin.xlsviewer.network.FileLoader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName.orEmpty()

    private lateinit var rvStudents: RecyclerView
    private lateinit var adapter: StudentsRecyclerAdapter
    private val fileName = "FutureStudioIcon.xls"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        setupRecyclerViews()
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
                    val list = parser.readExcelFile(fileName)
                    adapter.setDataset(list)
                } else {
                    Log.d(TAG, "server contact failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "error")
            }
        })
    }

    private fun setupRecyclerViews() {
        adapter = StudentsRecyclerAdapter()

        setRecyclerViewDecoration(rvStudents)
        rvStudents.adapter = adapter
    }

    private fun setRecyclerViewDecoration(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    private fun findViews() {
        rvStudents = findViewById(R.id.rv_students)
    }
}
