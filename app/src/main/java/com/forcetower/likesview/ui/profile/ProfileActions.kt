package com.forcetower.likesview.ui.profile

import com.forcetower.likesview.core.model.helpers.BasicProfile
import com.forcetower.likesview.core.model.values.InstagramMedia

interface ProfileActions {
    fun onReelClicked(profile: BasicProfile?)
    fun onMediaClicked(media: InstagramMedia?)
}