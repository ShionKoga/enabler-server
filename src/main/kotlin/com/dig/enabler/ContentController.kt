package com.dig.enabler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/content")
class ContentController(val contentService: ContentService) {
    @GetMapping
    fun getAll(): List<ContentOverview> {
        return contentService.getAll()
    }

    @GetMapping("/{contentId}")
    fun getContentBy(@PathVariable contentId: Int): Optional<Content> {
        return contentService.getContentBy(contentId)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun saveNewContent(@RequestBody content: Content): Content {
        contentService.save(content)
        return content
    }

    @PutMapping("/{contentId}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateContent(@PathVariable contentId: Int, @RequestBody content: Content): Content {
        contentService.updateContent(contentId, content)
        return content
    }

    @DeleteMapping("/{contentId}")
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteContent(@PathVariable contentId: Int): List<ContentOverview> {
        contentService.delete(contentId)
        return contentService.getAll()
    }
}