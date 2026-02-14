package com.example.mobilecw2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

lateinit var db: AppDatabase
lateinit var leaguesDao: LeaguesDao


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this ,
            AppDatabase::class.java, "mydatabase").build()
        leaguesDao = db.getLeaguesDao()

        setContent {
            Home()
        }
    }
}

@Composable
fun Home() {
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.footballbgmob),
            contentDescription = "BgImg",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
        ){
        Text(
            text = "FOOTBALL LEAGUES",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.Cyan,
        )
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        item{
//            Text(
//                text = "FOOTBALL LEAGUES",
//                fontSize = 30.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Cyan
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//        }
        item {
            Button(onClick = {
                try {
                    scope.launch {
                        val leaguesData= listOf(
                            Leagues(4328, "English Premier League", "Soccer", "Premier League, EPL"),
                            Leagues(4329, "English League Championship", "Soccer", "Championship"),
                            Leagues(4330, "Scottish Premier League", "Soccer", "Scottish Premiership, SPFL"),
                            Leagues(4331, "German Bundesliga", "Soccer", "Bundesliga, Fußball-Bundesliga"),
                            Leagues(4332, "Italian Serie A", "Soccer", "Serie A"),
                            Leagues(4334, "French Ligue 1", "Soccer", "Ligue 1 Conforama"),
                            Leagues(4335, "Spanish La Liga", "Soccer", "LaLiga Santander, La Liga"),
                            Leagues(4336, "Greek Superleague Greece", "Soccer", ""),
                            Leagues(4337, "Dutch Eredivisie", "Soccer", "Eredivisie"),
                            Leagues(4338, "Belgian First Division A", "Soccer", "Jupiler Pro League"),
                            Leagues(4339, "Turkish Super Lig", "Soccer", "Super Lig"),
                            Leagues(4340, "Danish Superliga", "Soccer", ""),
                            Leagues(4344, "Portuguese Primeira Liga", "Soccer", "Liga NOS"),
                            Leagues(4346, "American Major League Soccer", "Soccer", "MLS, Major League Soccer"),
                            Leagues(4347, "Swedish Allsvenskan", "Soccer", "Fotbollsallsvenskan"),
                            Leagues(4350, "Mexican Primera League", "Soccer", "Liga MX"),
                            Leagues(4351, "Brazilian Serie A", "Soccer", ""),
                            Leagues(4354, "Ukrainian Premier League", "Soccer", ""),
                            Leagues(4355, "Russian Football Premier League", "Soccer", "Чемпионат России по футболу"),
                            Leagues(4356, "Australian A-League", "Soccer", "A-League"),
                            Leagues(4358, "Norwegian Eliteserien", "Soccer", "Eliteserien"),
                            Leagues(4359, "Chinese Super League", "Soccer", "")
                        )
                        leaguesData.forEach {
                            lg ->  leaguesDao.insertLeagues(lg)
                        }
                    }

                } catch (_: NumberFormatException){ }

            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)) {
                Text("Add Leagues to DB")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(onClick = {
                val iSearchClubsByLeague= Intent(context,SearchClubsByLeague::class.java)
                context.startActivity(iSearchClubsByLeague)
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)) {
                Text("Search for Clubs By League")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(onClick = {
                val iSearchClubs= Intent(context,SearchClubs::class.java)
                context.startActivity(iSearchClubs)
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)) {
                Text("Search for Clubs")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(onClick = {
                val iSearchClubsFromWeb= Intent(context,SearchClubsFromWeb::class.java)
                context.startActivity(iSearchClubsFromWeb)
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text("Search Clubs from Web")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home()
}