package com.alfredobejarano.endorsements


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.alfredobejarano.endorsements.model.PlayerProfile
import com.alfredobejarano.endorsements.repository.remote.Platforms
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel
import com.alfredobejarano.endorsements.viewmodel.StorageViewModel
import kotlinx.android.synthetic.main.fragment_battletag.*

/**
 * A simple [Fragment] subclass that prompts the user to input his battletag and platform.
 *
 */
class BattletagFragment : Fragment(), TextView.OnEditorActionListener {
    private lateinit var mProfileViewModel: ProfileViewModel
    private lateinit var mStorageViewModel: StorageViewModel
    private var mPlayerProfile = PlayerProfile(0, "", Platforms.PC)

    /**
     * Retrieves the ViewModels from this fragment activity and observes necessary changes to them.
     */
    private fun setupViewModels() {
        // Retrieve the ProfileViewModel from the activity.
        mStorageViewModel = ViewModelProviders.of(activity as FragmentActivity)[StorageViewModel::class.java]
        mProfileViewModel = ViewModelProviders.of(activity as FragmentActivity)[ProfileViewModel::class.java]
        // Observe to the status of the profile request from t he Storage ViewModel.
        mProfileViewModel.status.observe(this, Observer<ProfileViewModel.Status> {
            if (it == ProfileViewModel.Status.STATUS_OK_AND_STORE) {
                mStorageViewModel.storePlayerProfile(mPlayerProfile)
            }
        })
    }

    /**
     * Create this fragment content view.
     * @param inflater The defined layout inflater for this fragment.
     * @param container The layout that is containing this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setupViewModels()
        // Inflate the view contents for this fragment.
        return inflater.inflate(R.layout.fragment_battletag, container, false)
    }

    /**
     * After inflating the view and displaying it into the fragment, initialize widget listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Assign a platform changed listener to the PlatformSelector.
        platform_selector?.listener = object : PlatformsSelector.OnPlatformSelectedListener {
            override fun onPlatformSelected(platform: Platforms) {
                // Change the hint text depending on the selected platform.
                battletag?.setHint(when (platform) {
                    Platforms.PC -> R.string.batttletag
                    Platforms.XBL -> R.string.gamertag
                    Platforms.PSN -> R.string.psnId
                })
            }
        }
        // Set a listener on the IME options button.
        battletag?.setOnEditorActionListener(this)
    }

    override fun onEditorAction(field: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Retrieve the selected platform from the selector.
            val selectedPlatform = platform_selector?.selectedPlatform
                    ?: Platforms.PC
            // Retrieve the input from the user and use it as the Battletag.
            val userName = (field as EditText?)?.text?.toString()
                    ?: ""
            // Define a PLayerProfile object from the input.
            mPlayerProfile = PlayerProfile(0, userName, selectedPlatform)
            // Search for the profile when the IME key gets clicked.
            mProfileViewModel.getProfileData(selectedPlatform, userName, true)
            // Report that an action has been consumed.
            true
        } else {
            // Report that no action has been reported.
            false
        }
    }
}
