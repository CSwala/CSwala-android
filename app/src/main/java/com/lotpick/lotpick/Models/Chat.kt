package com.lotpick.lotpick.Models

class Chat {
    var sender: String? = null
    var receiver: String? = null
    var message: String? = null
    var isseen = false

    constructor() {}
    constructor(sender: String?, receiver: String?, message: String?, isseen: Boolean) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.isseen = isseen
    }
}