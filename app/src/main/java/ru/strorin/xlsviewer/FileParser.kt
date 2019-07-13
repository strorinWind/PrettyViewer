package ru.strorin.xlsviewer

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import java.io.*

class FileParser(context: Context) {
    private val TAG = FileParser::class.simpleName.orEmpty()

    private val mContext : Context = context

    fun writeResponseBodyToDisk(body: ResponseBody, name: String): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(mContext.getExternalFilesDir(null), name)
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

    fun readExcelFile(name: String) : String {
        var resString : String = ""
        try {
            //  open excel sheet
            val file = File(mContext.getExternalFilesDir(null), name)
            // Create a workbook using the File System
            val myWorkBook = WorkbookFactory.create(file);

            // Get the first sheet from workbook
            val mySheet = myWorkBook.getSheetAt(0)
            // We now need something to iterate through the cells.
            val rowIter = mySheet.rowIterator()
            var rowno = 0
            resString += "\n"
            var currentNum = 1.0

            while (rowIter.hasNext()) {
                Log.e(TAG, " row no $rowno")
                val myRow = rowIter.next() as XSSFRow

                val xssfCell = myRow.first() as XSSFCell

                if (xssfCell.cellType == 0 && xssfCell.numericCellValue == currentNum) {
                    val cellIter = myRow.cellIterator()
                    var colno = 0
                    var sno = ""
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as XSSFCell
                        sno += "$myCell "
                        colno++
                        Log.d(TAG, " Index :" + myCell.columnIndex + " -- " + myCell.toString())
                    }
                    resString += "$sno\n"
                    currentNum++
                }
                rowno++
            }
        } catch (e: Exception) {
            Log.d(TAG, "error $e")
        }
        return resString

    }
}