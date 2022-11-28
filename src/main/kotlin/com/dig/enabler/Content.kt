package com.dig.enabler

import jakarta.persistence.*

@Entity
@Table(name = "content")
data class Content (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val title: String,
    val body: String,
)

