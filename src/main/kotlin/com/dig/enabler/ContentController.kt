package com.dig.enabler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/content")
class ContentController {
    val data: MutableList<Content> = mutableListOf()

    @GetMapping
    fun getAll(): List<ContentOverview> {
        return data.map {
            ContentOverview(
                it.id,
                it.title,
            )
        }
    }

    @GetMapping("/{contentId}")
    fun getContentBy(@PathVariable contentId: Int): Content? {
        return data.first { it.id == contentId }
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun saveNewContent(@RequestBody content: Content) {
        data.add(content)
    }
}