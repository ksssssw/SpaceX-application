package com.example.spacexapp.presentation.screens.rockets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.spacexapp.domain.models.Rocket
import com.example.spacexapp.presentation.theme.SpaceXAppTheme

@Composable
fun RocketsScreen(
    viewModel: RocketsViewModel = hiltViewModel(),
    onRocketClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading && uiState.rockets.isEmpty() -> {
                CircularProgressIndicator()
            }

            uiState.error != null && uiState.rockets.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "오류: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.Button(
                        onClick = { viewModel.loadFirstPage() }
                    ) {
                        Text("재시도")
                    }
                }
            }

            uiState.rockets.isNotEmpty() -> {
                RocketsList(
                    rockets = uiState.rockets,
                    isLoadingMore = uiState.isLoadingMore,
                    onLoadMore = { viewModel.loadNextPage() },
                    onRocketClick = onRocketClick
                )
            }

            else -> {
                Text(text = "로켓을 찾을 수 없습니다")
            }
        }
    }
}

@Composable
fun RocketsList(
    rockets: List<Rocket>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onRocketClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    // 리스트 스크롤 감지 로직
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= rockets.size - 2
        }
    }

    // 추가 아이템 로드
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !isLoadingMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(rockets) { rocket ->
            RocketItem(
                rocket = rocket,
                onClick = { onRocketClick(rocket.id) }
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RocketItem(rocket: Rocket, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (rocket.images.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(rocket.images.first())
                        .crossfade(true)
                        .build(),
                    contentDescription = "로켓 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rocket.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusIcon(active = rocket.active)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "첫 비행: ${rocket.firstFlight}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rocket.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RocketStat(label = "높이", value = "${rocket.height} m")
                RocketStat(label = "직경", value = "${rocket.diameter} m")
                RocketStat(label = "무게", value = "${rocket.mass / 1000} 톤")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RocketStat(
                    label = "발사 비용",
                    value = "$${rocket.costPerLaunch / 1000000}M"
                )
                RocketStat(label = "성공률", value = "${rocket.successRatePct}%")
                RocketStat(label = "단계", value = rocket.stages.toString())
            }
        }
    }
}

@Composable
fun StatusIcon(active: Boolean) {
    val icon = if (active) Icons.Default.Check else Icons.Default.Close
    val tint = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val label = if (active) "활성" else "비활성"

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
fun RocketStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RocketItemPreview() {
    SpaceXAppTheme {
        RocketItem(
            rocket = Rocket(
                id = "1",
                name = "Falcon 9",
                description = "Falcon 9 is a reusable, two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of people and payloads into Earth orbit and beyond.",
                firstFlight = "2010-06-04",
                active = true,
                stages = 2,
                boosters = 0,
                costPerLaunch = 50000000,
                successRatePct = 98,
                height = 70.0,
                diameter = 3.7,
                mass = 549054,
                images = listOf(
                    "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg"
                )
            ),
            onClick = {}
        )
    }
}