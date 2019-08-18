package com.forcetower.likesview.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.NavInflater
import com.forcetower.likesview.R
import com.forcetower.likesview.core.EventObserver
import com.forcetower.likesview.core.extensions.checkAllMatched
import com.forcetower.likesview.core.vm.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import java.util.ArrayDeque
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel: LaunchViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.launchDestination.observe(this, EventObserver { dest ->
            when (dest) {
                LaunchDestination.MAIN_ACTIVITY -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                LaunchDestination.ONBOARDING -> {
                    startActivity(Intent(this, OnboardingActivity::class.java))
                }
                LaunchDestination.ONBOARDING_NO_PROFILES -> {
                    val intent = Intent(this, OnboardingActivity::class.java)
                    val graph = NavInflater(this, PermissiveNavigatorProvider())
                        .inflate(R.navigation.onboarding_graph)

                    val node = graph.findNode(R.id.first_profile)
                    // TODO: Warning! This might break in some version!!!
                    intent.putExtra("android-support-nav:controller:deepLinkIds", node!!.buildExplicitly())
                    startActivity(intent)
                }
            }.checkAllMatched
            finish()
        })
    }


}

fun NavDestination.buildExplicitly(): IntArray {
    val hierarchy = ArrayDeque<NavDestination>()
    var current: NavDestination? = this
    do {
        val parent = current!!.parent
        if (parent == null || parent.startDestination != current.id) {
            hierarchy.addFirst(current)
        }
        current = parent
    } while (current != null)
    val deepLinkIds = IntArray(hierarchy.size)
    var index = 0
    for (destination in hierarchy) {
        deepLinkIds[index++] = destination.id
    }
    return deepLinkIds
}