package prj.edu.bytta.meldinger

data class InputField(
    var input: String = "",
    var isError: Boolean = false,
    var errorMelding: String = "",
)
