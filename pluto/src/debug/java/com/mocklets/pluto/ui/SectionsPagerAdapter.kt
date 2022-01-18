package com.mocklets.pluto.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mocklets.pluto.R
import com.mocklets.pluto.modules.exceptions.ui.CrashesFragment
import com.mocklets.pluto.modules.logging.ui.LogsFragment
import com.mocklets.pluto.modules.network.ui.NetworkFragment
import com.mocklets.pluto.modules.preferences.ui.SharedPrefFragment

internal val TAB_TITLES = arrayOf(
    R.string.pluto___tab_network,
    R.string.pluto___tab_logger,
    R.string.pluto___tab_crash,
    R.string.pluto___tab_shared_pref
)

internal class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (TAB_TITLES[position]) {
            R.string.pluto___tab_logger -> LogsFragment()
            R.string.pluto___tab_network -> NetworkFragment()
            R.string.pluto___tab_crash -> CrashesFragment()
            R.string.pluto___tab_shared_pref -> SharedPrefFragment()
            else -> NetworkFragment()
        }
    }
}
