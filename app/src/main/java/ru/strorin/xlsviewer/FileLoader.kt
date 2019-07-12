package ru.strorin.xlsviewer

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.strorin.xlsviewer.network.FileEndpoint
import java.util.concurrent.TimeUnit

const val URL = "https://priem3.hse.ru/"
const val TIMEOUT_IN_SECONDS = 5L

class FileLoader {

    private val fileEndpoint: FileEndpoint

    init {
        val httpClient = buildOkHttpClient()
        val retrofit = buildRetrofitClient(httpClient)

        //init endpoints here. It's can be more then one endpoint
        fileEndpoint = retrofit.create<FileEndpoint>(FileEndpoint::class.java)
    }

    companion object {
        private val sInstance: FileLoader = FileLoader()

        fun getInstance() : FileLoader {
            return sInstance
        }
    }

    private fun buildRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val networkLogInterceptor = HttpLoggingInterceptor()
        networkLogInterceptor.level = HttpLoggingInterceptor.Level.BASIC


        return OkHttpClient.Builder()
            .addInterceptor(networkLogInterceptor)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    fun file(): FileEndpoint {
        return fileEndpoint
    }
}