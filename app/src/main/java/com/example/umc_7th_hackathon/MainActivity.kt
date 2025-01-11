package com.example.umc_7th_hackathon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_7th_hackathon.databinding.ActivityMainBinding
import com.example.umc_7th_hackathon.review.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 5000

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapView: MapView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var imgIv: ImageView // BottomSheetDialog 내부의 ImageView


    // 위치 권한 요청
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                initMapView()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                initMapView()
            }
            else -> {
                Toast.makeText(this, "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MapView 초기화
        mapView = findViewById(R.id.map_view)

        // 위치 권한 확인
        if (!hasPermission()) {
            locationPermissionRequest.launch(PERMISSIONS)
        } else {
            initMapView()
        }

        // 카메라 버튼 클릭 시 CameraActivity로 이동
        binding.cameraBt.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)

            if (::naverMap.isInitialized) {
                val location = locationSource.lastLocation
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // 현재 위치에 마커 추가
                    addMarker(latitude, longitude)

                } else {
                    Toast.makeText(this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // BottomSheetDialog 초기화
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_review, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        imgIv = bottomSheetView.findViewById(R.id.img_iv)

        // CameraActivity에서 전달된 사진 URI 확인
        val photoUri = intent.getStringExtra("photoUri")
        if (photoUri != null) {
            val uri = Uri.parse(photoUri)
            showBottomSheetWithImage(uri)
        }

    }

    //마커 추가 함수
    private fun addMarker(latitude: Double, longitude: Double) {
        val marker = com.naver.maps.map.overlay.Marker()
        marker.position = com.naver.maps.geometry.LatLng(latitude, longitude)
        marker.map = naverMap
    }


    private fun showBottomSheetWithImage(photoUri: Uri) {
        // BottomSheetDialog의 img_iv에 사진 표시
        imgIv.setImageURI(photoUri)

        // BottomSheetDialog 표시
        bottomSheetDialog.show()
    }

    // Naver 지도 초기화
    private fun initMapView() {
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapView.getMapAsync { naverMap ->
            this.naverMap = naverMap
            naverMap.locationSource = locationSource
            naverMap.uiSettings.isLocationButtonEnabled = true
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    // 권한 확인 메서드
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    // MapView 생명주기 메서드
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}