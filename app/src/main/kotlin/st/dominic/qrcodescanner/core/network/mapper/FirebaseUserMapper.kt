package st.dominic.qrcodescanner.core.network.mapper

import com.google.firebase.auth.FirebaseUser
import st.dominic.qrcodescanner.core.model.AuthCurrentUser

fun toAuthCurrentUser(firebaseUser: FirebaseUser?): AuthCurrentUser? {
    return if (firebaseUser != null) {
        AuthCurrentUser(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName.toString(),
            email = firebaseUser.email.toString(),
            isEmailVerified = firebaseUser.isEmailVerified,
        )
    } else {
        null
    }
}