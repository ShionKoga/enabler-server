package com.dig.enabler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EnablerApplication

fun main(args: Array<String>) {
	runApplication<EnablerApplication>(*args)
}
