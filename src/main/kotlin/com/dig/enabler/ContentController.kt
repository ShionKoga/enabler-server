package com.dig.enabler

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/content")
class ContentController {
    @GetMapping
    fun getAll(): List<String> {
        return listOf(
            "hoge",
            "fuga",
            "piyo",
        )
    }
}