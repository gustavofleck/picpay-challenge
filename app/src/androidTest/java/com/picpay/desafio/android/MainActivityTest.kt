package com.picpay.desafio.android

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import com.picpay.desafio.android.RecyclerViewMatchers.checkRecyclerViewItem
import com.picpay.desafio.android.utils.KoinTestRunner
import com.picpay.desafio.android.view.MainActivity
import com.picpay.desafio.android.viewmodel.UsersViewModel
import io.mockk.mockk
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private val intent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    private lateinit var mainActivity: ActivityScenario<MainActivity>
    private lateinit var mContext: Context
    private val viewModelMock = mockk<UsersViewModel>(relaxed = true)

    @Before
    fun setUp() {
        loadKoinModules(module {
            viewModel { viewModelMock }
        })
        mainActivity = launchActivity(intent)
        mainActivity.onActivity {
            mContext = it
        }
    }

    @Test
    fun shouldDisplayTitle() {
        mainActivity.moveToState(Lifecycle.State.RESUMED)

        mainActivity.onActivity {
            val expectedTitle = mContext.getString(R.string.title)

            onView(withText(expectedTitle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayListItem() {
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/users" -> successResponse
                    else -> errorResponse
                }
            }
        }

        server.start(serverPort)

        launchActivity<MainActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)

            checkRecyclerViewItem(R.id.name, 0, isDisplayed())
            checkRecyclerViewItem(R.id.username, 0, isDisplayed())
            checkRecyclerViewItem(R.id.picture, 0, isDisplayed())
        }

        server.close()
    }

    companion object {
        private const val serverPort = 8080

        private val successResponse by lazy {
            val body =
                "[{\"id\":1001,\"name\":\"Eduardo Santos\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@eduardo.santos\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy { MockResponse().setResponseCode(404) }
    }
}
