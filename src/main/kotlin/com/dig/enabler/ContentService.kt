package com.dig.enabler

import org.springframework.stereotype.Service
import java.util.*

interface ContentService {
    fun getAll(): List<ContentOverview>
    fun getContentBy(contentId: Int): Optional<Content>
    fun save(content: Content)
    fun updateContent(contentId: Int, content: Content)
    fun delete(contentId: Int)
}

@Service
class DefaultContentService(val contentRepository: ContentRepository): ContentService {
    override fun getAll(): List<ContentOverview> {
        val contents = contentRepository.findAll()
        return contents.map {
            ContentOverview(
                it.id,
                it.title,
            )
        }
    }

    override fun getContentBy(contentId: Int): Optional<Content> {
        return  contentRepository.findById(contentId)
    }

    override fun save(content: Content) {
        contentRepository.save(content)
    }

    override fun updateContent(contentId: Int, content: Content) {
        val newContent = Content(
            contentId,
            content.title,
            content.body,
        )
        contentRepository.save(newContent)
    }

    override fun delete(contentId: Int) {
        contentRepository.deleteById(contentId)
    }
}
