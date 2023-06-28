/**
 * Copyright (C) 2020 SINTEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.fiskinfo.ui.snap

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.tabs.TabLayout

import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.SnapFragmentBinding

/**
 * The SnapFragment is the main container fragment for the echo snap message functionality.
 * It contains tabs for showing the users own echo snaps, and for the inbox and outbox for
 * snap messages.
 */
class SnapFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var snapPagerAdapter: SnapPageAdapter
    private var _mBinding: SnapFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = SnapFragmentBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        snapPagerAdapter = SnapPageAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.snappager)
        viewPager.adapter = snapPagerAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.snaptab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

/*    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
*/
    inner class SnapPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getPageTitle(position: Int): CharSequence? {
            return resources.getStringArray(R.array.snap_tab_titles)[position]
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    SnapBoxFragment.newInstance(true)

                }
                1 -> {
                    SnapBoxFragment.newInstance(false)
                }
                else -> {
                    EchogramListFragment.newInstance()
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    companion object {

        fun newInstance(): SnapFragment {
            return SnapFragment()
        }
    }

}
