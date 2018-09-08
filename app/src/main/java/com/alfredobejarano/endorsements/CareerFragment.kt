package com.alfredobejarano.endorsements


import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alfredobejarano.endorsements.databinding.FragmentCareerBinding
import com.alfredobejarano.endorsements.model.PlayerProfile
import com.alfredobejarano.endorsements.repository.remote.Platforms
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel
import com.alfredobejarano.endorsements.viewmodel.StorageViewModel
import kotlinx.android.synthetic.main.fragment_career.*

/**
 * A simple [Fragment] subclass that displays detailed data about a player endorsement.
 */
class CareerFragment : Fragment() {
    private var mPlayerProfile: PlayerProfile? = null
    private lateinit var mBinding: FragmentCareerBinding
    private lateinit var mStorageViewModel: StorageViewModel
    private lateinit var mProfileViewModel: ProfileViewModel

    /**
     * Creates this fragment and binds a ViewModel to its layout.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = setupDataBinding(inflater)

    /**
     * Initializes the DataBinding for this fragment.
     */
    private fun setupDataBinding(inflater: LayoutInflater): View {
        // Inflate the layout for this fragment
        mBinding = FragmentCareerBinding.inflate(inflater)
        // Initialize the ViewModels for this class.
        setupViewModels()
        // Retrieve the stored profile.
        mStorageViewModel.getStoredProfile()
        // Set this binding lifecycle owner
        mBinding.setLifecycleOwner(this)
        // Assign the ViewModel to the binding.
        mBinding.viewModel = mProfileViewModel
        // Assign the PlayerProfile object to the Data Binding.
        mBinding.playerProfile = mPlayerProfile
        // Return the binding root view.
        return mBinding.root
    }

    /**
     * Retrieves the ProfileView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Refresh the ProfileData when the refresh listener gets pulled.
        refresh?.setOnRefreshListener {
            mProfileViewModel.getProfileData(mPlayerProfile?.platform ?: Platforms.PC,
                    mPlayerProfile?.userName ?: "", false)
        }
        // Render the player icon.
        mProfileViewModel.playerIcon.observe(this, Observer {
            player_icon?.setImageURI(it)
        })
    }

    /**
     * Retrieve the necessary ViewModels from the activity.
     */
    private fun setupViewModels() {
        // Get the view model from the activity.
        mProfileViewModel = ViewModelProviders.of(activity as FragmentActivity)[ProfileViewModel::class.java]
        // Remove the observers from the activity.
        mProfileViewModel.status.removeObservers(activity as LifecycleOwner)
        // Sync the refresh listener with the ViewModel status.
        mProfileViewModel.loading.observe(this, Observer {
            refresh?.isRefreshing = it == View.VISIBLE
        })
        // Observe the progress of the ProgressBar widgets.
        mProfileViewModel.shotCaller.observe(this, Observer {
            shotcaller?.progress = it ?: 0
        })
        mProfileViewModel.goodTeammate.observe(this, Observer {
            goodteammate?.progress = it ?: 0
        })
        mProfileViewModel.sportsmanship.observe(this, Observer {
            sportsmanship?.progress = it ?: 0
        })

        // Retrieve the storage ViewModel.
        mStorageViewModel = ViewModelProviders.of(activity as FragmentActivity)[StorageViewModel::class.java]
        // Remove the observers from the activity.
        mStorageViewModel.session.removeObservers(activity as LifecycleOwner)
        // Observe changes in the stored session.
        mStorageViewModel.session.observe(this, Observer {
            mPlayerProfile = it
            profile_name?.text = it?.userName
        })
    }
}
