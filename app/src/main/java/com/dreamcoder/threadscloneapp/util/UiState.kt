sealed class UiState {
    object Loading : UiState()
    data class Success(val message:String) : UiState()
    data class Error(val message: String) : UiState()
    data class ValidationError(val field: Field, val message: String) : UiState()
    object Empty : UiState()

    enum class Field{
        NAME,USERNAME,EMAIL,BIO,PASSWORD
    }

}