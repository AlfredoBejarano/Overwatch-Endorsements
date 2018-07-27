package com.alfredobejarano.endorsements

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import android.widget.LinearLayout
import com.alfredobejarano.endorsements.repository.remote.Platforms
import kotlinx.android.synthetic.main.view_platform_selector.view.*

class PlatformsSelector(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    var selectedPlatform = Platforms.PC
    var listener: OnPlatformSelectedListener? = null
    private val root: View = LayoutInflater.from(context).inflate(R.layout.view_platform_selector, this, true)
    private val selector: LinearLayout = root.selector

    init {
        for (i in 0 until selector.childCount) {
            selector.getChildAt(i).setOnClickListener {
                it.setOnClickListener { selectPlatforms(it.id) }
            }
        }
    }

    private fun selectPlatforms(viewId: Int) {
        for (i in 0 until selector.childCount) {
            val child = root.selector.getChildAt(i) as CheckedTextView
            if (child.id == viewId) {
                child.isChecked = true
                selectedPlatform = when (viewId) {
                    R.id.pc -> Platforms.PC
                    R.id.xbl -> Platforms.XBL
                    else -> Platforms.PSN
                }
                listener?.onPlatformSelected(selectedPlatform)
            } else {
                child.isChecked = false
            }
        }
    }

    interface OnPlatformSelectedListener {
        fun onPlatformSelected(platform: Platforms)
    }
}