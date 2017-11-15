package com.gumio_inf.android.raamemo.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.gumio_inf.android.raamemo.R
import com.gumio_inf.android.raamemo.model.RaamenItem
import com.gumio_inf.android.raamemo.model.ShopItem
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class DataInputActivity : AppCompatActivity(), android.location.LocationListener {
    private val REQUEST_PERMISSION = 1000
    private var locationManager: LocationManager? = null
    private var photo: Bitmap? = null

    private var latitude: Double = 0.0
    private var longitued: Double = 0.0

    private var shop: EditText? = null
    private var raamen: EditText? = null
    private var taste: EditText? = null
    private var locate: EditText? = null
    private var memo: EditText? = null
    private var picture: ImageView? = null

    private var bitmapStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_input)
        // LocationManager インスタンス生成
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        shop = (findViewById(R.id.editShop) as EditText?)!!
        raamen = (findViewById(R.id.editRaamenName) as EditText?)!!
        locate = (findViewById(R.id.editLocate) as EditText?)!!
        taste = (findViewById(R.id.editTaste) as EditText?)!!
        memo = (findViewById(R.id.editMemo) as EditText?)!!
        picture = (findViewById(R.id.icon) as ImageView?)
    }

    fun getLocateShop(v: View) {
        Toast.makeText(this, "少しまってね〜", Toast.LENGTH_LONG).show()
        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission()
        } else {
            locationStart()
        }
    }

    // 位置情報許可の確認
    fun checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationStart()
        } else {
            requestLocationPermission()
        }// 拒否していた場合
    }

    // 許可を求める
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION)

        } else {
            val toast = Toast.makeText(this, "許可してくれないとわからないよ〜", Toast.LENGTH_SHORT)
            toast.show()

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION)

        }
    }

    // 結果の受け取り
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "場所によっては取得できないよ！！", Toast.LENGTH_LONG).show()
                locationStart()
                return

            } else {
                // それでも拒否された時の対応
                val toast = Toast.makeText(this, "なんで拒否するの！？", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun locationStart() {
        // LocationManager インスタンス生成
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        startGPS()
    }

    protected fun startGPS() {
        Log.d("LocationActivity", "gpsEnabled")
        val gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            // GPSを設定するように促す
            enableLocationSettings()
        }

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.requestLocationUpdates")
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                // minTime = 1000msec, minDistance = 50m
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTime.toLong(), MinDistance, this)
            } catch (e: Exception) {
                e.printStackTrace()

                val toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT)
                toast.show()

                enableLocationSettings()
            }

        }

        super.onResume()
    }

    override fun onPause() {
        super.onPause()

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.removeUpdates")
            // update を止める
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager!!.removeUpdates(this)
        }
    }

    override fun onLocationChanged(location: Location) {

        latitude = location.latitude
        longitued = location.longitude

        locate?.setText(getLocate())
        stopGPS()
    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    private fun enableLocationSettings() {
        Toast.makeText(this, "GPSをオンにしてね！", Toast.LENGTH_LONG).show()
        val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingsIntent)
    }

    private fun stopGPS() {
        if (locationManager != null) {
            Log.d("LocationActivity", "onStop()")
            // update を止める
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            locationManager!!.removeUpdates(this)
        }
    }


    public override fun onStop() {
        super.onStop()
        stopGPS()
    }

    protected fun getLocate(): String {
        var ret = ""
        try {
            val gcd = Geocoder(this, Locale.JAPAN)
            val addresses = gcd.getFromLocation(latitude, longitued, 1)
            if (addresses.size > 0) {
                ret = addresses[0].getAddressLine(1)
                if (ret.length > 0) {
                    ret = ret.substring(10)
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d("現在地:", ret)
        return ret
    }

    //カメラで撮影した情報を渡す
    fun onCameraUp(v: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    //ギャラリーから選択された情報を渡す
    fun onPhotoUp(v: View) {
        val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    //暗黙的インテントで選択された時に呼ばれる
    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                //ギャラリーから画像を取得したとき
                try {
                    var inputStream: InputStream = contentResolver.openInputStream(intent.data)
                    // 画像サイズ情報を取得する
                    val imageOptions = BitmapFactory.Options()
                    imageOptions.inPreferredConfig = Bitmap.Config.RGB_565
                    imageOptions.inJustDecodeBounds = true
                    BitmapFactory.decodeStream(inputStream, null, imageOptions)

                    inputStream.close()

                    // もし、画像が大きかったら縮小して読み込む
                    //  今回はimageSizeMaxの大きさに合わせる
                    val imageSizeMax = 500
                    inputStream = contentResolver.openInputStream(intent.data)
                    val imageScaleWidth = imageOptions.outWidth.toFloat() / imageSizeMax
                    val imageScaleHeight = imageOptions.outHeight.toFloat() / imageSizeMax

                    // もしも、縮小できるサイズならば、縮小して読み込む
                    if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                        val imageOptions2 = BitmapFactory.Options()

                        // 縦横、小さい方に縮小するスケールを合わせる
                        val imageScale = Math.floor((if (imageScaleWidth > imageScaleHeight) imageScaleHeight else imageScaleWidth).toDouble()).toInt()

                        // inSampleSizeには2のべき上が入るべきなので、imageScaleに最も近く、かつそれ以下の2のべき上の数を探す
                        var i = 2
                        while (i <= imageScale) {
                            imageOptions2.inSampleSize = i
                            i *= 2
                        }

                        photo = BitmapFactory.decodeStream(inputStream, null, imageOptions2)
                    } else {
                        photo = BitmapFactory.decodeStream(inputStream)
                    }

                    inputStream.close()

                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "エラー", Toast.LENGTH_SHORT).show()
                }

                //BitmapをStringにしてDBに格納できるようにする
                val baos = ByteArrayOutputStream()
                photo?.compress(Bitmap.CompressFormat.PNG, 100, baos)
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                Toast.makeText(applicationContext, "アップロードしたよ！", Toast.LENGTH_SHORT).show()

            } else if (requestCode == REQUEST_CODE_CAMERA) {
                //カメラで撮影された時の対処
                val photo = intent.extras.get("data") as Bitmap
                val photoRe = Bitmap.createScaledBitmap(photo, 300, 300, false)

                //BitmapをStringにしてDBに格納できるようにする
                val baos = ByteArrayOutputStream()
                photoRe.compress(Bitmap.CompressFormat.PNG, 100, baos)
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                Toast.makeText(applicationContext, "アップロードしたよ！", Toast.LENGTH_SHORT).show()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(applicationContext, "CANCEL", Toast.LENGTH_SHORT).show()
        }
    }

    //保存ボタンをメニューバーに設置
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_memo_save, menu)
        return true
    }

    //保存ボタンが押されたことを検知する
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (R.id.create_save == id) {
            saveMemo()
            finish()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //DBへの格納
    fun saveMemo() {
        val shopItem = ShopItem()
        //店の情報
        shopItem.name = shop?.text.toString()
        shopItem.longitude = longitued
        shopItem.latitude = latitude
        shopItem.location = locate?.text.toString()
        shopItem.save()

        //ラーメンの情報
        val raamenItem = RaamenItem()
        raamenItem.name = raamen?.text.toString()
        raamenItem.createdDt = Date()
        raamenItem.picture = bitmapStr
        raamenItem.taste = taste?.text.toString()
        raamenItem.memo = memo?.text.toString()
        raamenItem.shopItem = shopItem

        //保存
        raamenItem.save()
        Toast.makeText(applicationContext, "保存しました", Toast.LENGTH_SHORT).show()
    }

    companion object {
        val REQUEST_CODE_CAMERA = 1
        val REQUEST_CODE_GALLERY = 2

        val MinTime = 1000
        val MinDistance = 3f
    }
}
