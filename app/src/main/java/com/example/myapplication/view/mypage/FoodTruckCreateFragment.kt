package com.example.myapplication.view.mypage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.data.dto.FoodType
import com.example.myapplication.R
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentMyFoodturckCreateBinding
import com.example.myapplication.viewmodel.MyPageViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Header
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class FoodTruckCreateFragment : Fragment() {

    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var binding: FragmentMyFoodturckCreateBinding
    private var selectedImageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isReadMediaImagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val isReadMediaAudioGranted = permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        val isReadMediaVideoGranted = permissions[Manifest.permission.READ_MEDIA_VIDEO] ?: false

        if (isReadMediaImagesGranted && isReadMediaAudioGranted && isReadMediaVideoGranted) {
            openGallery()
        } else {
            Snackbar.make(requireView(), "미디어 권한이 필요합니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.profile.setImageBitmap(bitmap)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyFoodturckCreateBinding.inflate(inflater, container, false)
        val view = binding.root

        // Spinner 설정
        val foodTypes = FoodType.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, foodTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.profile.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        binding.button.setOnClickListener {
            val name = binding.editTextText.text.toString()
            val description = binding.editTextText2.text.toString()
            val foodType = binding.spinner.selectedItem.toString()

            selectedImageUri?.let { uri ->
                val imageFile = getFileFromUri(uri)
                val token = MySharedPreferences.getToken(requireContext())

                lifecycleScope.launch {
                    val response = viewModel.registerFoodTruck(token, name, description, imageFile, foodType)
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.action_foodTruckCreateFragment_to_myPageFragment)

                        Log.d(TAG, "Food truck registration successful.")
                    } else {
                        Log.d(TAG,"${response.errorBody()?.string()} ")
                        Log.d(TAG,"${response.headers()} ")
                        Log.d(TAG,"${response.body()} ")
                        Log.d(TAG, "Food truck registration failed.")
                    }
                }
            } ?: run {
                Snackbar.make(requireView(), "이미지를 선택하세요.", Snackbar.LENGTH_SHORT).show()
            }
        }


        viewModel.foodTruckRegistered.observe(viewLifecycleOwner) { isRegistered ->
            if (isRegistered) {

                Log.d(TAG, "Food truck registration successful.")
            } else {
                Log.d(TAG, "Food truck registration failed.")
            }
        }

        return view
    }

    private fun getFileFromUri(uri: Uri): File {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) } ?: ""
        cursor?.close()
        Log.d(TAG, "Real Path: $filePath")
        return File(filePath)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionsAndOpenGallery() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    companion object {
        private const val TAG = "FoodTruckCreateFragment"
    }
}
