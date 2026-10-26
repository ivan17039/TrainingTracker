package com.ivanb.trainingtracker

import com.ivanb.trainingtracker.data.toInputString
import org.junit.Test
import kotlin.test.assertEquals

class InputFormatTest {
    @Test
    fun wholeNumbersHaveNoDecimalPoint() {
        assertEquals("60", 60.0.toInputString())
        assertEquals("0", 0.0.toInputString())
    }

    @Test
    fun fractionsAreKept() {
        assertEquals("62.5", 62.5.toInputString())
    }
}