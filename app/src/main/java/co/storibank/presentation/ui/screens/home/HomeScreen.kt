package co.storibank.presentation.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.storibank.R
import co.storibank.domain.model.BankInfo
import co.storibank.domain.model.BankMovement
import co.storibank.presentation.ui.screens.util.format
import co.storibank.presentation.viewmodel.HomeViewModel
import co.storibank.presentation.viewmodel.state.BankInfoState
import co.storibank.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToMovementDetail: (String) -> Unit,
) {
    val bankInfoState by viewModel.bankInfoState.collectAsState()

    if (bankInfoState is BankInfoState.Idle) {
        viewModel.fetchBankInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.bank_information_title)) },
                modifier = Modifier.background(PurpleGrey80),
            )
        },
        content = {
            when (bankInfoState) {
                is BankInfoState.Loading -> {
                    LoadingContent()
                }

                is BankInfoState.Success -> {
                    val bankInfo = (bankInfoState as BankInfoState.Success).bankInfo
                    BankInfoContent(
                        bankInfo = bankInfo,
                        navigateToMovementDetail = navigateToMovementDetail,
                    )
                }

                is BankInfoState.Error -> {
                    val errorMessage = (bankInfoState as BankInfoState.Error).message
                    ErrorContent(errorMessage = errorMessage)
                }

                else -> {
                    ErrorContent(errorMessage = stringResource(R.string.error_general))
                }
            }
        },
    )
}

@Composable
private fun BankInfoContent(
    bankInfo: BankInfo,
    navigateToMovementDetail: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${stringResource(R.string.bank_balance)} ${bankInfo.balance}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        LazyColumn {
            items(bankInfo.movements) { movement ->
                MovementItem(
                    movement = movement,
                    onItemClick = { navigateToMovementDetail(movement.id) },
                )
            }
        }
    }
}

@Composable
private fun MovementItem(
    movement: BankMovement,
    onItemClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onItemClick() },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.movement_description, movement.description))
            Text(text = stringResource(R.string.movement_amount, movement.amount))
            Text(text = stringResource(R.string.movement_date, movement.date.format()))
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Red,
        )
    }
}
