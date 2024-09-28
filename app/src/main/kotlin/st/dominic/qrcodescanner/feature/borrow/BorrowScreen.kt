package st.dominic.qrcodescanner.feature.borrow

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import st.dominic.qrcodescanner.R
import st.dominic.qrcodescanner.core.designsystem.component.ShimmerImage

@Composable
fun BorrowRoute(modifier: Modifier = Modifier, borrowViewModel: BorrowViewModel = hiltViewModel()) {
    BorrowScreen(modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowScreen(modifier: Modifier = Modifier, scrollState: ScrollState = rememberScrollState()) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    var title by rememberSaveable { mutableStateOf("") }

    var author by rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.borrow_book),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            scrollBehavior = topAppBarScrollBehavior,
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Upload, contentDescription = "")
        }
    }) { paddingValues ->
        Column(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            ShimmerImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { },
                imageUrl = "",
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
                              value = "",
                              onValueChange = {},
                              readOnly = true,
                              label = {
                                  Text(text = "Qr Code Result")
                              },
                              trailingIcon = {
                                  IconButton(onClick = {}) {
                                      Icon(
                                          imageVector = Icons.Default.QrCode,
                                          contentDescription = ""
                                      )
                                  }
                              })

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
                              value = title,
                              onValueChange = {
                                  title = it
                              },
                              label = {
                                  Text(text = "Title")
                              })

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
                              value = author,
                              onValueChange = {
                                  author = it
                              },
                              label = {
                                  Text(text = "Author")
                              })
        }
    }

}