package com.example.mobilecw2

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


class SearchClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clubsDao = db.getClubsDao()

        setContent {
            SearchForClubs()
        }
    }
}

@Composable
fun SearchForClubs() {
    var clubs by rememberSaveable {
        mutableStateOf<List<Clubs>>(emptyList())
    }
    var keyword by rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    var noSearchResults by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Search for Club",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            TextField(value = keyword, onValueChange = { keyword = it })
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                try {
                    scope.launch{
                        var clubSearch = clubsDao.searchClubsByName(keyword)
                        if (clubSearch.isEmpty()) {
                            noSearchResults = true
                        } else {
                             clubs = clubSearch
                            noSearchResults = false
                        }
                    }
                } catch (_: NumberFormatException){ }
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue))  {
                Text(text = "Search")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (noSearchResults){
            item {
                Text(
                    text = "Search results not found!",
                    color = Color.Red
                )
            }
        } else{
            items(clubs){ club->
                //Text(text = club.strTeam + club.strTeamLogo)
                ClubListItem(club)
            }
        }

    }

}

@Composable
fun ClubListItem(club:Clubs){
    var imageBitmap by rememberSaveable {
        mutableStateOf<ImageBitmap?>(null)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = club.strTeamLogo) {
        val bitmap= withContext(Dispatchers.IO){
            fetchImageBitMap(club.strTeamLogo)
        }
        imageBitmap= bitmap
    }
    imageBitmap.let {
        if (it != null) {
            Image(
                bitmap = it,
                contentDescription = "Club Logo",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        }
        Text(text = club.strTeam)
    }
}

suspend fun fetchImageBitMap(url: String):ImageBitmap{
    val connection= URL(url).openConnection() as HttpURLConnection
    connection.doInput= true
    connection.connect()

    val inputStream= connection.inputStream
    val bitmap= BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SearchForClubs()
}