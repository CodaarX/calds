package com.decagonhq.clads.ui.profile.bottomnav

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.decagonhq.clads.R
import com.decagonhq.clads.data.domain.PhotoGalleryModel
import com.decagonhq.clads.databinding.PhotoGalleryEditImageFragmentBinding
import com.decagonhq.clads.util.DataListener
import com.decagonhq.clads.util.photosProvidersList

class PhotoGalleryEditImageFragment : Fragment() {
    private var _binding: PhotoGalleryEditImageFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var photoIV: Uri
    private lateinit var imageName: String
    private val args: PhotoGalleryEditImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = PhotoGalleryEditImageFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        photoIV = args.imageUri.toUri()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoImageView = binding.mediaFragmentRecyclerViewPhotoImageView
        photoIV = args.imageUri.toUri()
        imageName = args.imageName

        DataListener.imageListener.value = false

        Glide.with(this)
            .load(photoIV)
            .into(photoImageView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.photo_gallery_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.media_share -> sharePhoto()
            R.id.media_edit -> editPhoto()
            R.id.media_delete -> {
                deletePhoto()
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // method to share photo
    private fun sharePhoto() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoIV)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
    }

    // delete photo
    private fun deletePhoto() {
        val photoGalleryModel = PhotoGalleryModel(photoIV, imageName)
        photosProvidersList.remove(photoGalleryModel)
    }

    // method to edit photo
    private fun editPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}