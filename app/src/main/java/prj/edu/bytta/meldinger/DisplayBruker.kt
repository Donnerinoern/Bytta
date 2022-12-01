package prj.edu.bytta.meldinger

import android.graphics.Bitmap

data class DisplayBruker(

    val uid: String,

    val displayNavn: String,


) {
    val displayBruker = mutableListOf<DisplayBruker>()


}
