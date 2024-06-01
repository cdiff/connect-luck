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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentFoodtruckMenuCreateBinding
import com.example.myapplication.viewmodel.MyPageViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class FoodTruckMenuCreateFragment : Fragment() {

    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var binding: FragmentFoodtruckMenuCreateBinding
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
        binding = FragmentFoodtruckMenuCreateBinding.inflate(inflater, container, false)
        val view = binding.root

        val id = arguments?.getSerializable("id") as Int

        // Retrieve the foodTruckId from the arguments

        binding.profile.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        binding.button.setOnClickListener {
            val name = binding.editTextText.text.toString()
            val description = binding.editTextText2.text.toString()
            val price = binding.editTextText3.text.toString().toDoubleOrNull()

            if (price == null) {
                Snackbar.make(requireView(), "가격을 올바르게 입력하세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            selectedImageUri?.let { uri ->
                val imageFile = getFileFromUri(uri)
                val token = MySharedPreferences.getToken(requireContext())

                lifecycleScope.launch {
                    val response = viewModel.addFoodTruckMenu(token, id, name,
                        price.toInt(), description, imageFile)
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.action_foodTruckMenuCreateFragment_to_myPageFragment)

                        Log.d(TAG, "Menu registration successful.")
                    } else {
                        Log.d(TAG, "Menu registration failed.")
                    }
                }
            } ?: run {
                Snackbar.make(requireView(), "이미지를 선택하세요.", Snackbar.LENGTH_SHORT).show()
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
        private const val TAG = "FoodTruckMenuCreateFragment"
    }
}
