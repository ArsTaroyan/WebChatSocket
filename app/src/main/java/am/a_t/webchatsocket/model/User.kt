package am.a_t.webchatsocket.model

data class User(
    var userName: String,
    var message: String,
    var client: String
) {
//    companion object {
//        fun emptyUser() : User = User("", "")
//    }
}