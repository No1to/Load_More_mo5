package com.example.load_more_mo5

data class Data(var category: String) {
    var title: String? = null
    var subtitle: String? = null

    init {
        this.category = category
    }
}