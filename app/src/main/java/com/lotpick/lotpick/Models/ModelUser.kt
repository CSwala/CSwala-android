package com.lotpick.lotpick.Models

class ModelUser {
    var id: String? = null
    var username: String? = null
    var imageurl: String? = null

    constructor(id: String?, username: String?, imageurl: String?) {
        this.id = id
        this.username = username
        this.imageurl = imageurl
    }

    constructor() {}
}