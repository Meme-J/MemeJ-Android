package com.example.memej.listings

import com.example.memej.containers.homeMemePostContainer

class homeMemeList(
    val children: List<homeMemePostContainer>,
    val after: String?,
    val before: String?
)