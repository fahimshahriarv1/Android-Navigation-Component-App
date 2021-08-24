package com.fahim.navapp2

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.fahim.navapp2.databinding.FragmentInfoMainBinding
import com.google.gson.Gson
import data.Response
import data.ResponseB2b
import database.AppDatabase
import database.UserProfileDao
import event.EventObserver
import viewModels.InfoViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoMain : Fragment() {
    private val camRequest = 220

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentInfoMainBinding
    lateinit var info: ResponseB2b
    lateinit var model: InfoViewModel
    private val readStorage = 227
    private var photo: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = activity?.let { ViewModelProvider(it).get(InfoViewModel::class.java) }!!
        binding.model = model

        val navOP = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .build()
        val g=Gson()
        info = g.fromJson(arguments?.get("info").toString(),ResponseB2b::class.java)

        model?.firstName?.set(info.response.firstName)
        model?.lastName?.set(info.response.lastName)
        model?.email?.set(info.response.email)
        model?.mobile?.set(info.response.mobileNumber ?: info.response.mobileNumber)

        view.findViewById<Button>(R.id.btGetFromApi).setOnClickListener {
            model.getAllInfoFromDB(info.response.username!!,dbBuild())
        }

        model.allinfo.observe(viewLifecycleOwner,EventObserver{
            val g=Gson()
            val s=g.fromJson(it,Response::class.java)
            model.getPaymentInfo(s.token!!)
        })

        view.findViewById<Button>(R.id.btnTakeImage).setOnClickListener {
            val x = requestPermission(android.Manifest.permission.CAMERA, camRequest)
            if (x) {
                val cameraIntent: Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, camRequest);
            }

        }
        model.imageSrc.observe(viewLifecycleOwner,{
            view.findViewById<ImageView>(R.id.imageViewPic).setImageBitmap(it)
        })

        view.findViewById<Button>(R.id.btSelect).setOnClickListener {
            val x =
                requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, readStorage)
            if (x) {
                val galleryIntent: Intent = Intent(Intent.ACTION_PICK);
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, readStorage);
            }
        }


        model?.callError?.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            if (it == "Fetch Success") {
                val g = Gson()
                val args = Bundle()
                args.putString("history", g.toJson(model.paymentInfo))
                findNavController().navigate(R.id.action_infoMain_to_paymentHistory, args, navOP)
            }
        })

    }

    private fun requestPermission(permission: String, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
                false
            } else {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === camRequest && resultCode === AppCompatActivity.RESULT_OK) {
            photo = data!!.extras!!["data"] as Bitmap?
            model.imageSrc.value = photo
        } else if (requestCode === readStorage && resultCode === AppCompatActivity.RESULT_OK) {
            photo = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)
            model.imageSrc.value = photo
        }
    }

    fun dbBuild(): UserProfileDao
    {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "User_Database"
        ).build()
        val userDao = db.userDao()

        return userDao
    }
}