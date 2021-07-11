package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.github.abdallahabdelfattah13.news_simple_app.TestData
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*


@ExperimentalCoroutinesApi
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private val subscribeScheduler = Schedulers.trampoline()
    private val observeScheduler = Schedulers.trampoline()
    private val articlesTemplate = listOf(
        TestData.createNewsArticle(),
        TestData.createNewsArticle()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state reacts correctly to querySubject events`() {
        val q1 = "android"
        val invalidQ = ""
        val q2 = "amazon"
        val q3 = "kotlin"
        val q1Articles =
            articlesTemplate.mapIndexed { index, article -> article.copy(title = "$q1 ${index + 1}") }
        val q2Articles =
            articlesTemplate.mapIndexed { index, article -> article.copy(title = "$q2 ${index + 1}") }
        val q3Articles =
            articlesTemplate.mapIndexed { index, article -> article.copy(title = "$q3 ${index + 1}") }
        val lifecycle = createLifeCycle(Lifecycle.State.RESUMED)
        val differ = AsyncPagingDataDiffer(
            diffCallback = DummyDiffCallback(),
            updateCallback = NoOpListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val queriesQueue: Queue<String> =
            LinkedList(listOf(q1, invalidQ, q2, q3))
        val expectedResults: Queue<List<Article>> =
            LinkedList(listOf(q1Articles, q2Articles, q3Articles))

        val newsRepository: NewsRepository = mock()
        whenever(newsRepository.searchNews(q1))
            .thenReturn(Flowable.just(PagingData.from(q1Articles)))
        whenever(newsRepository.searchNews(q2))
            .thenReturn(Flowable.just(PagingData.from(q2Articles)))
        whenever(newsRepository.searchNews(q3))
            .thenReturn(Flowable.just(PagingData.from(q3Articles)))

        val newsViewModel = NewsViewModel(
            newsRepository,
            subscribeScheduler,
            observeScheduler
        )
        newsViewModel.state.observeForever {
            differ.submitData(lifecycle, it)
            val latestItems = differ.snapshot().items
            Assert.assertEquals(expectedResults.poll(), latestItems)
        }

        queriesQueue
            .forEach { q -> newsViewModel.querySubject.onNext(q) }

        Assert.assertTrue(
            "All expectedResults must be asserted true with the emissions!",
            expectedResults.isEmpty()
        )
    }

    private fun createLifeCycle(state: Lifecycle.State) =
        object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) {
                //no-op
            }

            override fun removeObserver(observer: LifecycleObserver) {
                //no-op
            }

            override fun getCurrentState(): State {
                return state
            }
        }

    class NoOpListCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }

    class DummyDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}