package st.dominic.qrcodescanner.feature.borrowbook

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import st.dominic.qrcodescanner.core.model.LocalBook

@Composable
fun rememberBorrowBookState(): BorrowBookState {
    return rememberSaveable(saver = BorrowBookState.Saver) {
        BorrowBookState()
    }
}

@Stable
class BorrowBookState {
    var imageUri by mutableStateOf<Uri>(Uri.EMPTY)

    var qrCode by mutableStateOf("")

    var title by mutableStateOf("")

    var author by mutableStateOf("")

    var snackbarMessage by mutableStateOf("")

    fun buildLocalBook(): LocalBook? {
        if (imageUri == Uri.EMPTY) {
            snackbarMessage = "Image is empty"
            return null
        }

        if (qrCode.isEmpty()) {
            snackbarMessage = "QR Code is empty"
            return null
        }

        if (title.isEmpty()) {
            snackbarMessage = "Title is empty"
            return null
        }

        if (author.isEmpty()) {
            snackbarMessage = "Author is empty"
            return null
        }

        return LocalBook(
            imageUri = imageUri, qrCode = qrCode, title = title, author = author
        )
    }

    companion object {
        val Saver = listSaver<BorrowBookState, Any>(
            save = { state ->
                listOf(
                    state.imageUri,
                    state.qrCode,
                    state.author,
                    state.title,
                )
            },
            restore = {
                BorrowBookState().apply {
                    imageUri = it[0] as Uri

                    qrCode = it[1] as String

                    title = it[2] as String

                    author = it[3] as String
                }
            },
        )
    }
}
