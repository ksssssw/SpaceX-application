package com.example.spacexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.spacexapp.ui.theme.SpaceXAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RocketItem(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun RocketItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data("https://imgur.com/DaCfMsj.jpg")
                .build(),
            contentDescription = "TEST"
        )

        Text("HI")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpaceXAppTheme {
        RocketItem()
    }
}