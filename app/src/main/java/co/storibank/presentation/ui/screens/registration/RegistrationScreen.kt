package co.storibank.presentation.ui.screens.registration

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import co.storibank.presentation.ui.screens.util.createImageFile
import co.storibank.presentation.ui.screens.util.uriToByteArray
import co.storibank.presentation.viewmodel.RegistrationState
import co.storibank.presentation.viewmodel.RegistrationViewModel
import co.storibank.ui.theme.PurpleGrey80
import coil.compose.rememberAsyncImagePainter
import java.util.Objects

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel) {
    val registrationState by viewModel.registrationState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Registration") },
                modifier = Modifier.background(PurpleGrey80),
            )
        },
        content = {
            RegistrationContent(
                onRegister = { email, password, imageBytes ->
                    viewModel.signUp(email, password, imageBytes)
                },
                registrationState = registrationState,
            )
        },
    )
}

@Composable
fun RegistrationContent(
    onRegister: (String, String, ByteArray) -> Unit,
    registrationState: RegistrationState,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val file = context.createImageFile()
    val uri =
        FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            "co.storibank" + ".provider",
            file,
        )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) {
            if (it) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier =
                    Modifier
                        .padding(16.dp, 8.dp)
                        .size(100.dp)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clip(CircleShape),
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Add Image")
        }

        Button(
            onClick = {
                val byteArray = uriToByteArray(context.contentResolver, capturedImageUri)
                onRegister(email, password, byteArray ?: ByteArray(0))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = registrationState !is RegistrationState.Loading,
        ) {
            if (registrationState is RegistrationState.Loading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Register")
            }
        }

        if (registrationState is RegistrationState.Success) {
            Text(
                text = "Registration Successful",
                color = PurpleGrey80,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        if (registrationState is RegistrationState.Error) {
            Text(
                text = "Registration Error: ${registrationState.message}",
                color = PurpleGrey80,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}
