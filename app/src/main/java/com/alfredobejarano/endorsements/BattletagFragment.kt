package com.alfredobejarano.endorsements


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.alfredobejarano.endorsements.repository.remote.Platforms
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_career.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BattletagFragment : Fragment() {
    /**
     * Create this fragment content view.
     * @param inflater The defined layout inflater for this fragment.
     * @param container The layout that is containing this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_career, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val careerVM = ViewModelProviders.of(activity as FragmentActivity)[ProfileViewModel::class.java]
        platform_selector?.listener = object : PlatformsSelector.OnPlatformSelectedListener {
            override fun onPlatformSelected(platform: Platforms) {
                battletag?.setHint(when (platform) {
                    Platforms.PC -> R.string.batttletag
                    Platforms.XBL -> R.string.gamertag
                    Platforms.PSN -> R.string.psnId
                })
            }
        }
        battletag?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                careerVM.getProfileData(platform_selector?.selectedPlatform
                        ?: Platforms.PC, (v as EditText?)?.text)
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }
}
