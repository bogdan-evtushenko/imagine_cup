package com.example.imaginecup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imaginecup.PhotoActivity.Companion.newIntent
import com.example.imaginecup.room.DatabaseClient
import com.example.imaginecup.room.Photo
import com.example.imaginecup.util.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var photosAdapter: PhotosAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(this, PreviewActivity::class.java))
        //ComputerVision().test(this)

        btnUpload.setOnClickListener {
            println(DatabaseClient.photosDao.getAll())
            uploadPhoto()
        }
        photosAdapter = PhotosAdapter(this) {
            //TODO past here start PhotoActivity
            startActivity(newIntent(this@MainActivity, it))
        }
        photosAdapter.setItems(DatabaseClient.photosDao.getAll())
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = photosAdapter
    }

    private fun uploadPhoto() {
        val chooseImageIntent = ImagePicker.getPickImageIntent(this)
        startActivityForResult(chooseImageIntent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    val bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
                    if (bitmap != null) {
                        //val photo = ImageUtils.scaleAndCropBitmap(bitmap)
                        //ImageUtils.displayRoundedPicture(this, bitmap, ibProfilePicture)
                        //DatabaseClient.photosDao.insert(photo)
                        ComputerVision().fetchInformationAboutPhoto(this, bitmap) {
                            val photo = Photo(jsonData = it, bitmap = bitmap)
                            println("Photo are uploaded")
                            println("Photo is")
                            println(photo)
                            searchView?.isIconified = true
                            DatabaseClient.photosDao.insert(photo)
                            photosAdapter.setItems(DatabaseClient.photosDao.getAll())
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as? SearchView
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
        println("Here search view $searchView")
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("here text change")
                photosAdapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    companion object {
        const val REQUEST_PICK_IMAGE = 1
    }
}
