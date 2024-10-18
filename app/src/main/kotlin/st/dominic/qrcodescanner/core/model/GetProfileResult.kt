package st.dominic.qrcodescanner.core.model

data class GetProfileResult(
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val isSignedIn: Boolean? = null,
    val isEmailVerified: Boolean? = null,
    val isAdmin: Boolean? = null,
)