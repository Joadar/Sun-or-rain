package io.smallant.sunorrain.ui.nextDays

import android.os.Bundle
import android.view.View
import io.smallant.sunorrain.R
import io.smallant.sunorrain.adapters.DaysAdapter
import io.smallant.sunorrain.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_next_days.*

/**
 * Created by jpannetier on 10/01/2018.
 */
class NextDaysFragment: BaseFragment() {

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
        fun create(lat: Float, lon: Float): NextDaysFragment {
            val args = Bundle(2)
            args.putFloat(ARGS_LAT, lat)
            args.putFloat(ARGS_LON, lon)
            val fragment = NextDaysFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var layoutId: Int = R.layout.fragment_next_days

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        recycler_next_days.apply {
            setHasFixedSize(true)
            adapter = DaysAdapter(arrayListOf("9°C", "1°C", "16°C", "15°C", "21°C"))
        }
    }
}