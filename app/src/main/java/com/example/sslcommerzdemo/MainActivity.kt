package com.example.sslcommerzdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sslcommerzdemo.ui.theme.SSlCommerzDemoTheme
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import java.time.format.TextStyle

class MainActivity : ComponentActivity(), SSLCTransactionResponseListener {
    var sslCommerzInitialization: SSLCommerzInitialization? = null
    var additionalInitializer: SSLCAdditionalInitializer? = null
    var TAG = "Payment info"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SSlCommerzDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                        TextButton(
                            onClick = {initPayment("10") },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = Color.Green
                            )
                        ) {
                            Text(text = "Pay Now", color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }

    private fun initPayment(amount: String) {
        sslCommerzInitialization = SSLCommerzInitialization(
            "test643b75eb8098a",
            "test643b75eb8098a@ssl",
            amount.toDouble(),
            SSLCCurrencyType.BDT,
            "$amount",
            "",
            SSLCSdkType.TESTBOX
        )


        additionalInitializer = SSLCAdditionalInitializer()
        additionalInitializer!!.valueA = "User id: 1234"

        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .addAdditionalInitializer(additionalInitializer)
            .buildApiCall(this)
    }

    override fun transactionSuccess(transactionInfoModel: SSLCTransactionInfoModel?) {
        if (transactionInfoModel!!.riskLevel.equals("0")) {
            Log.d(TAG, "Transaction Successfully completed")
            Log.d(TAG, transactionInfoModel.tranId)
            Log.d(TAG, transactionInfoModel.bankTranId)
            Log.d(TAG, transactionInfoModel.amount)
            Log.d(TAG, transactionInfoModel.tranDate)

            Log.d("completed", transactionInfoModel.tranDate)

        } else
            Log.d(TAG, "Risk message: " + transactionInfoModel.riskTitle)
        Toast.makeText(this,"Transaction status: successful (" + transactionInfoModel.riskTitle + ")",
            Toast.LENGTH_SHORT).show()

    }

    override fun transactionFail(s: String?) {
        Log.e(TAG, "Transaction Failed")

        Toast.makeText(this, "Transaction status:$s", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    override fun closed(s: String?) {
        Log.e(TAG, "Transaction Failed: $s")
        Toast.makeText(this, "Transaction status:$s", Toast.LENGTH_SHORT).show()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SSlCommerzDemoTheme {

    }
}