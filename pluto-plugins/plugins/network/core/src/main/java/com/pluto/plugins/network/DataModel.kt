package com.pluto.plugins.network

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
