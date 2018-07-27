package com.alfredobejarano.endorsements


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alfredobejarano.endorsements.databinding.FragmentCareerBinding
import com.alfredobejarano.endorsements.repository.local.AppDatabase
import com.alfredobejarano.endorsements.repository.remote.Platforms
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_career.*
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass that displays detailed data about a player endorsement.
 */
class CareerFragment : Fragment() {
    private var mViewModel: ProfileViewModel? = null

    /**
     * Creates this fragment and binds a ViewModel to its layout.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentCareerBinding.inflate(inflater)
        // Define the ViewModel for this fragment data binding.
        mViewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        binding.viewModel = mViewModel
        // Set this binding lifecycle owner
        binding.setLifecycleOwner(this)// Setup swipe to refresh.
        refresh?.setOnRefreshListener {
            thread {
                val session = AppDatabase.getInstance(context!!)
                        .playerProfileDao()
                        .getPlayerProfile()
                mViewModel?.getProfileData(session?.platform ?: Platforms.PC
                        , session?.userName)
            }.start()
        }
        mViewModel?.loading?.observe(this, Observer {
            refresh?.isRefreshing = it == View.VISIBLE

        })
        // Render the player icon.
        mViewModel?.playerIcon?.observe(this, Observer {
            player_icon?.setImageURI(it)
        })
        // Render the good teammate endorsement percentage.
        mViewModel?.goodTeammate?.observe(this, Observer {
            goodteammate?.progress = it?.toInt() ?: 0 * 100
        })
        // Render the sportsmanship endorsement percentage.
        mViewModel?.sportsmanship?.observe(this, Observer {
            sportsmanship?.progress = it?.toInt() ?: 0 * 100
        })
        // Render the shot caller endorsement percentage.
        mViewModel?.shotCaller?.observe(this, Observer {
            shotcaller?.progress = it?.toInt() ?: 0 * 100
        })
        // Return the binding root view.
        return binding.root
    }
}
