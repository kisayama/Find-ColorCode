package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun BasicColorRow(colorList: List<String>,onBasicSquareSelected: (String) -> Unit){
  Row (modifier = Modifier
   .fillMaxWidth()
   .padding(10.dp),
   horizontalArrangement = Arrangement.Center,
   ){
   BasicColorSquare(
    modifier = Modifier.weight(4f),
    colorCode =colorList[0],
    onBasicSquareSelected = onBasicSquareSelected
   )
   BasicColorSquare(
    modifier = Modifier.weight(4f),
    colorCode =colorList[1],
    onBasicSquareSelected = onBasicSquareSelected
   )
   BasicColorSquare(
    modifier = Modifier.weight(4f),
    colorCode =colorList[2],
    onBasicSquareSelected = onBasicSquareSelected
   )
   BasicColorSquare(
    modifier = Modifier.weight(4f),
    colorCode =colorList[3],
    onBasicSquareSelected = onBasicSquareSelected
   )
  }
}

@Composable
 fun BasicColorSquare(
 modifier: Modifier, colorCode:String,
 onBasicSquareSelected: (String)->Unit){
  Box (
   modifier = modifier
    .clickable { onBasicSquareSelected(colorCode) }
    .background(Color(android.graphics.Color.parseColor(colorCode)))
    .size(50.dp)
  )
 }

@Preview
@Composable
fun PreviewBasicColorRow(){
 val colorList = listOf("#FF0000","#00FF00", "#0000FF","#FFFFFF")
 BasicColorRow(colorList, onBasicSquareSelected = {})
}