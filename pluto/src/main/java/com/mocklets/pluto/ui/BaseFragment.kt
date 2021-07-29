package com.mocklets.pluto.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.google.android.material.tabs.TabLayoutMediator
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.delayedLaunchWhenResumed
import com.mocklets.pluto.core.extensions.hideKeyboard
import com.mocklets.pluto.core.ui.routing.RouterAction
import com.mocklets.pluto.core.ui.routing.Screens
import com.mocklets.pluto.core.ui.routing.lazyRouter
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.viewBinding
import com.mocklets.pluto.databinding.PlutoFragmentBaseBinding

@Keep
internal class BaseFragment : Fragment(R.layout.pluto___fragment_base) {

    private val binding by viewBinding(PlutoFragmentBaseBinding::bind)
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPager()
        binding.close.setDebounceClickListener { router.perform(RouterAction.BackToApp("top_back")) }
        binding.settings.setDebounceClickListener(haptic = true) { router.navigate(Screens.Settings) }
        binding.appState.setDebounceClickListener(haptic = true) { router.navigate(Screens.AppState) }
    }

    private fun setupPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.setCurrentItem(Pluto.session.selectTabIndex, false)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
                lifecycleScope.delayedLaunchWhenResumed(SMOOTH_TRANSITION_DELAY) { activity!!.hideKeyboard() }
                Pluto.session.selectTabIndex = position
            }

            override fun onPageSelected(position: Int) {
            }
        })

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = context?.getString(TAB_TITLES[position])
        }.attach()
        binding.tabs.tabMode = MODE_SCROLLABLE
        binding.tabs.isInlineLabel = false
    }

    private companion object {
        const val SMOOTH_TRANSITION_DELAY = 200L
    }
}
