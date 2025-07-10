package com.example.keygencetestapp.pages.stockInPage.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.R
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.UnitTypes
import com.example.keygencetestapp.pages.stockInPage.viewModel.NewItemViewModel
import com.example.keygencetestapp.utils.ResponseState
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.example.keygencetestapp.components.PortableAlertDialog
import com.example.keygencetestapp.components.PortableDropdown
import compose.icons.TablerIcons
import compose.icons.tablericons.AccessPointOff


val Hashtag: ImageVector
    get() {
        if (_Hashtag != null) return _Hashtag!!

        _Hashtag = ImageVector.Builder(
            name = "Hashtag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.25f, 8.25f)
                horizontalLineTo(20.25f)
                moveTo(3.75f, 15.75f)
                horizontalLineTo(18.75f)
                moveTo(16.95f, 2.25f)
                lineTo(13.05f, 21.75f)
                moveTo(10.9503f, 2.25f)
                lineTo(7.05029f, 21.75f)
            }
        }.build()

        return _Hashtag!!
    }

private var _Hashtag: ImageVector? = null

val Tag: ImageVector
    get() {
        if (_Tag != null) return _Tag!!

        _Tag = ImageVector.Builder(
            name = "Tag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9.56802f, 3f)
                horizontalLineTo(5.25f)
                curveTo(4.00736f, 3f, 3f, 4.00736f, 3f, 5.25f)
                verticalLineTo(9.56802f)
                curveTo(3f, 10.1648f, 3.23705f, 10.7371f, 3.65901f, 11.159f)
                lineTo(13.2401f, 20.7401f)
                curveTo(13.9388f, 21.4388f, 15.0199f, 21.6117f, 15.8465f, 21.0705f)
                curveTo(17.9271f, 19.7084f, 19.7084f, 17.9271f, 21.0705f, 15.8465f)
                curveTo(21.6117f, 15.0199f, 21.4388f, 13.9388f, 20.7401f, 13.2401f)
                lineTo(11.159f, 3.65901f)
                curveTo(10.7371f, 3.23705f, 10.1648f, 3f, 9.56802f, 3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 6f)
                horizontalLineTo(6.0075f)
                verticalLineTo(6.0075f)
                horizontalLineTo(6f)
                verticalLineTo(6f)
                close()
            }
        }.build()

        return _Tag!!
    }

private var _Tag: ImageVector? = null

val Scale: ImageVector
    get() {
        if (_Scale != null) return _Scale!!

        _Scale = ImageVector.Builder(
            name = "Scale",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3f)
                verticalLineTo(20.25f)
                moveTo(12f, 20.25f)
                curveTo(10.528f, 20.25f, 9.1179f, 20.515f, 7.81483f, 21f)
                moveTo(12f, 20.25f)
                curveTo(13.472f, 20.25f, 14.8821f, 20.515f, 16.1852f, 21f)
                moveTo(18.75f, 4.97089f)
                curveTo(16.5446f, 4.66051f, 14.291f, 4.5f, 12f, 4.5f)
                curveTo(9.70897f, 4.5f, 7.45542f, 4.66051f, 5.25f, 4.97089f)
                moveTo(18.75f, 4.97089f)
                curveTo(19.7604f, 5.1131f, 20.7608f, 5.28677f, 21.75f, 5.49087f)
                moveTo(18.75f, 4.97089f)
                lineTo(21.3704f, 15.6961f)
                curveTo(21.4922f, 16.1948f, 21.2642f, 16.7237f, 20.7811f, 16.8975f)
                curveTo(20.1468f, 17.1257f, 19.4629f, 17.25f, 18.75f, 17.25f)
                curveTo(18.0371f, 17.25f, 17.3532f, 17.1257f, 16.7189f, 16.8975f)
                curveTo(16.2358f, 16.7237f, 16.0078f, 16.1948f, 16.1296f, 15.6961f)
                lineTo(18.75f, 4.97089f)
                close()
                moveTo(2.25f, 5.49087f)
                curveTo(3.23922f, 5.28677f, 4.23956f, 5.1131f, 5.25f, 4.97089f)
                moveTo(5.25f, 4.97089f)
                lineTo(7.87036f, 15.6961f)
                curveTo(7.9922f, 16.1948f, 7.76419f, 16.7237f, 7.28114f, 16.8975f)
                curveTo(6.6468f, 17.1257f, 5.96292f, 17.25f, 5.25f, 17.25f)
                curveTo(4.53708f, 17.25f, 3.8532f, 17.1257f, 3.21886f, 16.8975f)
                curveTo(2.73581f, 16.7237f, 2.5078f, 16.1948f, 2.62964f, 15.6961f)
                lineTo(5.25f, 4.97089f)
                close()
            }
        }.build()

        return _Scale!!
    }

private var _Scale: ImageVector? = null


