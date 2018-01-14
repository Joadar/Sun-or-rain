package io.smallant.sunorrain.ui.features.nextDays

import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication.Companion.repository
import io.smallant.sunorrain.adapters.DaysAdapter
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.ForecastDetail
import io.smallant.sunorrain.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_next_days.*

/**
 * Created by jpannetier on 10/01/2018.
 */
class NextDaysFragment :
        BaseFragment(),
        NextDaysContract.View {

    companion object {

        const val ARGS_CITY_NAME = "ARGS_CITY_NAME"
        const val ARGS_CITY_ID = "ARGS_CITY_ID"
        const val ARGS_LAT = "ARGS_LAT"
        const val ARGS_LON = "ARGS_LON"

        @JvmStatic
        fun create(city: String): NextDaysFragment {
            val args = Bundle(1)
            args.putString(ARGS_CITY_NAME, city)
            val fragment = NextDaysFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun create(cityId: Long): NextDaysFragment {
            val args = Bundle(1)
            args.putLong(ARGS_CITY_ID, cityId)
            val fragment = NextDaysFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun create(lat: Double, lon: Double): NextDaysFragment {
            val args = Bundle(2)
            args.putDouble(ARGS_LAT, lat)
            args.putDouble(ARGS_LON, lon)
            val fragment = NextDaysFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val presenter: NextDaysPresenter by lazy { NextDaysPresenter(repository) }
    private val daysAdapter: DaysAdapter by lazy { DaysAdapter(arrayListOf(ForecastDetail()), context) }

    override var layoutId: Int = R.layout.fragment_next_days

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.view = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        arguments?.let {
            if (it.getString(ARGS_CITY_NAME, null) == null)
                presenter.getWeekWeather(it.getDouble(ARGS_LAT), it.getDouble(ARGS_LON))
            else
                presenter.getWeekWeather(it.getString(ARGS_CITY_NAME))
        }
    }

    override fun displayWeekWeather(data: Forecast) {
        daysAdapter.add(data.list)
    }

    override fun displaySearchError() {
        Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show()
    }

    private fun initRecycler() {
        recycler_next_days.apply {
            setHasFixedSize(true)
            adapter = daysAdapter
        }
    }
}