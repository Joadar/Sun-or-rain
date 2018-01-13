package io.smallant.sunorrain.data.services

import io.reactivex.Observable
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

    fun getWeatherWeek(city: String) = api.getWeatherWeek(city)
    fun getCurrentWeather(city: String) = api.getCurrentWeather(city)
    fun getWeatherDayLatLon(lat: Double, lon: Double) = api.getWeatherDayLatLon(lat, lon)
    fun getWeatherWeekLatLon(lat: Double, lon: Double) = api.getWeatherWeekLatLon(lat, lon)

    interface API {
        @GET("data/2.5/forecast/daily?mode=json&units=metric&cnt=7")
        fun getWeatherWeek(@Query("q") query: String): Observable<Weather>

        @GET("data/2.5/weather?mode=json&units=metric")
        fun getCurrentWeather(@Query("q") query: String): Observable<Weather>

        @GET("data/2.5/weather?mode=json&units=metric")
        fun getWeatherDayLatLon(@Query("lat") lat: Double, @Query("lon") lon: Double): Observable<Weather>

        @GET("data/2.5/forecast/daily?mode=json&units=metric&cnt=7")
        fun getWeatherWeekLatLon(@Query("lat") lat: Double, @Query("lon") lon: Double): Observable<Weather>
    }
}