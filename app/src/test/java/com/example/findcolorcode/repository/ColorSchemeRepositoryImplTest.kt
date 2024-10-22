package com.example.findcolorcode.repository

import com.example.findcolorcode.api.TheColorApiService
import com.example.findcolorcode.model.ColorCode
import com.example.findcolorcode.model.ColorSchemeResponse
import com.example.findcolorcode.model.HexValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

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