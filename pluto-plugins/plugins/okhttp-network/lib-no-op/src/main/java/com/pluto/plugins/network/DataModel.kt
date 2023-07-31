package com.pluto.plugins.network

/**
 * Request class for Custom Network traces
 *
 * @param url request url
 * @param method request method
 * @param body request body
 * @param headers request header map
 * @param sendTimeMillis request send timestamp
 * @param isGzipped is request gzipped
 */
data class CustomRequest(
    val url: String,
    val method: RequestMethod,
    val body: CustomBody?,
    val headers: Map<String, String?>,
    val sendTimeMillis: Long,
    val isGzipped: Boolean
)

/**
 * Response class for Custom Network traces
 *
 * @param statusCode response HTTP status code
 * @param body response body
 * @param headers response header map
 * @param sendTimeMillis request send timestamp
 * @param receiveTimeMillis response receive timestamp
 * @param isGzipped is response gzipped
 */
data class CustomResponse(
    val statusCode: Int,
    val body: CustomBody?,
    val headers: Map<String, String?>,
    val sendTimeMillis: Long,
    val receiveTimeMillis: Long,
    val isGzipped: Boolean
)

data class CustomBody(
    val body: CharSequence,
    val mediaType: BodyMediaType,
    val mediaSubtype: BodyMediaSubType
)

enum class RequestMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
}

/**
 * Custom representation of high-level media type, such as "text", "image", "audio", "video", or "application".
 */
enum class BodyMediaType {
    APPLICATION,
    TEXT,
    IMAGE,
    AUDIO,
    VIDEO
}

/**
 * Custom representation of specific media subtype, such as "plain" or "png", "mpeg", "mp4" or "xml"
 */
enum class BodyMediaSubType {
    JSON,
    XML,
    HTML,
    X_WWW_FORM_URLENCODED,
    PLAIN,
    PNG,
    MPEG,
    MP4
}
