package com.example.spacexapp.presentation.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spacexapp.domain.models.CompanyInfo

@Composable
fun InfoScreen(
    viewModel: InfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
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
                    androidx.compose.material3.Button(
                        onClick = { viewModel.loadCompanyInfo() }
                    ) {
                        Text("Retry")
                    }
                }
            }
            uiState.companyInfo != null -> {
                CompanyInfoContent(companyInfo = uiState.companyInfo!!)
            }
        }
    }
}

@Composable
fun CompanyInfoContent(companyInfo: CompanyInfo) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = companyInfo.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = companyInfo.summary,
            style = MaterialTheme.typography.bodyLarge
        )

        Divider()

        InfoCard("회사 정보") {
            InfoItem("설립자", companyInfo.founder)
            InfoItem("설립년도", companyInfo.founded.toString())
            InfoItem("CEO", companyInfo.ceo)
            InfoItem("CTO", companyInfo.cto)
            InfoItem("COO", companyInfo.coo)
            InfoItem("CTO Propulsion", companyInfo.ctoPropulsion)
        }

        InfoCard("통계") {
            InfoItem("직원 수", companyInfo.employees.toString())
            InfoItem("차량 수", companyInfo.vehicles.toString())
            InfoItem("발사 사이트", companyInfo.launchSites.toString())
            InfoItem("테스트 사이트", companyInfo.testSites.toString())
            InfoItem("기업 가치", "$${companyInfo.valuation/1000000000}B")
        }

        InfoCard("본사") {
            InfoItem("주소", companyInfo.headquarters.address)
            InfoItem("도시", companyInfo.headquarters.city)
            InfoItem("주", companyInfo.headquarters.state)
        }

        InfoCard("링크") {
            InfoItem("웹사이트", companyInfo.links.website)
            InfoItem("Flickr", companyInfo.links.flickr)
            InfoItem("Twitter", companyInfo.links.twitter)
            InfoItem("Elon Twitter", companyInfo.links.elonTwitter)
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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