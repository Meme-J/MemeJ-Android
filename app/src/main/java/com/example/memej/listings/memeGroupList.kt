package com.example.memej.listings

import com.example.memej.containers.memeGroupPostContainer

class memeGroupList(
    val children: List<memeGroupPostContainer>,
    val after: String?,
    val before: String?
)
