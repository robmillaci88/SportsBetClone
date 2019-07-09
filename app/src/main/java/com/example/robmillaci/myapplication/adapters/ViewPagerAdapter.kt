package com.example.robmillaci.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.extension_functions.inflate
import kotlinx.android.synthetic.main.view_pager_layout.view.*
import java.lang.ref.WeakReference

@Suppress("DEPRECATION")
/**
 * Adapter for the Viewpager displaying the sports and racing images
 */
class ViewPagerAdapter(val images: Array<Int>, val context: WeakReference<Context>) : androidx.viewpager.widget.PagerAdapter() {

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = R.layout.view_pager_layout.inflate(context.get(),null)
        view.viewPagerImage.setImageDrawable(context.get()?.resources?.getDrawable(images[position]))
        container.addView(view)
        return view
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
      return images.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }

}