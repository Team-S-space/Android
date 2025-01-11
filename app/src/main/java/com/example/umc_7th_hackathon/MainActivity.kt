package com.example.umc_7th_hackathon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_7th_hackathon.databinding.ActivityMainBinding
import com.example.umc_7th_hackathon.review.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.ChipGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.util.FusedLocationSource
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date as Date

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

    private lateinit var sunriseBtn: ImageButton
    private lateinit var sunsetBtn: ImageButton

    // 일출 및 일몰 시간 (예: 06:30, 18:00)
    private val sunriseTime = "06:30"
    private val sunsetTime = "18:00"

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

        // 일출 일몰 버튼
        sunriseBtn = binding.sunriseBtn
        sunsetBtn = binding.sunsetBtn

        // 초기 상태 설정
        initializeButtonState()

        // 버튼 클릭 이벤트 설정
        sunriseBtn.setOnClickListener { onButtonClick(true) }
        sunsetBtn.setOnClickListener { onButtonClick(false) }
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
            setupChipListener()
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

    // 지역 선택 시 지도 이동
    private fun setupChipListener() {
        val chipGroup = binding.chipGroup
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip01 -> {
                    moveToLocation(35.9078, 127.7669, 6.0)
                }
                R.id.chip02 -> { // 서울 Chip 선택
                    moveToLocation(37.5665, 126.9780, 11.0) // 서울 좌표와 줌 레벨
                }
                R.id.chip03 -> {
                    moveToLocation(37.4138, 127.5183, 8.0) // 경기도
                }
                R.id.chip04 -> {
                    moveToLocation(37.4563, 126.7052, 10.0) // 인천
                }
                R.id.chip05 -> {
                    moveToLocation(37.5558, 128.2093, 9.0) // 강원
                }
                R.id.chip06 -> {
                    moveToLocation(36.5184, 126.8000, 11.0) // 충남
                }
                R.id.chip07 -> {
                    moveToLocation(36.4800, 127.2890, 11.0) // 충북
                }
                R.id.chip08 -> {
                    moveToLocation(35.7175, 127.1530, 10.0) // 전북
                }
                R.id.chip09 -> {
                    moveToLocation(34.8679, 126.9910, 10.0) // 전남
                }
                R.id.chip10 -> {
                    moveToLocation(36.2486, 128.6648, 10.0) // 경북
                }
                R.id.chip11 -> {
                    moveToLocation(	35.2384, 128.6920, 10.0) // 경남
                }
                R.id.chip12 -> {
                    moveToLocation(	33.4996, 126.5312, 10.0) // 경남
                }
            }
        }
    }

    private fun moveToLocation(latitude: Double, longitude: Double, zoomLevel: Double) {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(latitude, longitude), zoomLevel)
            .animate(CameraAnimation.Easing) // 애니메이션 효과 추가
        naverMap.moveCamera(cameraUpdate)
    }

    // 일출 일몰 버튼 수정
    private fun setButtonState(sunriseActive: Boolean, sunsetActive: Boolean) {
        sunriseBtn.tag = if (sunriseActive) "active" else "inactive"
        sunsetBtn.tag = if (sunsetActive) "active" else "inactive"

        sunriseBtn.setImageResource(
            if (sunriseActive) R.drawable.ic_sunrise_on else R.drawable.ic_sunrise_off
        )
        sunsetBtn.setImageResource(
            if (sunsetActive) R.drawable.ic_sunset_on else R.drawable.ic_sunset_off
        )
    }

    private fun onButtonClick(isSunrise: Boolean) {
        val isSunriseActive = sunriseBtn.tag == "active"
        val isSunsetActive = sunsetBtn.tag == "active"

        when {
            isSunrise && isSunriseActive && isSunsetActive -> {
                // 둘 다 활성화 → "일출" 비활성화
                setButtonState(sunriseActive = false, sunsetActive = true)
            }
            !isSunrise && isSunriseActive && isSunsetActive -> {
                // 둘 다 활성화 → "일몰" 비활성화
                setButtonState(sunriseActive = true, sunsetActive = false)
            }
            isSunrise && !isSunriseActive -> {
                // "일출" 버튼 활성화
                setButtonState(sunriseActive = true, sunsetActive = isSunsetActive)
            }
            !isSunrise && !isSunsetActive -> {
                // "일몰" 버튼 활성화
                setButtonState(sunriseActive = isSunriseActive, sunsetActive = true)
            }
        }
    }

    private fun initializeButtonState() {
        val currentTime = getCurrentTime()
        val isSunrise = currentTime!! < sunriseTime && currentTime!! > sunsetTime // 일출
        val isSunset = currentTime!! < sunsetTime && currentTime!! > sunriseTime // 일몰

        when {
            isSunrise -> {
                setButtonState(sunriseActive = true, sunsetActive = false)
            }
            isSunset -> {
                setButtonState(sunriseActive = false, sunsetActive = true)
            }
        }
    }

    fun getCurrentTime() : String?{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
