package com.kapilguru.trainer.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.kapilguru.trainer.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object RetrofitNetwork {

    //  private const val BASE_URL = "http://3.233.217.61:9000/"
//    private const val BASE_URL = "http://35.174.62.203:9000/" //Main
//    private const val BASE_URL = "http://44.197.234.100:9000/" //ritwik
//    private const val BASE_URL = "https://kapilguru.com/kg-v1/" //Live

    private fun getRetrofit(): Retrofit {
        val gSon = getGson()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // add gson here later on to exclude expose annotations
            .client(getRetroClient().build())
            .build() //Doesn't require the adapter
    }

    private fun getGson() = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    private fun getRetroClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS) //120
            .readTimeout(1000, TimeUnit.SECONDS) //120
            .writeTimeout(1000, TimeUnit.SECONDS) //120
        httpClient.addInterceptor(logging)

        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                if (AppConnectivityCheck(MyApplication.context).getConnectivityStatus() == 4) {
                    throw IOException("No internet connection")
                } else {
                    return chain.proceed(chain.request())
                }
            }
        })

        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                // here you fetch the token from Shared Preferences
                // create method
                val token = fetchTokenFromSharedPreferences()
                val newRequest: Request = chain.request().newBuilder().header("Authorization", "$token").build()
                return chain.proceed(newRequest)
            }
        })
        return getUnsafeOkHttpClient(httpClient)
    }

    private fun getUnsafeOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {

                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }
            val trustManager = trustManagers[0] as X509TrustManager
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun fetchTokenFromSharedPreferences(): String {
        val mContext = MyApplication.context
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString(JWT_TOKEN, "a")
//        Log.v("Token retro", jwtToken.toString())
        return jwtToken.toString()
    }

    val API_KAPIL_TUTOR_SERVICE_SERVICE: ApiKapilTutorService =
        getRetrofit().create(ApiKapilTutorService::class.java)

    data class NetworkConnectionError(override var message: String, var code: Int? = NETWORK_CONNECTIVITY_EROR) : IOException(message)

}