package com.mocklets.pluto.network.ui.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mocklets.pluto.network.R

internal val TAB_TITLES = arrayOf(
    R.string.pluto_network___tab_overview,
    R.string.pluto_network___tab_request,
    R.string.pluto_network___tab_response
)

internal class NetworkDetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (TAB_TITLES[position]) {
            R.string.pluto_network___tab_overview -> NetworkDetailsOverviewFragment()
            R.string.pluto_network___tab_request -> NetworkDetailsRequestFragment()
            R.string.pluto_network___tab_response -> NetworkDetailsResponseFragment()
            else -> NetworkDetailsRequestFragment()
        }
    }
}
