package com.fahim.navapp2

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.fahim.navapp2.databinding.FragmentLoginMainBinding
import com.google.gson.Gson
import database.AppDatabase
import database.UserProfileDao
import event.EventObserver
import viewModels.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginMain : Fragment() {

    lateinit var binding: FragmentLoginMainBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var sp: SharedPreferences

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
        binding = FragmentLoginMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = activity?.let { ViewModelProvider(it).get(LoginViewModel::class.java) }
        binding.model = model

        //sp=getSharedPreferences("Shared_Info", AppCompatActivity.MODE_PRIVATE)
        //val edi: SharedPreferences.Editor=sp.edit()

        val navOP = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_default_pop_enter_anim)
            .build()

        model?.isLoading?.observe(requireActivity(), {
            view.findViewById<ProgressBar>(R.id.progressBar).visibility =
                if (model.isLoading.value == true) View.VISIBLE else View.INVISIBLE
        })

        model?.error?.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

            if (it == "Success") {
                model?.insertToDB(dbBuild(), model.call.response)
            }
        })

        model?.dbError?.observe(viewLifecycleOwner, EventObserver{
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                .show()
            Log.i("DBerror", it)

            if (it == "Successful DB operation") {
                val args = Bundle()
                val g = Gson()
                args.putString("info", g.toJson(model.call))
                findNavController().navigate(R.id.action_loginMain_to_infoMain, args, navOP)
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun dbBuild():UserProfileDao
    {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "User_Database"
        ).build()
        val userDao = db.userDao()

        return userDao
    }

}