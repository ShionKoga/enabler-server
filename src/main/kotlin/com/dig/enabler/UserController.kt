package com.dig.enabler

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController {
    @GetMapping("/me")
    fun getMe(@RequestHeader headers: Map<String, String>): String {
        val authorization = headers["Authorization"]
        println()
        println(authorization)
        println()
        return "me!!!!!!"
    }
}