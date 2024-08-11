package com.rkpandey.leafclassification

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rkpandey.leafclassification.ml.Adamaxmodel125cycle
//import com.rkpandey.leafclassification.ml.Adammodel325cycle
import com.rkpandey.leafclassification.ml.Rmsmodel225cycle
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {
//https://www.youtube.com/watch?v=gVJC1j2n9tE&t=560s
    lateinit var selectBtn: Button
    lateinit var predBtn: Button
    lateinit var resView: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        selectBtn = findViewById(R.id.selectBtn)
        predBtn = findViewById(R.id.predictBtn)
        resView = findViewById(R.id.resView)
        imageView = findViewById(R.id.imageView)

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        //tensorflow image processor
        var imageProcessor = ImageProcessor.Builder()
            //.add(NormalizeOp(0.0f,255.0f))
            //.add( TransformToGrayscaleOp()) can't figure out how to port
            .add(ResizeOp(256,256,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        selectBtn.setOnClickListener {
            var intent: Intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }
        predBtn.setOnClickListener{
            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            tensorImage = imageProcessor.process(tensorImage)
            val model = Adamaxmodel125cycle.newInstance(this)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var maxIdx = 0
            outputFeature0.forEachIndexed { index, fl ->
                if(outputFeature0[maxIdx] < fl){
                    maxIdx = index
                }
            }
            resView.setText(labels[maxIdx])

            // Releases model resources if no longer used.
            model.close()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
                var uri = data?.data;
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                imageView.setImageBitmap(bitmap)
            }
    }
}
