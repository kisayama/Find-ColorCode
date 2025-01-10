package com.example.findcolorcode.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ColorSchemeRepositoryImplTest {

    private val repository = ColorSchemeRepositoryImpl()

    @Test
    fun testGetColorScheme() = runBlocking {
        val colorCode = "ff0000"
        val colorList = repository.getColorScheme(colorCode,"analogic", "json",5)

        assertNotNull(colorList)
        assertEquals(5,colorList.size)
        colorList.forEach{
            colorCode ->
            println("HexValue:$colorCode")
        }
    }

}