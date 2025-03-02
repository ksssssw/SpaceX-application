package com.example.spacexapp.presentation.screens.rocketdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.spacexapp.domain.models.Rocket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketDetailScreen(
    rocketId: String,
    navController: NavController,
    viewModel: RocketDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Load rocket details when the screen is shown
    viewModel.loadRocketDetails(rocketId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.rocket?.name ?: "Rocket Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadRocketDetails(rocketId) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
                uiState.rocket != null -> {
                    RocketDetailContent(rocket = uiState.rocket!!)
                }
            }
        }
    }
}

@Composable
fun RocketDetailContent(rocket: Rocket) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Image
        if (rocket.images.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(rocket.images.first())
                    .crossfade(true)
                    .build(),
                contentDescription = "Rocket image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        // Header with name and status
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rocket.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatusChip(active = rocket.active)
        }

        // First flight info
        Text(
            text = "First flight: ${rocket.firstFlight}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Description
        Text(
            text = rocket.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Divider()

        // Technical specifications section
        Text(
            text = "Technical Specifications",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        SpecificationCard(rocket)

        Divider()

        // Performance section
        Text(
            text = "Performance",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        PerformanceCard(rocket)

        // Image gallery
        if (rocket.images.size > 1) {
            Divider()

            Text(
                text = "Gallery",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display additional images
            rocket.images.drop(1).forEach { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Rocket gallery image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatusChip(active: Boolean) {
    val icon = if (active) Icons.Default.Check else Icons.Default.Close
    val tint = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val label = if (active) "Active" else "Inactive"

    Surface(
        color = tint.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = tint
            )
        }
    }
}

@Composable
fun SpecificationCard(rocket: Rocket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpecItem("Height", "${rocket.height} meters")
            SpecItem("Diameter", "${rocket.diameter} meters")
            SpecItem("Mass", "${rocket.mass / 1000} tons")
            SpecItem("Stages", rocket.stages.toString())
            SpecItem("Boosters", rocket.boosters.toString())
        }
    }
}

@Composable
fun PerformanceCard(rocket: Rocket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpecItem("Cost per Launch", "$${rocket.costPerLaunch / 1000000}M")
            SpecItem("Success Rate", "${rocket.successRatePct}%")
        }
    }
}

@Composable
fun SpecItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}