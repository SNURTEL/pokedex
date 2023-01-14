package com.example.pappokedex

import android.app.Application
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.runner.manipulation.Ordering

// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {

                                                                // !
    override fun newApplication(cl: ClassLoader?, name: String?, context: android.content.Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}