package co.storibank.presentation.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.storibank.R
import co.storibank.domain.model.BankMovement
import co.storibank.presentation.ui.screens.util.format

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovementDetailScreen(
    movement: BankMovement?,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.movement_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_description),
                        )
                    }
                },
            )
        },
        content = {
            MovementDetails(movement = movement)
        },
    )
}

@Composable
private fun MovementDetails(movement: BankMovement?) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        movement?.let {
            Text(text = "${stringResource(R.string.movement_id_label)} ${it.id}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${stringResource(R.string.amount_label)} ${it.amount}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${stringResource(R.string.description_label)} ${it.description}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${stringResource(R.string.date_label)} ${it.date.format()}")
        }
    }
}
