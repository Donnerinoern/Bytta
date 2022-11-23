package prj.edu.bytta


sealed class Screen(val route: String) {
    object MainActivity: Screen("mainactivity")
    object LoginScreen: Screen("loginscreen")
    object HomeActivity: Screen("homeactivity")
}
