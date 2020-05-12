package com.example.memej.listings

import com.example.memej.containers.memeTemplatePostContainer

class memeTemplateList(
    val children: List<memeTemplatePostContainer>,
    val after: String?,
    val before: String?
)