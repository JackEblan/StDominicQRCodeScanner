package st.dominic.qrcodescanner.core.model

sealed interface GetProfileResult {
    data class Success(val authCurrentUser: AuthCurrentUser, val isAdmin: Boolean) :
        GetProfileResult

    data object Failed : GetProfileResult

    data object EmailVerify : GetProfileResult
}