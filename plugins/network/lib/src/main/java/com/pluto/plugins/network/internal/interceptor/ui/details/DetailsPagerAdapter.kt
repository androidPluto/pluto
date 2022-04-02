package com.pluto.plugins.network.internal.interceptor.ui.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pluto.plugins.network.R

internal val TAB_TITLES = arrayOf(
    R.string.pluto_network___tab_overview,
    R.string.pluto_network___tab_request,
    R.string.pluto_network___tab_response
)

internal class DetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (TAB_TITLES[position]) {
            R.string.pluto_network___tab_overview -> OverviewFragment()
            R.string.pluto_network___tab_request -> RequestFragment()
            R.string.pluto_network___tab_response -> ResponseFragment()
            else -> RequestFragment()
        }
    }
}
