package com.haw.forecastapp.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.haw.forecastapp.R
import com.haw.forecastapp.data.DataOrException
import com.haw.forecastapp.model.Weather
import com.haw.forecastapp.ui.theme.gradientBackgroundGray
import com.haw.forecastapp.ui.theme.gradientBackgroundPrimary
import com.haw.forecastapp.utils.Constants
import com.haw.forecastapp.utils.formatDate
import com.haw.forecastapp.utils.formatDecimals
import com.haw.forecastapp.widgets.WeatherAppBar

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val weatherData =
        produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue =
            DataOrException(loading = true)
        ) {
            value = mainViewModel.getWeatherData(city = "Moscow")
        }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(
            weather = weatherData.data!!,
            navController = navController
        )
    }
}

@Composable
fun MainScaffold(
    weather: Weather,
    navController: NavController
) {
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = weather.city.name + ",${weather.city.country}",
                navController = navController,
            ) {

            }
        }
    ) {
        MainContent(data = weather)
    }
}

@Composable
fun MainContent(data: Weather) {
    val imageUrl = Constants.BASE_IMAGE_URL + data.list[0].weather[0].icon + ".png"
    val weather = data.list[0].weather[0]

    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(brush = gradientBackgroundGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = formatDate(data.list[0].dt), // Wed Nov 30
            style = MaterialTheme.typography.h2,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(200.dp)
                .background(
                    brush = gradientBackgroundPrimary,
                    shape = RoundedCornerShape(24.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = formatDecimals(data.list[0].temp.day) + "°",
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = weather.main,
                    style = MaterialTheme.typography.h3,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = stringResource(R.string.icon_image),
        modifier = Modifier.size(100.dp)
    )
}




