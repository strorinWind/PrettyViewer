package ru.strorin.xlsviewer

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem

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

                    val writtenToDisk = writeResponseBodyToDisk(response.body()!!)

                    Log.d(TAG, "file download was a success? $writtenToDisk")
                    readExcelFileFromAssets()
                } else {
                    Log.d(TAG, "server contact failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "error")
            }
        })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(getExternalFilesDir(null), fileName)
            Log.d(TAG, futureStudioIconFile.absolutePath)

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()

                return true
            } catch (e: IOException) {
                return false
            } finally {
                inputStream?.close()

                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }

    }

    fun readExcelFileFromAssets() {
        try {
            val myInput: InputStream
            // initialize asset manager
//            val assetManager = assets
            //  open excel sheet
            val file = File(getExternalFilesDir(null), fileName)
            myInput = contentResolver.openInputStream(Uri.fromFile(file))!!
//            myInput = assetManager.open("myexcelsheet.xls")
            // Create a POI File System object
            val myFileSystem = POIFSFileSystem(myInput)
            // Create a workbook using the File System
//            val wb = XSSFWorkbook(File("file.xlsx"))

            val myWorkBook = HSSFWorkbook(myFileSystem)
            // Get the first sheet from workbook
            val mySheet = myWorkBook.getSheetAt(0)
            // We now need something to iterate through the cells.
            val rowIter = mySheet.rowIterator()
            var rowno = 0
            textView.append("\n")
            while (rowIter.hasNext()) {
                Log.e(TAG, " row no $rowno")
                val myRow = rowIter.next() as HSSFRow
                if (rowno != 0) {
                    val cellIter = myRow.cellIterator()
                    var colno = 0
                    var sno = ""
                    var date = ""
                    var det = ""
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as HSSFCell
                        when (colno) {
                            0 -> sno = myCell.toString()
                            1 -> date = myCell.toString()
                            2 -> det = myCell.toString()
                        }
                        colno++
                        Log.e(TAG, " Index :" + myCell.columnIndex + " -- " + myCell.toString())
                    }
                    textView.append("$sno -- $date  -- $det\n")
                }
                rowno++
            }
        } catch (e: Exception) {
            Log.e(TAG, "error $e")
        }

    }
}
