package st.dominic.qrcodescanner.core.model

data class BorrowBookResult(
    val progress: Float? = null,
    val downloadLink: String? = null,
    val exception: Exception? = null,
    val success: Boolean? = null,
    val notSignedIn: Boolean? = null,
)
