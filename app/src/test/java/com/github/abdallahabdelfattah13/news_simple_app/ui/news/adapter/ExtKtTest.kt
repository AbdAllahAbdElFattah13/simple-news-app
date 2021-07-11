package com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter

import com.github.abdallahabdelfattah13.news_simple_app.TestData
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat

class ExtKtTest {

    private val datePattern = "dd-MM-yy"
    private val sdf = SimpleDateFormat(datePattern)
    private val dateString = "05-05-19"
    private val articleTemplate = TestData.createNewsArticle()

    @Test
    fun `formatAuthorName - author name returned as url`() {
        val article = articleTemplate.copy(
            author = "https://www.engadget.com/about/editors/cherlynn-low"
        )
        val expected = "Cherlynn Low"
        val actual = article.formatAuthorName()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `formatAuthorName - author name as just the name`() {
        val article = articleTemplate.copy(
            author = "Cherlynn Low"
        )
        val expected = "Cherlynn Low"
        val actual = article.formatAuthorName()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun formatArticleDate() {
        val article = articleTemplate.copy(
            date = sdf.parse(dateString)!!
        )
        val expected = dateString
        val actual = article.formatArticleDate()

        Assert.assertEquals(expected, actual)
    }
}