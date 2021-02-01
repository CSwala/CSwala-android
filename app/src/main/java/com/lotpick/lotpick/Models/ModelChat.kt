package com.lotpick.lotpick.Models

class ModelChat {
    var message: String? = null
    var receiver: String? = null
    var sender: String? = null
    var timestamp: String? = null
    var isSeen = false

    constructor() {}
    constructor(message: String?, receiver: String?, sender: String?, timestamp: String?, isSeen: Boolean) {
        this.message = message
        this.receiver = receiver
        this.sender = sender
        this.timestamp = timestamp
        this.isSeen = isSeen
    }

    fun setReciever(receiver: String?) {
        this.receiver = receiver
    }
}