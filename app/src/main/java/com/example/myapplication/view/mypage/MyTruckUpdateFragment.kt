package com.example.myapplication.view.mypage

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
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
import com.bumptech.glide.Glide
import com.example.data.dto.FoodTruckHeader
import com.example.data.dto.FoodType
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentMyTruckUpdateBinding
import com.example.myapplication.viewmodel.MyPageViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class MyTruckUpdateFragment : Fragment() {

    private var _binding: FragmentMyTruckUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedFoodTruck: FoodTruckHeader
    private val viewModel: MyPageViewModel by viewModels()
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyTruckUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = MySharedPreferences.getToken(requireContext())


        selectedFoodTruck = arguments?.getSerializable("selectedFoodTruck") as FoodTruckHeader

        // Setting up the views with the selected food truck details
        binding.editTextText.setText(selectedFoodTruck.name)
        binding.editTextText2.setText(selectedFoodTruck.description)

        Glide.with(this)
            .load(selectedFoodTruck.imageUrl)
            .into(binding.profile)

        // Spinner 설정
        val foodTypes = FoodType.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, foodTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        val spinnerPosition = adapter.getPosition(selectedFoodTruck.foodType)
        binding.spinner.setSelection(spinnerPosition)

        binding.profile.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        // Set up button click listeners

        binding.button.setOnClickListener {
            val updatedName = binding.editTextText.text.toString()
            val updatedDescription = binding.editTextText2.text.toString()
            val updatedFoodType = binding.spinner.selectedItem.toString()

            selectedImageUri?.let { uri ->
                val imageFile = getFileFromUri(uri)
                val token = MySharedPreferences.getToken(requireContext())

                lifecycleScope.launch {
                    val foodTruckId = selectedFoodTruck.id.toInt()

                    val response = viewModel.updateFoodTruck(token, foodTruckId, updatedName, updatedFoodType, updatedDescription, imageFile)
                    if (response.isSuccessful) {
                        Log.d(TAG, "Food truck updated successfully")
                    } else {
                        Log.e(TAG, "Failed to update food truck. Error: ${response.errorBody()?.string()}")
                    }
                }
            } ?: run {
                Snackbar.make(requireView(), "이미지를 선택하세요.", Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.button2.setOnClickListener {
            // Handle menu update logic
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFileFromUri(uri: Uri): File {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) } ?: ""
        cursor?.close()
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

}