@Composable
fun NewItemScreen(
    itemCode: String,
    modifier: Modifier = Modifier,
    newItemViewModel: NewItemViewModel = hiltViewModel(),
    navController: NavController
) {
    val itemState by newItemViewModel.initialProduct.collectAsState()
    LaunchedEffect(Unit) {
        newItemViewModel.getProductByItemCode2(itemCode);
    }
    Surface(
        modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val item = itemState) {
                is ResponseState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResponseState.Success -> {
                    val successState = item as ResponseState.Success<Product?>
                    if (successState.data != null) { // Ensure the loaded item matches the ID
                        StockInItem(product = successState.data, navController = navController)
                    } else {
                        CreateNewOne(id = itemCode)
                    }
                }

                is ResponseState.Error -> {
                    Text("Error")
                }

                is ResponseState.Idle -> {
                    // Show a loading indicator or placeholder while waiting for LaunchedEffect to trigger
                }
            }
        }
    }
}

@Composable
fun CreateNewOne(modifier: Modifier = Modifier, id: String) {
    var isAcceptedCreate by remember { mutableStateOf(false) }
    Surface(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isAcceptedCreate) {
                ElevatedCard(Modifier.padding(20.dp)) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Item ")
                        Text("**${id}**", fontWeight = FontWeight.ExtraBold)
                        Text("is not exist yet!")
                    }
                }
                Text("Create new one?")
                Button({
                    isAcceptedCreate = true
                }) {
                    Text("Create")
                }
            } else {
                CreateNewItemForm(id)
            }
        }
    }
}

@Composable
fun CreateNewItemForm(
    code: String,
    modifier: Modifier = Modifier,
    newItemViewModel: NewItemViewModel = hiltViewModel()
) {
    //Initial
    var itemCode by remember { mutableStateOf(code) }
    var itemName by remember { mutableStateOf("") }
    val regex = Regex("^[A-Za-z0-9_-]+$")
    val itemUnit = rememberTextFieldState(UnitTypes.values()[0].toString())
    //Error Handling
    val itemCodeErrorMessage = stringResource(R.string.Item_Code_Error_Message)
    val isErrorItemCode = !regex.matches(itemCode)
    val isErrorItemName = itemName.isEmpty()
    val itemNameErrorMessage = stringResource(id = R.string.Item_Name_Error_Message)
    val createItemResult by
    newItemViewModel.createProductState.collectAsState()
    val isValidForCreation = !isErrorItemCode && !isErrorItemName


    when (createItemResult) {
        is ResponseState.Success -> {
            Toast.makeText(LocalContext.current, "Created successfully!", Toast.LENGTH_SHORT).show()
        }

        is ResponseState.Error -> {
            ErrorPrompt(createItemResult as ResponseState.Error<Boolean?>)
        }

        is ResponseState.Loading -> {

        }

        else -> {} // Handle Idle state if necessary, or leave empty
    }


    Surface(
        modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.heroicons_inbox_arrow_down),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.2f)
                    .size(64.dp) // Scale the icon 2x (assuming original size is around 32dp)
                    .align(Alignment.CenterHorizontally),

            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            "Item code",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        OutlinedTextField(
                            itemCode,
                            enabled =
                                createItemResult !is ResponseState.Loading,
                            leadingIcon = {
                                Icon(Hashtag, null)
                            },
                            onValueChange = { itemCode = it.trim() },
                            isError = isErrorItemCode,
                            supportingText = {
                                if (isErrorItemCode) {
                                    Text(
                                        text = itemCodeErrorMessage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            },
                            label = { Text("Enter item code") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.3f
                                )
                            )
                        )

                    }
                }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            "Item Name",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        OutlinedTextField(
                            itemName,
                            leadingIcon = {
                                Icon(Tag, null)
                            },
                            onValueChange = { itemName = it },
                            isError = isErrorItemName,
                            enabled =
                                createItemResult !is ResponseState.Loading,
                            supportingText = {
                                if (isErrorItemName) {
                                    Text(
                                        text = itemNameErrorMessage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            },
                            label = { Text("Enter item name") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.3f
                                )
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            "Item Unit",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        PortableDropdown(
                            options = UnitTypes.entries.map { it.name },
                            textFieldState = itemUnit,
                            isEnable =
                                createItemResult !is ResponseState.Loading,
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            newItemViewModel.createProduct(
                                Product(
                                    itemCode = itemCode,
                                    name = itemName,
                                    unit = itemUnit.text.toString()
                                )
                            )
                        },
                        enabled = isValidForCreation && createItemResult !is ResponseState.Loading
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (createItemResult is ResponseState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                            Text(
                                "Submit",
                                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorPrompt(createItemResult: ResponseState.Error<Boolean?>) {
    var isShowDialog by remember {
        mutableStateOf(true)
    }
    if (isShowDialog) {
        PortableAlertDialog(
            onDismissRequest = { isShowDialog = false },
            onConfirmation = {
                isShowDialog = false
            },
            dialogTitle = "Error",
            dialogText = createItemResult.message,
            icon = TablerIcons.AccessPointOff
        )
    }
}



@Preview(showBackground = true)
@Composable
fun CreateNewItemFormPreview() {
    Surface {
        CreateNewItemForm(code = "ITEM001")
    }
}

@Preview(showBackground = true)
@Composable
fun CreateNewOnePreview() {
    CreateNewOne(id = "12345")
}


@Preview
@Composable
fun CreateNewItemScreenPreview() {
//    NewItemScreen(id = "123")
}