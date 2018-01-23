package io.smallant.sunorrain.data.services

import io.reactivex.Single
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Jonathan on 21/05/2016.
 */

class WeatherService {

    var api: API
    private val ENDPOINT: String = "http://api.openweathermap.org/"

    init {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder();
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url: HttpUrl = originalHttpUrl.newBuilder()
                    .addQueryParameter("APPID", "5c84a74006d1628b21f6f622178c3a89")
                    .build()

            val requestBuilder = original.newBuilder()
                    .url(url)
                    .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .baseUrl(ENDPOINT)
                .build()

        api = retrofit.create(API::class.java)
    }

    fun getCurrentWeather(city: String, units: String) = api.getCurrentWeather(city, units)
    fun getCurrentWeather(lat: Double, lon: Double, units: String) = api.getCurrentWeather(lat, lon, units)
    fun getWeatherWeek(city: String, units: String) = api.getWeatherWeek(city, units)
    fun getWeatherWeek(lat: Double, lon: Double, units: String) = api.getWeatherWeek(lat, lon, units)

    interface API {
        @GET("data/2.5/weather?mode=json")
        fun getCurrentWeather(@Query("q") query: String, @Query("units") units: String): Single<Weather>

        @GET("data/2.5/weather?mode=json")
        fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("units") units: String): Single<Weather>

        @GET("data/2.5/forecast/daily?mode=json&cnt=7")
        fun getWeatherWeek(@Query("q") query: String, @Query("units") units: String): Single<Forecast>

        @GET("data/2.5/forecast/daily?mode=json&cnt=7")
        fun getWeatherWeek(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("units") units: String): Single<Forecast>
    }
}