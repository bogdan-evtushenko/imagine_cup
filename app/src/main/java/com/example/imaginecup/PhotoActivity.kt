package com.example.imaginecup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.imaginecup.room.DatabaseClient
import com.example.imaginecup.room.Photo
import com.example.imaginecup.util.ImagePicker
import com.example.imaginecup.util.ImageUtils
import com.google.gson.Gson
import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {

    private lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val photoJson = intent.getStringExtra(PHOTO_ID) as String

        photo = DatabaseClient.photosDao.getAll().find { it.jsonData == photoJson }!!

        val analysisResult = Gson().fromJson(photo.jsonData, AnalysisResult::class.java)
        // Log.v("idk", analysisResult.description.captions[0].text)
        val microsoftDesc = analysisResult.description.captions[0].text
        val desc = analysisResult.description.tags.joinToString()
        var objects = mutableListOf<String>()
        analysisResult.tags.forEach {
            objects.add(it.name)
            Log.d("idk", it.name)
        }
        ImageUtils.displayRoundedPicture(this, photo.bitmap, ivPhoto)

        microsoftText.text = microsoftDesc
        descText.text = desc
        objectsText.text = objects.joinToString()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                DatabaseClient.photosDao.delete(photo)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val PHOTO_ID = "photo_id"

        fun newIntent(context: Context, photo: Photo): Intent {
            val intent = Intent(context, PhotoActivity::class.java)
            intent.putExtra(PHOTO_ID, photo.jsonData)
            return intent
        }
    }
}
