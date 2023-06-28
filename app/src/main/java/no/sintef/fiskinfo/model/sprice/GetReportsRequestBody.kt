package no.sintef.fiskinfo.model.sprice

class GetReportsRequestBody internal constructor(
    val Username: String,
    val Password: String
) {
    data class Builder(
        var Username: String = "",
        var Password: String = ""
    ) {
        fun Username(username: String) = apply { this.Username = username }
        fun Password(password: String) = apply { this.Password = password }
    }
}