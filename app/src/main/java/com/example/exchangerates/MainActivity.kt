package com.example.exchangerates

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.exchangerates.model.TabItem
import com.example.exchangerates.ui.theme.ExchangeRatesTheme
import com.example.exchangerates.viewmodel.ExchangeRatesUIState
import com.example.exchangerates.viewmodel.ExchangeRatesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExchangeRatesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}


@Composable
fun CalculatorApp() {
    val items = listOf(
        TabItem("Home", Icons.Filled.Home, route = "Home"),
        TabItem("Image", Icons.Filled.Favorite, route = "Image"),
        TabItem("Info", Icons.Filled.Info, route = "Info"),
    )
    BasicLayout(items)
}

@Composable
fun ErrorScreen() {
    androidx.compose.material.Text("Error retrieving exchange rates. Conversion cannot be used.")
}

@Composable
fun LoadingScreen() {
    androidx.compose.material.Text("Loading exchange rates...")
}

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun BasicLayout(items: List<TabItem>,) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text(stringResource(R.string.currency_converter_1)) },
            )
        },
        content = { MyNavController(navController = navController) },

        bottomBar = {
            MyBottomNavigation(items, navController)
        },
    )

}

@Composable
fun CalculatorScreen(eurInput: String, vndOutput: Double, changeEur:(value: String) -> Unit, convert:() -> Unit) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.currency_calculator),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = {Text(text = stringResource(R.string.enter_euros))},
            value= eurInput,
            onValueChange = { changeEur((it.replace(',','.')))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.result,String.format("%.2f",vndOutput).replace(',','.')),
            modifier = Modifier.padding(start = 16.dp)
        )
        Button(
            onClick = {
                convert()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.calculate))
        }
    }
}

@Composable
fun MyBottomNavigation(items: List<TabItem>, navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    BottomNavigation {
        items.forEachIndexed{index,item ->
            BottomNavigationItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                icon = {Icon(item.icon, contentDescription = null)},
                label = { Text(item.label)}
            )
        }

    }

}

@Composable
fun MainScreen() {
    ExchangeRatesComponent()
}

@Composable
fun ImageScreen() {
    ImageCoil()
}

@Composable
fun ImageCoil() {
    Image(painter = rememberAsyncImagePainter("https://www.fxexchangerate.com/static/pair/vnd/vndeur.webp"),
        contentDescription = null,
        modifier = Modifier
            .size(400.dp))


}

@Composable
fun InfoScreen() {
    androidx.compose.material.Text(text = "Info Screen")
}

@Composable
fun MyNavController(navController: NavHostController) {

    NavHost(
        navController= navController,
        startDestination = "Home"
    ) {
        composable(route = "Home") {
            MainScreen()
        }
        composable(route = "Image") {
            ImageScreen()
        }
        composable(route = "Info") {
            InfoScreen()
        }
    }
}


@Composable
fun ExchangeRatesComponent(exchangeRatesViewModel: ExchangeRatesViewModel = viewModel()) {
    when (exchangeRatesViewModel.exchangeRatesUIState) {
        is ExchangeRatesUIState.Success -> CalculatorScreen(
            eurInput = exchangeRatesViewModel.eurInput,
            vndOutput = exchangeRatesViewModel.vndOutput,
            changeEur = { exchangeRatesViewModel.changeEur(it) },
            convert =  { exchangeRatesViewModel.convert() }
        )
        is ExchangeRatesUIState.Error -> ErrorScreen()
        is ExchangeRatesUIState.Loading -> LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    ExchangeRatesTheme {
        CalculatorApp()
    }
}