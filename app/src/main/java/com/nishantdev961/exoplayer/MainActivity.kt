package com.nishantdev961.exoplayer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private val PICK_VIDEO = 1
    var videoUri: Uri ? = null
    lateinit var mediaController: MediaController

    val dbStorage by lazy {
        FirebaseStorage.getInstance().getReference("Video")
    }
    val db by lazy {
        FirebaseDatabase.getInstance().getReference("video")
    }

    lateinit var member:Member
    lateinit var uploadTask: UploadTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaController = MediaController(this)
        videoView_main.setMediaController(mediaController)
        videoView_main.start()

        btn_chooseVideo.setOnClickListener(this)
        btn_showVideo.setOnClickListener(this)
        btn_uploadVideo.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_VIDEO
            || resultCode == RESULT_OK
            || data!=null
            || data?.data!=null
        ){
            videoUri = data?.data!!
            videoView_main.setVideoURI(videoUri)
        }
    }

    fun getExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        var mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onClick(view: View) {

        when(view.id){
            R.id.btn_chooseVideo->{
                chooseVideo()
            }
            R.id.btn_showVideo->{
                showVideo()
            }
            R.id.btn_uploadVideo->{
                uploadVideo()
            }
        }
    }

    private fun uploadVideo() {

        val videoName = etext_videoName.text.toString()
        val search = videoName.toLowerCase()
        if(videoUri != null || videoName.isNotEmpty()){

            progressBar_main.visibility = View.VISIBLE

            val storageRef = dbStorage.child(System.currentTimeMillis().toString()+"."+getExtension(videoUri!!))
            uploadTask = storageRef.putFile(videoUri!!)

            var urlTask : Task<Uri> = uploadTask.continueWithTask(object:Continuation<UploadTask.TaskSnapshot, Task<Uri>>{

                override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if(!task.isSuccessful){
                        throw task.exception!!
                    }
                    return storageRef.downloadUrl
                }

            })
                .addOnCompleteListener(object:OnCompleteListener<Uri> {
                    override fun onComplete(task: Task<Uri>) {

                        if(task.isSuccessful){
                            val downloadUrl: Uri? = task.result
                            Toast.makeText(this@MainActivity,"Video Uploaded Success", Toast.LENGTH_LONG).show()

                            member = Member(videoName, downloadUrl.toString(), search)

                            val result: String = db.push().key.toString()
                            db.child(result).setValue(member)
                        }
                        else{
                            Toast.makeText(this@MainActivity,"Video Uploaded Failed", Toast.LENGTH_LONG).show()
                        }
                        progressBar_main.visibility = View.GONE
                    }

                })
        }
        else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }

    private fun chooseVideo(){
        val i = Intent();
        i.setType("video/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i, PICK_VIDEO)
    }

    private fun showVideo(){
        Toast.makeText(this, "Aur sunao", Toast.LENGTH_LONG).show()
        val i = Intent(this, ShowVideo::class.java)
        startActivity(i)
        finish()
    }
}