package com.skeletonviewer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skeletonviewer.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CameraScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun captureButton_isDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Capture")
            .assertIsDisplayed()
    }

    @Test
    fun switchCameraButton_isDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Switch Camera")
            .assertIsDisplayed()
    }

    @Test
    fun settingsButton_isDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Settings")
            .assertIsDisplayed()
    }
}
