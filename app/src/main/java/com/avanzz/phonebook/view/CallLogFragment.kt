package com.avanzz.phonebook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.avanzz.phonebook.R
import com.google.android.material.tabs.TabLayout

class CallLogFragment: Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(): CallLogFragment {
            return CallLogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_call_log, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        // Hide the TabLayout on scroll action in child fragments
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // No implementation needed
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    tabLayout.visibility = View.GONE
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    tabLayout.visibility = View.VISIBLE
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an adapter that returns a fragment for each of the three primary sections of the activity.
        val adapter = TabAdapter(childFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = adapter

        // Connect the TabLayout with the ViewPager.
        tabLayout.setupWithViewPager(viewPager)

        // Set the text for each tab.
        tabLayout.getTabAt(0)?.text = "Received"
        tabLayout.getTabAt(1)?.text = "Dialed"
        tabLayout.getTabAt(2)?.text = "Missed"


    }

    class TabAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ReceivedFragment()
                1 -> DialedFragment()
                2 -> MissedFragment()
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

}