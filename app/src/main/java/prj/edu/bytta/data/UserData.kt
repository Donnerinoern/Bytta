package prj.edu.bytta.data

data class UserData(
    var userID: String? = null,
    var username: String? = null,
    var imageUrl: String? = null
) {
    fun toMap() = mapOf(
        "userId" to userID,
        "username" to username,
        "imageUrl" to imageUrl
    )
}
