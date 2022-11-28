package com.dig.enabler

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface ContentRepository: JpaRepository<Content, Int>
