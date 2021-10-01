package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.data.ClientInfo
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatus
import kotlinx.android.synthetic.main.activity_subscription.*
import org.json.JSONObject

class SubscriptionActivity : AppCompatActivity() {

  private lateinit var subscriptionRG: RadioGroup
  private val tag = javaClass.simpleName
  private var paypalAmount = "449.0"

  private val url = "${Constant.PUBLIC_IP}/subscription/customer"
  private var subScriptionType = 1

  @SuppressLint("LongLogTag")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val checkoutConfig = CheckoutConfig(
      application = this.application,
      clientId = com.example.myapplication.paypal.Config.PAYPAL_CLIENT_ID,
      environment = Environment.SANDBOX,
      returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
      currencyCode = CurrencyCode.PHP,
      userAction = UserAction.PAY_NOW,
      settingsConfig = SettingsConfig(
        loggingEnabled = true,
        shouldFailEligibility = false
      )
    )

    PayPalCheckout.setConfig(checkoutConfig)
    setContentView(R.layout.activity_subscription)
    this.title = "Subscription Option"
    subscriptionRG = findViewById(R.id.subscriptionRG)
    val contextView = findViewById<View>(R.id.context_view)

    subscriptionRG.setOnCheckedChangeListener { group, checkedId ->
      val radioId = findViewById<RadioButton>(checkedId)
      Log.d("radioId", "${radioId.text}")
      if (radioId.text == "PHP 449 One Time Payment") {
        paypalAmount = "449.0"
        subScriptionType= 1
      }

      if (radioId.text == "PHP 958 Monthly Payment") {
        paypalAmount = "958.0"
        subScriptionType= 2
      }

      if (radioId.text == "PHP 10,034 Year Payment") {
        paypalAmount = "10034.0"
        subScriptionType= 3
      }
    }

    payPalButton.onEligibilityStatusChanged =
      { buttonEligibilityStatus: PaymentButtonEligibilityStatus ->
        Log.v(tag, "OnEligibilityStatusChanged")
        Log.d(tag, "Button eligibility status: $buttonEligibilityStatus")
      }

    payPalButton.setup(
      createOrder = CreateOrder { createOrderActions ->
        val order = Order(
          intent = OrderIntent.CAPTURE,
          appContext = AppContext(
            userAction = UserAction.PAY_NOW
          ),
          purchaseUnitList = listOf(
            PurchaseUnit(
              amount = Amount(
                currencyCode = CurrencyCode.USD,
                value = paypalAmount
              )
            )
          )
        )

        createOrderActions.create(order)
      },
      onApprove = OnApprove { approval ->
        approval.orderActions.capture { captureOrderResult ->
          val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
          val token = pref?.getString("TOKEN", null)

          Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
          val rq: RequestQueue = Volley.newRequestQueue(this)
          val postData = JSONObject()
          try {
            postData.put("subscriptionType", subScriptionType)

            val jar = @SuppressLint("LongLogTag")
            object : JsonObjectRequest(Method.POST, url, postData, { response ->
              Log.d("API_CAPTURE_PAYMENT_SUCCESS", "$response")
              finish()
            }, { error ->
              Log.e("API_CAPTURE_PAYMENT_ERROR", "$error.message")
              Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }) {
              override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $token"
                return params
              }
            }
            rq.add(jar)
          } catch (e: Exception) {
            Log.e("API_CAPTURE_PAYMENT_ERROR", "$e")
          }

          val intent = Intent(this, PaymentSuccessActivity::class.java)
          startActivity(intent)
          finish()

        }
      }
    )
  }

  /*private fun setupPaymentButton() {
    paymentButton.setup(
      createOrder = CreateOrder { createOrderActions ->
        Log.v(tag, "CreateOrder")
        createOrderActions.create(
          Order.Builder()
            .appContext(
              AppContext(
                userAction = UserAction.PAY_NOW
              )
            )
            .intent(OrderIntent.CAPTURE)
            .purchaseUnitList(
              listOf(
                PurchaseUnit.Builder()
                  .amount(
                    Amount.Builder()
                      .value("0.01")
                      .currencyCode(CurrencyCode.USD)
                      .build()
                  )
                  .build()
              )
            )
            .build()
            .also { Log.d(tag, "Order: $it") }
        )
      },
      onApprove = OnApprove { approval ->
        Log.v(tag, "OnApprove")
        Log.d(tag, "Approval details: $approval")
        approval.orderActions.capture { captureOrderResult ->
          Log.v(tag, "Capture Order")
          Log.d(tag, "Capture order result: $captureOrderResult")
        }
      },
      onCancel = OnCancel {
        Log.v(tag, "OnCancel")
        Log.d(tag, "Buyer cancelled the checkout experience.")
      },
      onError = OnError { errorInfo ->
        Log.v(tag, "OnError")
        Log.d(tag, "Error details: $errorInfo")
      }
    )
  }*/

  /* companion object {
     fun startIntent(context: Context): Intent {
       return Intent(context, SubscriptionActivity::class.java)
     }
   }*/
}