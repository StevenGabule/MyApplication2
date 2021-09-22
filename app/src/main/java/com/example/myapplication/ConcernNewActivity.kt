package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant.Companion.PUBLIC_IP
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ConcernNewActivity : AppCompatActivity() {
  private lateinit var imageView: ImageView
  private lateinit var name: TextInputEditText
  private lateinit var description: TextInputEditText
  private var imageData: ByteArray? = null
  private lateinit var imageButton: Button
  private lateinit var adviserACTV: AutoCompleteTextView
  private lateinit var submitButtonView: Button
  private val postURL: String = "$PUBLIC_IP/concern"
  private val fetchAdvisers: String = "$PUBLIC_IP/get-adviser-by-id"

  companion object {
    private const val IMAGE_PICK_CODE = 999
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_concern_new)
    this.title = "Add new concern"

    imageView = findViewById(R.id.imageView)

    name = findViewById(R.id.title_EditText)
    description = findViewById(R.id.description_EditText)

    imageButton = findViewById(R.id.upload_image_buttonView)
    imageButton.setOnClickListener {
      launchGallery()
    }

    submitButtonView = findViewById(R.id.submit_buttonView)
    submitButtonView.setOnClickListener {
      uploadImage()
    }
    val issueDropDown = findViewById<AutoCompleteTextView>(R.id.issueAutoCompleteTextView)
    adviserACTV = findViewById(R.id.adviserAutoCompleteTextView)

    val issues = resources.getStringArray(R.array.issues)
    val adapter = ArrayAdapter(this, R.layout.list_item, issues)
    issueDropDown.setAdapter(adapter)

    // Auto complete threshold
    // The minimum number of characters to type to show the drop down
    // issueDropDown.threshold = 10

    issueDropDown.onItemClickListener = OnItemClickListener { parent, _, position, id->
      val selectedItem = parent.getItemAtPosition(position).toString()
      // Display the clicked item using toast
      Toast.makeText(this,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
      println("Selected : $selectedItem, id: $id")
      val requestQueue = Volley.newRequestQueue(this)
      val postData = JSONObject()

      try {
        postData.put("id", id.toString())
      } catch (e: JSONException) {
        e.printStackTrace()
      }

      val listNames = mutableListOf("")
      val request = JsonArrayRequest(
        Request.Method.POST,
        "$fetchAdvisers/$id",
        null,
        { response ->
//          println("response is: $response")
          for (x in 0 until response.length()) {
            println("response is: $x")
            listNames += listOf(response.getJSONObject(x).getJSONObject("postedBy")["name"].toString())
          }
          val adapter1 = ArrayAdapter(this, R.layout.list_item, listNames)
          adviserACTV.setAdapter(adapter1)
        },
        {
          println("error is: $it")
        }
      )
      requestQueue.add(request)
    }

    /*issueDropDown.doOnTextChanged { inputText, d, e, i ->
      Log.d("LOGGING", "$inputText")
      Log.d("i", "$i")
      Log.d("d", "$d")
      Log.d("e", "$e")
      val requestQueue = Volley.newRequestQueue(this)
      val postData = JSONObject()

      try {
        postData.put("id", i.toString())
      } catch (e: JSONException) {
        e.printStackTrace()
      }
      val listNames = mutableListOf("")
      val request = JsonArrayRequest(
        Request.Method.POST,
        "$fetchAdvisers/$i",
        null,
        { response ->
//          println("response is: $response")
          for (x in 0 until response.length()) {
            println("response is: $x")
            listNames += listOf(response.getJSONObject(x).getJSONObject("postedBy")["name"].toString())
          }
          val adapter1 = ArrayAdapter(this, R.layout.list_item, listNames)
          adviserACTV.setAdapter(adapter1)
        },
        {
          println("error is: $it")
        }
      )
      requestQueue.add(request)
    }*/

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun launchGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(Intent.createChooser(intent, "Browse a picture"), IMAGE_PICK_CODE)
  }

  private fun uploadImage() {
    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)
    imageData ?: return
    val request = object : VolleyFileUploadRequest(
      Method.POST,
      postURL,
      Response.Listener {
        println("response is: $it")
      },
      Response.ErrorListener {
        println("error is: $it")
      }
    ) {
      override fun getByteData(): MutableMap<String, FileDataPart> {
        val params = HashMap<String, FileDataPart>()
        params["concern_file"] = FileDataPart("image", imageData!!, "jpeg")
        return params
      }

      override fun getParams(): Map<String, String> {
        val params: MutableMap<String, String> = HashMap()
        params["title"] = name.text.toString().trim()
        params["description"] = description.text.toString().trim()
        return params
      }

      override fun getHeaders(): MutableMap<String, String> {
        val params: MutableMap<String, String> = HashMap()
        params["Authorization"] = "Bearer $token"
        return params
      }
    }
    Volley.newRequestQueue(this).add(request)
  }

  @Throws(IOException::class)
  private fun createImageData(uri: Uri) {
    val inputStream = contentResolver.openInputStream(uri)
    inputStream?.buffered()?.use {
      imageData = it.readBytes()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
      val uri = data?.data
      if (uri != null) {
        imageView.setImageURI(uri)
        createImageData(uri)
      }
    }
    super.onActivityResult(requestCode, resultCode, data)
  }
}

