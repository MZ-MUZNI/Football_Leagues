package com.example.mobilecw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

lateinit var clubsDao: ClubsDao
val clubsList = mutableListOf<Clubs>()

class SearchClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clubsDao = db.getClubsDao()

        setContent {
            SearchByLeague()
        }
    }
}



@Composable
fun SearchByLeague() {
    var leagueInfoDisplay by rememberSaveable {
        mutableStateOf("")
    }
    var keyword by rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Search Club by League",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            TextField(value = keyword, onValueChange = { keyword = it })
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch {
                    leagueInfoDisplay= fetchClubs(keyword)

                }
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)) {
                Text("Retrieve Clubs")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                try {
                    scope.launch {
                        clubsDao.insertClubs(clubsList)

                    }

                }catch (_: NumberFormatException){ }
            }, modifier = Modifier.width(238.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)) {
                Text("Save clubs to Database")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Text(
                //modifier = Modifier.verticalScroll(rememberScrollState()),
                text = leagueInfoDisplay
            )
        }
    }

}



suspend fun fetchClubs(keyword: String):String {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${keyword}"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    var stb = StringBuilder() // collecting all the JSON string

    // run the code of the launched coroutine in a new thread
    withContext(Dispatchers.IO) {
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) { // keeps reading until no more lines of text
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    val allClubs = parseJSON(stb)
    return allClubs
}


fun parseJSON(stb: StringBuilder): String{
    val json = JSONObject(stb.toString()) // full JSON returned by the Web Service
    var allClubs = StringBuilder() // Info about all the clubs extracted by this function
    //var jsonArray: JSONArray = json.getJSONArray("teams")
    var jsonArray: JSONArray = json.optJSONArray("teams") ?: JSONArray()

    if (jsonArray.length() == 0) {
        return "Search results not found!"
    }

    for (i in 0 until jsonArray.length()){
        val team: JSONObject= jsonArray.getJSONObject(i)

        val idTeam= team.getString("idTeam")
        val name= team.getString("strTeam")
        val shortName= team.getString("strTeamShort")
        val alternateNames = team.getString("strAlternate")
        val formedYear= team.getString("intFormedYear")
        val league= team.getString("strLeague")
        val leagueId = team.getString("idLeague")
        val stadium= team.getString("strStadium")
        val stadiumLocation = team.getString("strStadiumLocation")
        val stadiumCapacity = team.getString("intStadiumCapacity")
        val website= team.getString("strWebsite")
        val keywords= team.getString("strKeywords")
        val stadiumThumb= team.getString("strStadiumThumb")
        val teamJersey = team.getString("strTeamJersey")
        val teamLogo= team.getString("strTeamLogo")

        allClubs.append("\"idTeam\":\"$idTeam\",\n")
        allClubs.append("\"Name\":\"$name\",\n")
        allClubs.append("\"strTeamShort\":\"$shortName\",\n")
        allClubs.append("\"strAlternate\":\"$alternateNames\",\n")
        allClubs.append("\"intFormedYear\":\"$formedYear\",\n")
        allClubs.append("\"strLeague\":\"$league\",\n")
        allClubs.append("\"idLeague\":\"$leagueId\",\n")
        allClubs.append("\"strStadium\":\"$stadium\",\n")
        allClubs.append("\"strStadiumLocation\":\"$stadiumLocation\",\n")
        allClubs.append("\"intStadiumCapacity\":\"$stadiumCapacity\",\n")
        allClubs.append("\"strWebsite\":\"$website\",\n")
        allClubs.append("\"strKeywords\":\"$keywords\",\n")
        allClubs.append("\"strStadiumThumb\":\"$stadiumThumb\",\n")
        allClubs.append("\"strTeamJersey\":\"$teamJersey\",\n")
        allClubs.append("\"strTeamLogo\":\"$teamLogo\"\n\n")

        val club = Clubs(
            idTeam = idTeam,
            strTeam = name,
            strTeamShort = shortName,
            strAlternate = alternateNames,
            intFormedYear = formedYear,
            strLeague = league,
            idLeague = leagueId,
            strStadium = stadium,
            strStadiumLocation = stadiumLocation,
            intStadiumCapacity = stadiumCapacity,
            strWebsite = website,
            strKeywords = keywords,
            strStadiumThumb = stadiumThumb,
            strTeamJersey = teamJersey,
            strTeamLogo = teamLogo
        )
        clubsList.add(club)
    }
    return allClubs.toString()
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
        SearchByLeague()
}