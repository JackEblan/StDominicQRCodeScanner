package st.dominic.qrcodescanner.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberSignUpState(): SignUpState {
    return rememberSaveable(saver = SignUpState.Saver) {
        SignUpState()
    }
}

@Stable
class SignUpState {
    var email by mutableStateOf("")

    var password by mutableStateOf("")

    fun validateFields(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    companion object {
        val Saver = listSaver<SignUpState, Any>(
            save = { state ->
                listOf(
                    state.email,
                    state.password,
                )
            },
            restore = {
                SignUpState().apply {
                    email = it[0] as String

                    password = it[1] as String
                }
            },
        )
    }
}
