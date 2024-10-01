package st.dominic.qrcodescanner.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberSignInState(): SignInState {
    return rememberSaveable(saver = SignInState.Saver) {
        SignInState()
    }
}

@Stable
class SignInState {
    var email by mutableStateOf("")

    var password by mutableStateOf("")

    fun validateFields(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    companion object {
        val Saver = listSaver<SignInState, Any>(
            save = { state ->
                listOf(
                    state.email,
                    state.password,
                )
            },
            restore = {
                SignInState().apply {
                    email = it[0] as String

                    password = it[1] as String
                }
            },
        )
    }
}
