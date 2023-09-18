package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woof.Data.RowDataList
import com.example.woof.model.Dog
import com.example.woof.ui.theme.WoofTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WoofTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                     WoofApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofApp() {
    val dogList = RowDataList().getList()
    Scaffold (
      topBar = {
          WoofAppTopBar()
      }
    ){ paddingValues ->
        LazyColumn(contentPadding = paddingValues){
            items(dogList){
                (WoofCard(rowData = it, modifier = Modifier.padding(10.dp)))
            }
        }
    }
}


@Composable
fun WoofCard(modifier: Modifier = Modifier, rowData: Dog) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.inversePrimary,
        label = "color"
    )
    Card (modifier = modifier
        .fillMaxWidth()
        .clip(shape = (MaterialTheme.shapes.medium))
    ){
        Column(
            Modifier
                .background(color = color)
                .padding(start = 16.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)

                ,
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                DogIcon(modifier = Modifier, rowData)
                DogDetails(rowData = rowData)
                Spacer(modifier = Modifier.weight(1f))
                DogItemButton(
                    modifier = Modifier,
                    expanded = expanded,
                    onClick = {
                        expanded = !expanded
                    }
                )
            }
            if (expanded) {
                DogHobby(dogHobby = rowData.dogDescription, modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofAppTopBar(modifier: Modifier = Modifier) {
    
    CenterAlignedTopAppBar(title = { 
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Image(painter = painterResource(id = R.drawable.ic_woof_logo) , contentDescription = null)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.displayMedium, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        }
    }, modifier = modifier)
}

@Composable
private fun DogItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon1 = painterResource(id = R.drawable.baseline_arrow_drop_up_24)
    val icon2 = painterResource(id = R.drawable.baseline_arrow_drop_down_24)
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(
            painter = if (expanded) icon1 else icon2,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun DogIcon(modifier: Modifier = Modifier, rowData: Dog) {
    Image(
        painter = painterResource(id = rowData.imageRes),
        contentDescription = stringResource(
            id = rowData.dogDescription
        ),
        modifier
            .size(60.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun DogDetails(rowData: Dog) {
    Column(
        Modifier.padding(start = 8.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(
            text = stringResource(id = rowData.name),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${rowData.age} years old",
            fontSize = 14.sp
        )
    }
}

@Composable
fun DogHobby(
    @StringRes dogHobby: Int,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier){
        Text(
            text = stringResource(id = R.string.about),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = dogHobby),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}